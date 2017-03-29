package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayingActivity extends BaseActivity {


    @Bind(R.id.img_activityPaying_ioc)
    ImageView imgActivityPayingIoc;
    @Bind(R.id.tv_activityPaying_cardNum)
    TextView tvActivityPayingCardNum;
    @Bind(R.id.tv_activityPaying_username)
    TextView tvActivityPayingUsername;
    @Bind(R.id.tv_activityPaying_discount_before_amount)
    TextView tvActivityPayingDiscountBeforeAmount;
    @Bind(R.id.tv_activityPaying_discount_after_amount)
    TextView tvActivityPayingDiscountAfterAmount;
    @Bind(R.id.tv_activityPaying_orderNum)
    TextView tvActivityPayingOrderNum;
    @Bind(R.id.tv_activityPaying_time)
    TextView tvActivityPayingTime;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("交易详情");
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        initView();
    }

    private void initView() {

    }

    private void initData() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        // id = b.getString("id");
        String card_number = b.getString("card_number");
        String discount_before_amount = b.getString("discount_before_amount");
        String discount_after_amount = b.getString("discount_after_amount");
        String order_name = b.getString("order_number");
        String ctime = b.getString("ctime");
        String belong = b.getString("belong");

        //1中石化2中石油

        if (TextUtils.equals(belong, "2")) {
            imgActivityPayingIoc.setBackgroundResource(R.mipmap.ic_details_cnpc);
        } else {
            imgActivityPayingIoc.setBackgroundResource(R.mipmap.ic_details_sinopec);

        }

        tvActivityPayingCardNum.setText(card_number);
        tvActivityPayingDiscountAfterAmount.setText(discount_after_amount);
        tvActivityPayingDiscountBeforeAmount.setText(discount_before_amount);
        tvActivityPayingOrderNum.setText(order_name);
        tvActivityPayingTime.setText(ctime);

    }

}
