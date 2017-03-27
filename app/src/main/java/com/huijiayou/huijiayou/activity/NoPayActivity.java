package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoPayActivity extends BaseActivity {
    @Bind(R.id.tv_activitynoPay_time)
    TextView tvActivityNopaytime;
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
    private Handler handler = new Handler() {
    };
    private int time=60;
    private int time2=14;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_pay);
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

        startTime();
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

    /*
         *
         * 发送定时消息的方法
         *
         * */
    public void startTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time -= 1;
                if (time < 0) {
                    time2-=1;
                    time=60;
                    handler.postDelayed(this, 1000);
                    if (time2<=0){
                        handler.removeCallbacksAndMessages(null);
                        tvActivityNopaytime.setText("支付超时");
                    }

                } else {
                    tvActivityNopaytime.setText("未支付 （"+time2+":"+time+"s）" );
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }

}
