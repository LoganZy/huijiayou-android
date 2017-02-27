package com.wanglibao.huijiayou.activity;

import android.os.Bundle;

import com.wanglibao.huijiayou.R;


public class WXBindActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxbind);
        initTitle();
        tvTitle.setText("账号绑定");
    }
}
