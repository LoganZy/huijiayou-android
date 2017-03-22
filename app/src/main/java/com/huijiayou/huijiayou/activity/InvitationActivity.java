package com.huijiayou.huijiayou.activity;

import android.os.Bundle;

import com.huijiayou.huijiayou.R;

/**
 * 好友邀请页
 */
public class InvitationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        initTitle();
        tvTitle.setText("邀请车友");

    }
}
