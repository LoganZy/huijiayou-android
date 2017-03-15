package com.huijiayou.huijiayou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huijiayou.huijiayou.R;

public class DetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initTitle();
        tvTitle.setText("交易详情");
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {

    }
}
