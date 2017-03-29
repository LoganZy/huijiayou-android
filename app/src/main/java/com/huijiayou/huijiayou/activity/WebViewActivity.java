package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.widget.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.proWebView)
    ProgressWebView proWebView;

    String title,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initTitle();

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        tvTitle.setText(title);
        proWebView.loadUrl(url);
    }
}
