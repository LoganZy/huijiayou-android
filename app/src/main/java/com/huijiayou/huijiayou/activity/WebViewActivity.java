package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.widget.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.proWebView)
    ProgressWebView proWebView;

    @Bind(R.id.btn_activityWebView)
    Button btn_activityWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("帮助");

        Intent intent = getIntent();
        intent.getStringExtra("");

    }
}
