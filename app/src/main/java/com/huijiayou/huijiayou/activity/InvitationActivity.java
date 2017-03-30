package com.huijiayou.huijiayou.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.jsbridgewebview.BridgeHandler;
import com.huijiayou.huijiayou.jsbridgewebview.BridgeWebView;
import com.huijiayou.huijiayou.jsbridgewebview.CallBackFunction;
import com.huijiayou.huijiayou.jsbridgewebview.DefaultHandler;
import com.huijiayou.huijiayou.jsbridgewebview.WebViewClientCallback;
import com.huijiayou.huijiayou.net.DeviceUtils;
import com.huijiayou.huijiayou.utils.PreferencesUtil;

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
        tvTitle.setText("邀请车友");
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        String session_id = PreferencesUtil.getPreferences("session_id","");
        String url = "http://192.168.10.212:8888/?user_id=" + userId + "&" +
                session_id + "#/friend_invi";
        bridgeWebView.setBackgroundColor(0);
        bridgeWebView.setWebViewClientCallback(CallBack);
        bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        bridgeWebView.getSettings().setAllowFileAccess(true);
        bridgeWebView.getSettings().setAppCacheEnabled(true);
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.getSettings().setDatabaseEnabled(true);
        String userAgent = bridgeWebView.getSettings().getUserAgentString();
        bridgeWebView.getSettings().setUserAgentString(userAgent + "wlbAPP/" + DeviceUtils.getVersion(this));
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.setWebChromeClient(new WebChromeClient());
        bridgeWebView.registerHandler("invitation", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
        bridgeWebView.loadUrl(url);


    }

    private WebViewClientCallback CallBack = new WebViewClientCallback() {
        @Override
        public void pageFinishedCallBack(WebView view, String url) {

        }

        @Override
        public void pageStartedCallBack(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void receivedSslErrorCallBack(WebView view, SslErrorHandler handler, SslError error) {

        }

        @Override
        public void receivedErrorCallBack(WebView view, int errorCode, String description, String failingUrl) {

        }

        @Override
        public void shouldOverrideUrlLoadingCallBack(WebView view, String url) {

        }
    };
}
