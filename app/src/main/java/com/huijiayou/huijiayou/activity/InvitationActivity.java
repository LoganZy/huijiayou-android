package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.widget.jsbridgewebview.BridgeHandler;
import com.huijiayou.huijiayou.widget.jsbridgewebview.BridgeWebView;
import com.huijiayou.huijiayou.widget.jsbridgewebview.CallBackFunction;
import com.huijiayou.huijiayou.widget.jsbridgewebview.DefaultHandler;
import com.huijiayou.huijiayou.wxapi.ShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

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

        final String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        final String session_id = PreferencesUtil.getPreferences("session_id","");
        String url = NetConfig.H5_URL + "?user_id="+userId+"&"+session_id+"#/friend_invi";
//        String url = NetConfig.URL + "/wechat/#/friend_invi";
//        String userAgent = bridgeWebView.getSettings().getUserAgentString();
//        bridgeWebView.getSettings().setUserAgentString(userAgent + DeviceUtils.getHeadInfo(this));
        bridgeWebView.getSettings().setSaveFormData(false);
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.getSettings().setDomStorageEnabled(true);
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        bridgeWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        bridgeWebView.getSettings().setDomStorageEnabled(true);

        bridgeWebView.loadUrl(url);
        bridgeWebView.registerHandler("getUserInfos", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", userId);
                    String token = session_id.substring(session_id.indexOf("OIL_TOKEN")+10);
                    jsonObject.put("OIL_TOKEN", token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(jsonObject.toString());
            }
        });

    }

    public void shareInvitation(View view){
        String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
        String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
        String url = NetConfig.H5_URL + "?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
        new ShareUtil().shareWebPage(this, "", "", url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

      /*  if (mDialog != null) {
            mDialog.dismiss();
        }*/
    }
}
