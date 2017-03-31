package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.fragment.HomeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloseDealActivity extends BaseActivity {

    @Bind(R.id.img_activityClose_card)
    ImageView imgActivityCloseCard;
    @Bind(R.id.tv_activityClose_cardNum)
    TextView tvActivityCloseCardNum;
    @Bind(R.id.tv_activityClose_befor)
    TextView tvActivityCloseBefor;
    @Bind(R.id.tv_activityClose_after)
    TextView tvActivityCloseAfter;
    @Bind(R.id.tv_activityClose_ordername)
    TextView tvActivityCloseOrdername;
    @Bind(R.id.tv_activityClose_time)
    TextView tvActivityCloseTime;
    @Bind(R.id.bt_activityClose_pay)
    Button btActivityClosePay;
    @Bind(R.id.tv_activityClose_username)
    TextView tvActivityCloseUsername;
    @Bind(R.id.tv_activityClose_productname)
    TextView tvActivityCloseProductname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_deal);
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
        String id = b.getString("id");
        String card_number = b.getString("card_number");
        String discount_before_amount = b.getString("discount_before_amount");
        String discount_after_amount = b.getString("discount_after_amount");
        String order_name = b.getString("order_number");
        String ctime = b.getString("ctime");
        String belong = b.getString("belong");
        String user_name = b.getString("user_name");
        String product_name = b.getString("product_name");
        //1中石化2中石油

        if (TextUtils.equals(belong, "2")) {
            imgActivityCloseCard.setBackgroundResource(R.mipmap.ic_details_cnpc);
        } else {
            imgActivityCloseCard.setBackgroundResource(R.mipmap.ic_details_sinopec);

        }
        tvActivityCloseProductname.setText(product_name);
        tvActivityCloseUsername.setText(user_name);
        tvActivityCloseCardNum.setText(card_number);
        tvActivityCloseAfter.setText(discount_after_amount);
        tvActivityCloseBefor.setText(discount_before_amount);
        tvActivityCloseOrdername.setText(order_name);
        tvActivityCloseTime.setText(ctime);


    }

    @OnClick(R.id.bt_activityClose_pay)
    public void onClick() {
        Intent intent = new Intent();
        intent.putExtra("type", HomeFragment.TAG);
        startActivity(intent);
        finish();
    }


}
