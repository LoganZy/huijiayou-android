package com.huijiayou.huijiayou.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.huijiayou.huijiayou.R;

/**
 * Created by lugg on 2016/8/12.
 */
public class ShareUtil implements View.OnClickListener {

    private Activity activity;
    private String title;
    private String text;
    private String url;
    private String imageUrl;
    private UMShareListener mUmShareListener;
    Dialog dialog;

    public ShareUtil(Activity activity, String title, String text, String url, String imageUrl, UMShareListener mUmShareListener) {
        this.activity = activity;
        this.title = title;
        this.text = text;
        this.url = url;
        this.imageUrl = imageUrl;
        this.mUmShareListener = mUmShareListener;
    }

    public void showShareDialog(){
        dialog = new Dialog(activity, R.style.dialog_bgTransparent);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().setLayout(activity.getWindow().getWindowManager().getDefaultDisplay().getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(R.layout.dialog_share);
        TextView tv_dialog_share_wechat = (TextView) dialog.findViewById(R.id.tv_dialog_share_wechat);
        TextView tv_dialog_share_wxcircle = (TextView) dialog.findViewById(R.id.tv_dialog_share_wxcircle);
        TextView tv_dialog_share_qq = (TextView) dialog.findViewById(R.id.tv_dialog_share_qq);
        Button btn_dialog_share_cancel = (Button) dialog.findViewById(R.id.btn_dialog_share_cancel);

        tv_dialog_share_qq.setOnClickListener(this);
        tv_dialog_share_wxcircle.setOnClickListener(this);
        tv_dialog_share_wechat.setOnClickListener(this);
        btn_dialog_share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        SHARE_MEDIA share_media = null;
        switch (v.getId()){
            case R.id.tv_dialog_share_wechat:
                share_media = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.tv_dialog_share_wxcircle:
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.tv_dialog_share_qq:
                share_media = SHARE_MEDIA.QQ;
                break;
        }

        ShareAction shareAction = new ShareAction(activity);
        shareAction.setPlatform(share_media);
        shareAction.setCallback(mUmShareListener);//设置每个平台的点击事件
        if (TextUtils.isEmpty(title)){
            shareAction.withText("会加油");
        }else{
            shareAction.withText(title);
        }
        if (TextUtils.isEmpty(text)){
            shareAction.withText("更多精彩福利等你来");
        }else{
            shareAction.withText(text);
        }
        UMWeb umWeb = null;
        if (TextUtils.isEmpty(url)){
            umWeb = new UMWeb("");//点击分享内容打开的链接 TODO
        }else{
            umWeb = new UMWeb(url);//点击分享内容打开的链接
        }
        UMImage umImage = null;
        if (TextUtils.isEmpty(imageUrl)){
            umImage = new UMImage(activity, R.mipmap.ic_login_logo);
        }else{
            umImage = new UMImage(activity, imageUrl);
        }
        shareAction.withMedia(umImage);//附带的图片，音乐，视频等多媒体对象
        shareAction.share();//发起分享，调起微信，QQ，微博客户端进行分享。
    }
}
