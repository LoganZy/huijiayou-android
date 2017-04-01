package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.widget.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpActivity extends BaseActivity {

    @Bind(R.id.proWebView)
    ProgressWebView proWebView;

    @Bind(R.id.btn_activityHelp_feedback)
    Button btn_activityHelp_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("帮助");

        proWebView.loadUrl(NetConfig.help);
        btn_activityHelp_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this,FeedbackActivity.class));
            }
        });
    }
}
