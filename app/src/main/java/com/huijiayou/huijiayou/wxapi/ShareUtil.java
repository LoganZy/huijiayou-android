package com.huijiayou.huijiayou.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.Util;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by lugg on 2016/8/12.
 */
public class ShareUtil implements View.OnClickListener {

    static Activity activity;
    static String title;
    static String content;
    static String url;
    static String imageUrl;

    static Bitmap bitmap;
    static int type; //1 wen   2 img
    public static void shareWebPage(Activity ac, String t, String c, String u, String i){
        activity = ac;
        title = t;
        content = c;
        url = u;
        imageUrl = i;
        type = 1;
        initDialog();
    }

    public static void shareWebPage(Activity ac, Bitmap b){
        activity = ac;
        bitmap = b;
        type = 2;
        initDialog();
    }

    static void initDialog(){
        final Dialog dialog = new Dialog(activity, R.style.dialog_bgTransparent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().setLayout(activity.getWindow().getWindowManager().getDefaultDisplay().getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(R.layout.dialog_share);
        TextView tv_dialog_share_wechat = (TextView) dialog.findViewById(R.id.tv_dialog_share_wechat);
        TextView tv_dialog_share_wxcircle = (TextView) dialog.findViewById(R.id.tv_dialog_share_wxcircle);
        TextView tv_dialog_share_qq = (TextView) dialog.findViewById(R.id.tv_dialog_share_qq);
        Button btn_dialog_share_cancel = (Button) dialog.findViewById(R.id.btn_dialog_share_cancel);

        tv_dialog_share_wxcircle.setOnClickListener(onClickListener);
        tv_dialog_share_wechat.setOnClickListener(onClickListener);
        btn_dialog_share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    static View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int scene = SendMessageToWX.Req.WXSceneSession;
            switch (v.getId()){
                case R.id.tv_dialog_share_wechat:
                    scene = SendMessageToWX.Req.WXSceneSession;
                    break;
                case R.id.tv_dialog_share_wxcircle:
                    scene = SendMessageToWX.Req.WXSceneTimeline;
                    break;
            }
            WXMediaMessage wxMediaMessage = new WXMediaMessage();
            if (type == 1){
                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.ic_launcher);
                WXWebpageObject wxWebpageObject = new WXWebpageObject();
                wxWebpageObject.webpageUrl = url;

                wxMediaMessage.title = title;
                wxMediaMessage.description = content;
                wxMediaMessage.thumbData = Util.bmpToByteArray(bitmap,true);
                wxMediaMessage.mediaObject = wxWebpageObject;
            }else if (type == 2){
                WXImageObject wxImageObject = new WXImageObject(bitmap);
                wxMediaMessage.mediaObject = wxImageObject;
            }
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = wxMediaMessage;
            req.scene = scene;
            MyApplication.msgApi.sendReq(req);
            Intent intent = new Intent();
            MyApplication.msgApi.handleIntent(intent, new IWXAPIEventHandler() {
                @Override
                public void onReq(BaseReq baseReq) {
                    LogUtil.i(baseReq.transaction);
                }

                @Override
                public void onResp(BaseResp baseResp) {
                    LogUtil.i(baseResp.transaction);
                }
            });
        }
    };

    @Override
    public void onClick(View v) {


    }

}
