package com.huijiayou.huijiayou.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.DialogLoading;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvitationShareActivity extends BaseActivity implements View.OnClickListener, NewHttpRequest.RequestCallback{

    @Bind(R.id.imgView_activityInvitationShare_view)
    ImageView imgView_activityInvitationShare_view;
    DialogLoading dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_share);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("好友邀请");

        dialog = new DialogLoading(this);
        imgView_activityInvitationShare_view.setDrawingCacheEnabled(true);
        shareCodePicture();
    }

    private void shareCodePicture(){
        HashMap<String,Object> map = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
        String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
        String url = NetConfig.URL + "?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
        map.put("user_id",userId);
        map.put("invite_url",url);
        new NewHttpRequest(this, NetConfig.ACCOUNT,
                NetConfig.shareCodePicture, "jsonObject", 1, map, true, this).executeTask();
    }


    @OnClick({R.id.tv_activityInvitationShare_wechat, R.id.tv_activityInvitationShare_wxcircle})
    @Override
    public void onClick(View v) {
        Bitmap bitmap = imgView_activityInvitationShare_view.getDrawingCache();
        int scene = SendMessageToWX.Req.WXSceneSession;
        switch (v.getId()){
            case R.id.tv_activityInvitationShare_wechat:
                scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case R.id.tv_activityInvitationShare_wxcircle:
                scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
        }
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        wxMediaMessage.mediaObject = wxImageObject;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = wxMediaMessage;
        req.scene = scene;
        MyApplication.msgApi.sendReq(req);
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            Object invitePicture = jsonObject.get("invitePicture");
            if (invitePicture != null){
                ImageLoader.getInstance().loadImage(invitePicture.toString(), new ImageLoadingListener() {
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
                        imgView_activityInvitationShare_view.setImageBitmap(loadedImage);
                        if (dialog != null){
                            dialog.dismiss();
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
}
