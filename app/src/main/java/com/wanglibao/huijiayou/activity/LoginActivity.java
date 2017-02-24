package com.wanglibao.huijiayou.activity;

import android.os.Bundle;

import com.wanglibao.huijiayou.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initTitle();
        tvTitle.setText("登录");

    }
}
