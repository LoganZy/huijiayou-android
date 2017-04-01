package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.fragment.HomeFragment;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoPayActivity extends BaseActivity implements NewHttpRequest.RequestCallback {
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
    @Bind(R.id.tv_activityPay_username)
    TextView tvActivityPayUserName;
    @Bind(R.id.tv_activityPay_name)
    TextView tvActivityPayName;
    private String id;
    private Handler handler = new Handler(){};
    private int time1;
    private int time2;
    private Bundle b;
    private Boolean isOutTime=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_pay);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("交易详情");

        initData();
        initView();
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        if(isOutTime) {
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.ic_details_gas_n);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tvActivityNopaytime.setCompoundDrawables(null, rightDrawable, null, null);
            btActivityPayPay.setEnabled(false);
        }
        startTime();
    }

    private void initData() {
        Intent intent = getIntent();
        b = intent.getExtras();
        id = b.getString("id");
        String card_number = b.getString("card_number");
        String discount_before_amount = b.getString("discount_before_amount");
        String discount_after_amount = b.getString("discount_after_amount");
        String order_name = b.getString("order_number");
        String ctime = b.getString("ctime");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String current =  format.format(System.currentTimeMillis());
        String[] arr1= ctime.split("\\:");
        String[] arr =current.split("\\:");
        if(TextUtils.equals(arr[0],arr1[0])){
           int i =  Integer.parseInt(arr[1])- Integer.parseInt(arr1[1]);
            if(i<15){
               int total = 900+ Integer.parseInt(arr1[1])*60+Integer.parseInt(arr1[2])-Integer.parseInt(arr[1])*60-Integer.parseInt(arr[2]);
                if (total>0){
                    time1 =total%60;
                    time2 = total/60;
                }else {

                    tvActivityNopaytime.setText("支付超时");
                }

            }else{
                tvActivityNopaytime.setText("支付超时");
            }
        }else{
            tvActivityNopaytime.setText("支付超时");

        }
        LogUtil.i(arr[0]);
        String belong = b.getString("belong");
        String user_name = b.getString("user_name");
        String count = b.getString("count");
        String total_time = b.getString("total_time");
        String product_name = b.getString("product_name");
        //1中石化2中石油

        if (TextUtils.equals(belong, "2")) {
            imgActivityPayIoc.setBackgroundResource(R.mipmap.ic_details_cnpc);
        } else {
            imgActivityPayIoc.setBackgroundResource(R.mipmap.ic_details_sinopec);

        }
        tvActivityPayUserName.setText(user_name);
        tvActivityPayCardNum.setText(card_number);
        tvActivityPayDiscountAfterAmount.setText(discount_after_amount);
        tvActivityPayDiscountBeforeAmount.setText(discount_before_amount);
        tvActivityPayOrderNum.setText(order_name);
        tvActivityPayTime.setText(ctime);
        tvActivityPayName.setText(product_name);
    }

    @OnClick(R.id.bt_activityPay_pay)
    public void onClick() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("order_id", id);
        map.put("sign", "");
        new NewHttpRequest(this, Constans.URL_zxg + Constans.ORDER, Constans.getOrderInfo, Constans.JSONOBJECT, 2, map, true, this).executeTask();

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
                time1 -= 1;
                if (time1 <= 0) {
                    time2-=1;
                    time1=60;
                    handler.postDelayed(this, 1000);
                    if (time2<0){
                        handler.removeCallbacksAndMessages(null);
                        tvActivityNopaytime.setBackgroundResource(R.color.gray);
                        tvActivityNopaytime.setText("交易超时关闭");
                        Drawable rightDrawable = getResources().getDrawable(R.mipmap.ic_details_gas_n);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                        tvActivityNopaytime.setCompoundDrawables(null, rightDrawable, null, null);
                        btActivityPayPay.setEnabled(false);

                        isOutTime = true;
                        /*tvActivityNopaytime.setText("支付超时");
                        Intent intent = new Intent();
                        intent.putExtras(b);
                        intent.setClass(NoPayActivity.this,CloseDealActivity.class);
                        startActivity(intent);
                        finish();*/
                    }

                } else {
                    tvActivityNopaytime.setText("未支付 （"+time2+":"+time1+"s）" );
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        switch (taskId){
            case 2:
                try {
                    String ordernum =  jsonObject.getString("order_number");
                    int moneyMoth = (int)Double.parseDouble(jsonObject.getString( "unit_price"));
                    String productId = jsonObject.getString("product_id");
                    String month = jsonObject.getString("total_time");
                    double total =Double.parseDouble(jsonObject.getString("discount_before_amount")) ;
                    double saveMoney = Double.parseDouble(jsonObject.getString("discount_money"));
                    double discountTotal = Double.parseDouble(jsonObject.getString("discount_after_amount"));
                    Intent intent = new Intent();
                    intent.putExtra("type",PaymentActivity.type_pay);
                    intent.putExtra("orderNumber",ordernum);
                    intent.putExtra("moneyMonth",moneyMoth);
                    intent.putExtra("product_id",productId);
                    intent.putExtra("month",month);
                    intent.putExtra("total",total);
                    intent.putExtra("discountTotal",discountTotal);
                    intent.putExtra("saveMoney",saveMoney);
                    intent.setClass(this,PaymentActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {

    }
}
