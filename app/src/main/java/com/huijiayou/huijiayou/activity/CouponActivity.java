package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CouponActivity extends BaseActivity {

    @Bind(R.id.btn_activityCoupon_noUseCoupon)
    Button btn_activityCoupon_noUseCoupon;

    @Bind(R.id.tv_activityCoupon_noUseTag)
    TextView tv_activityCoupon_noUseTag;

    @Bind(R.id.recyclerView_activityCoupon_noUse)
    RecyclerView recyclerView_activityCoupon_noUse;

    @Bind(R.id.tv_activityCoupon_useTag)
    TextView tv_activityCoupon_useTag;

    @Bind(R.id.recyclerView_activityCoupon_use)
    RecyclerView recyclerView_activityCoupon_use;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("优惠券");


    }
}
