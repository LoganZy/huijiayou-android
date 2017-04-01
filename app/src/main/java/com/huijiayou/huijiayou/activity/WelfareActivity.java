package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.jsbridgewebview.BridgeHandler;
import com.huijiayou.huijiayou.jsbridgewebview.BridgeWebView;
import com.huijiayou.huijiayou.jsbridgewebview.CallBackFunction;
import com.huijiayou.huijiayou.jsbridgewebview.DefaultHandler;
import com.huijiayou.huijiayou.net.DeviceUtils;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.wxapi.ShareUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelfareActivity extends BaseActivity {

    @Bind(R.id.bwb_activityWelfare_view)
    BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("邀请好友");

        bridgeWebView.setBackgroundColor(0);
        bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        bridgeWebView.getSettings().setAllowFileAccess(true);
        bridgeWebView.getSettings().setAppCacheEnabled(true);
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.getSettings().setDatabaseEnabled(true);
        String userAgent = bridgeWebView.getSettings().getUserAgentString();
        bridgeWebView.getSettings().setUserAgentString(userAgent + "wlbAPP/" + DeviceUtils.getVersion(this));
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.setWebChromeClient(new WebChromeClient());

        bridgeWebView.loadUrl(NetConfig.getwelfare);

        bridgeWebView.registerHandler("invitation", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
                String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
                String url = "http://192.168.10.212:8888/?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
                new ShareUtil().shareWebPage(WelfareActivity.this, "", "", url);
            }
        });
    }

//    public void shareWelfare(View view){
//        String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
//        String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
//        String url = "http://192.168.10.212:8888/?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
//        new ShareUtil().shareWebPage(this, "", "", url);
//    }

}
