package com.huijiayou.huijiayou.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.utils.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

/**
 * Created by lugg on 2016/8/12.
 */
public class ShareUtil implements View.OnClickListener {

     Activity activity;
     String title;
     String content;
     String url;

    public  void shareWebPage(Activity ac, String t, String c, String u){
        activity = ac;
        title = t;
        content = c;
        url = u;
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
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
    };

    @Override
    public void onClick(View v) {


    }

}
