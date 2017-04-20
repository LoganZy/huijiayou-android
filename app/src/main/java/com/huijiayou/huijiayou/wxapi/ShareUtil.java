package com.huijiayou.huijiayou.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.InvitationShareActivity;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.huijiayou.huijiayou.utils.Util;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;

import static com.huijiayou.huijiayou.MyApplication.mTencent;

/**
 * Created by lugg on 2016/8/12.
 */
public class ShareUtil implements View.OnClickListener {

    Activity activity;
    String title;
    String content;
    String url;

    Dialog dialog;
    IUiListener iUiListener;

    public  void shareWebPage(Activity ac, String t, String c, String u, IUiListener iUiListener){
        activity = ac;
        title = t;
        content = c;
        url = u;
        this.iUiListener = iUiListener;
        if (TextUtils.isEmpty(t)){
            title = "抢油滴省钱加油";
        }
        if (TextUtils.isEmpty(c)){
            content = "抢到多少送多少，额外再送200元加油礼券，用了就是赚到，我会加油我骄傲！";
        }
        if (TextUtils.isEmpty(u)){
            url = NetConfig.URL;
        }
        initDialog();
    }
    void initDialog(){
        dialog = new Dialog(activity, R.style.dialog_bgTransparent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().setLayout(activity.getWindow().getWindowManager().getDefaultDisplay().getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(R.layout.dialog_share);
        TextView tv_dialog_share_wechat = (TextView) dialog.findViewById(R.id.tv_dialog_share_wechat);
        TextView tv_dialog_share_wxcircle = (TextView) dialog.findViewById(R.id.tv_dialog_share_wxcircle);
        TextView tv_dialog_share_qq = (TextView) dialog.findViewById(R.id.tv_dialog_share_qq);

        TextView tv_dialog_share_mianduimian = (TextView) dialog.findViewById(R.id.tv_dialog_share_mianduimian);
        Button btn_dialog_share_cancel = (Button) dialog.findViewById(R.id.btn_dialog_share_cancel);

        tv_dialog_share_wxcircle.setOnClickListener(onClickListener);
        tv_dialog_share_wechat.setOnClickListener(onClickListener);
        tv_dialog_share_qq.setOnClickListener(onClickListener);
        tv_dialog_share_mianduimian.setOnClickListener(onClickListener);
        btn_dialog_share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isPaySupported = false;
            switch (v.getId()){
                case R.id.tv_dialog_share_wechat:
                    isPaySupported = MyApplication.msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                    if (!isPaySupported) {
                        ToastUtils.createLongToast(activity, "您没有安装微信或者微信版本太低");
                        return;
                    }
                    shareWX(SendMessageToWX.Req.WXSceneSession);
                    break;
                case R.id.tv_dialog_share_wxcircle:
                    isPaySupported = MyApplication.msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                    if (!isPaySupported) {
                        ToastUtils.createLongToast(activity, "您没有安装微信或者微信版本太低");
                        return;
                    }
                    shareWX(SendMessageToWX.Req.WXSceneTimeline);
                    break;
                case R.id.tv_dialog_share_mianduimian:
                    activity.startActivity(new Intent(activity, InvitationShareActivity.class));
                    break;
                case R.id.tv_dialog_share_qq:
                    shareQQ();
                    break;
            }
            dialog.dismiss();
        }
    };

    private void shareWX(int scene){
        WXMediaMessage wxMediaMessage = new WXMediaMessage();

        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.share_wx);
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;

        wxMediaMessage.title = title;
        wxMediaMessage.description = content;
        wxMediaMessage.thumbData = Util.bmpToByteArray(bitmap,true);
        wxMediaMessage.mediaObject = wxWebpageObject;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = wxMediaMessage;
        req.scene = scene;
        MyApplication.msgApi.sendReq(req);
    }

    private void shareQQ(){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "会加油");
        mTencent.shareToQQ(activity, params, iUiListener);
    }

    @Override
    public void onClick(View v) {


    }

}
