package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoPayActivity extends BaseActivity {

    @Bind(R.id.img_activityPay_ioc)
    ImageView imgActivityPayIoc;
    @Bind(R.id.tv_activityPay_cardNum)
    TextView tvActivityPayCardNum;
    @Bind(R.id.tv_activityPay_discount_before_amount)
    TextView tvActivityPayDiscountBeforeAmount;
    @Bind(R.id.tv_activityPay_discount_after_amount)
    TextView tvActivityPayDiscountAfterAmount;
    @Bind(R.id.tv_activityPay_orderNum)
    TextView tvActivityPayOrderNum;
    @Bind(R.id.tv_activityPay_time)
    TextView tvActivityPayTime;
    @Bind(R.id.bt_activityPay_pay)
    Button btActivityPayPay;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_pay);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("交易详情");
        initData();
        initView();
    }

    private void initView() {

    }

    private void initData() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        id = b.getString("id");
        String card_number = b.getString("card_number");
        String discount_before_amount = b.getString("discount_before_amount");
        String discount_after_amount = b.getString("discount_after_amount");
        String order_name = b.getString("order_number");
        String ctime = b.getString("ctime");
        String belong = b.getString("belong");

        //1中石化2中石油

        if (TextUtils.equals(belong, "2")) {
            imgActivityPayIoc.setBackgroundResource(R.mipmap.ic_details_cnpc);
        } else {
            imgActivityPayIoc.setBackgroundResource(R.mipmap.ic_details_sinopec);

        }

        tvActivityPayCardNum.setText(card_number);
        tvActivityPayDiscountAfterAmount.setText(discount_after_amount);
        tvActivityPayDiscountBeforeAmount.setText(discount_before_amount);
        tvActivityPayOrderNum.setText(order_name);
        tvActivityPayTime.setText(ctime);

    }

    @OnClick(R.id.bt_activityPay_pay)
    public void onClick() {
    }
}
