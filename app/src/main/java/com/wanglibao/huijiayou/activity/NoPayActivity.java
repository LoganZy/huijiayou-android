package com.wanglibao.huijiayou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wanglibao.huijiayou.R;

public class NoPayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_pay);
        initTitle();
        tvTitle.setText("交易详情");
    }
}
