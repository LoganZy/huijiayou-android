package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.view.View;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.widget.ProgressWebView;
import com.huijiayou.huijiayou.wxapi.ShareUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelfareActivity extends BaseActivity {

    @Bind(R.id.proWebView_activityWelfare_view)
    ProgressWebView proWebView_activityWelfare_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("领取福利");

        proWebView_activityWelfare_view.loadUrl(NetConfig.getwelfare);
    }

    public void shareWelfare(View view){
        String mobile = PreferencesUtil.getPreferences(Constans.USER_PHONE,"");
        String invite_code = PreferencesUtil.getPreferences(Constans.USER_INVITE_CODE,"");
        String url = "http://192.168.10.212:8888/?mobile="+mobile+"&invite_code="+invite_code+"#/game/main";
        new ShareUtil().shareWebPage(this, "", "", url);
    }
}
