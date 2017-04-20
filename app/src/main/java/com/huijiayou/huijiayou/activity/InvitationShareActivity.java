package com.huijiayou.huijiayou.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.DialogLoading;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huijiayou.huijiayou.MyApplication.mTencent;

public class InvitationShareActivity extends BaseActivity implements View.OnClickListener, NewHttpRequest.RequestCallback, IUiListener{

    @Bind(R.id.imgView_activityInvitationShare_view)
    ImageView imgView_activityInvitationShare_view;

    @Bind(R.id.tv_activityInvitationShare_qq)
    TextView tv_activityInvitationShare_qq;
    DialogLoading dialog;
    private Bitmap bitmap;
    String imageName;

    boolean is_WRITE_EXTERNAL_STORAGE = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_share);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("好友邀请");

        dialog = new DialogLoading(this);
        shareCodePicture();
        imgView_activityInvitationShare_view.setDrawingCacheEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            is_WRITE_EXTERNAL_STORAGE = true;
            tv_activityInvitationShare_qq.setVisibility(View.VISIBLE);
            imageName = System.currentTimeMillis()+".png";
            if (is_WRITE_EXTERNAL_STORAGE){
                saveBitmap(imageName);
            }
        }else if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            is_WRITE_EXTERNAL_STORAGE = false;
            tv_activityInvitationShare_qq.setVisibility(View.GONE);
        }
    }

    private void shareCodePicture(){
        HashMap<String,Object> map = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
        mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
        String url = NetConfig.H5_URL + "?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
        map.put("user_id",userId);
        map.put("invite_url",url);
        new NewHttpRequest(this, NetConfig.ACCOUNT,
                NetConfig.shareCodePicture, "jsonObject", 1, map, true, this).executeTask();
    }


    @OnClick({R.id.tv_activityInvitationShare_wechat, R.id.tv_activityInvitationShare_wxcircle, R.id.tv_activityInvitationShare_qq})
    @Override
    public void onClick(View v) {
        if (bitmap == null){
            ToastUtils.createLongToast(this,"请重新打开此页面加载图片资源,或者提交反馈");
        }else{
            boolean isPaySupported = MyApplication.msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            switch (v.getId()){
                case R.id.tv_activityInvitationShare_wechat:
                    if (!isPaySupported) {
                        ToastUtils.createLongToast(this, "您没有安装微信或者微信版本太低");
                        return;
                    }
                    shareWX(SendMessageToWX.Req.WXSceneSession);
                    break;
                case R.id.tv_activityInvitationShare_wxcircle:
                    if (!isPaySupported) {
                        ToastUtils.createLongToast(this, "您没有安装微信或者微信版本太低");
                        return;
                    }
                    shareWX(SendMessageToWX.Req.WXSceneTimeline);
                    break;
                case R.id.tv_activityInvitationShare_qq:
                    shareQQ();
                    break;
            }
        }
    }

    private void shareQQ(){
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, Environment.getExternalStorageDirectory() + "/huijiayou/"+ imageName);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "会加油");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//      params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(this, params, this);
    }

    private void shareWX(int scene){
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        wxMediaMessage.mediaObject = wxImageObject;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = wxMediaMessage;
        req.scene = scene;
        MyApplication.msgApi.sendReq(req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bitmap != null){
            imgView_activityInvitationShare_view = null;
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    @Override
    public void netWorkError() {

    }

    private void saveBitmap(String fileName){
        File file = new File(Environment.getExternalStorageDirectory() + "/huijiayou/");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Environment.getExternalStorageDirectory() + "/huijiayou/", fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            String imageUri = jsonObject.getString("invitePicture");
            if (!TextUtils.isEmpty(imageUri)){
                ImageLoader.getInstance().loadImage(imageUri, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        if (dialog != null){
                            dialog.show();
                        }
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (dialog != null){
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        int imageHeight = loadedImage.getHeight();
//                        int imageWidth = loadedImage.getWidth();
//                        int viewHeight = imgView_activityInvitationShare_view.getHeight();
//                        int viewWidth = imgView_activityInvitationShare_view.getWidth();
//                        int height,width;
//                        if ((viewWidth / imageWidth) > (viewHeight / imageHeight)){
//                            height = viewHeight;
//                        }
                        imgView_activityInvitationShare_view.setImageBitmap(loadedImage);
                        bitmap = loadedImage;
                        if (dialog != null){
                            dialog.dismiss();
                        }

                        if (ContextCompat.checkSelfPermission(InvitationShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            is_WRITE_EXTERNAL_STORAGE = false;
                            tv_activityInvitationShare_qq.setVisibility(View.GONE);
                            new AlertDialog.Builder(InvitationShareActivity.this).setTitle("提示")
                                    .setMessage("我们需要您赋予我们读取您的存储空间的权限,用于分享图片到QQ")
                                    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(InvitationShareActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                        }})
                                    .setNeutralButton("不用了", null)
                                    .show();

                        }else {
                            is_WRITE_EXTERNAL_STORAGE = true;
                            tv_activityInvitationShare_qq.setVisibility(View.VISIBLE);
                            imageName = System.currentTimeMillis()+".png";
                            if (is_WRITE_EXTERNAL_STORAGE){
                                saveBitmap(imageName);
                            }
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        if (dialog != null){
                            dialog.dismiss();
                        }
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,this);
    }


    @Override
    public void onComplete(Object o) {
        ToastUtils.createNormalToast(this, "分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        ToastUtils.createLongToast(this, uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        ToastUtils.createLongToast(this, "取消分享");
    }
}
