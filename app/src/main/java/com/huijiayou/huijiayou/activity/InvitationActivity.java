package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.DeviceUtils;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.widget.jsbridgewebview.BridgeWebView;
import com.huijiayou.huijiayou.widget.jsbridgewebview.CallBackFunction;
import com.huijiayou.huijiayou.widget.jsbridgewebview.DefaultHandler;
import com.huijiayou.huijiayou.wxapi.ShareUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 好友邀请页
 */
public class InvitationActivity extends BaseActivity {

    @Bind(R.id.bridgeWebView)
    BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("好友邀请");

        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        String session_id = PreferencesUtil.getPreferences("session_id","");
//        String url = "http://192.168.10.212:8888/?user_id="+userId+"&"+session_id+"#/friend_invi";
        String url = "http://192.168.10.212:8888/#/friend_invi";
        String userAgent = bridgeWebView.getSettings().getUserAgentString();
        bridgeWebView.getSettings().setUserAgentString(userAgent + DeviceUtils.getHeadInfo(this));
        bridgeWebView.getSettings().setSaveFormData(false);
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        bridgeWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.loadUrl(url);
        bridgeWebView.callHandler("getUserInfos", "{user_id="+userId+","+session_id+"}", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                LogUtil.i(data);
            }});
    }

    public void shareInvitation(View view){
//        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
//        String session_id = PreferencesUtil.getPreferences("session_id","");
//        bridgeWebView.callHandler("getUserInfos", "{user_id="+userId+","+session_id+"}", new CallBackFunction() {
//            @Override
//            public void onCallBack(String data) {
//                LogUtil.i(data);
//            }});

        String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
        String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
        String url = "http://192.168.10.212:8888/?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
        new ShareUtil().shareWebPage(this, "", "", url);
    }

}
