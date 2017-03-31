package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.threadpool.ThreadPool;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1){
                Map<String,String> map = (Map<String, String>) msg.obj;
                String memo = map.get("memo");
                String result = map.get("result");
                String resultStatus = map.get("resultStatus");
                if ("9000".equals(resultStatus)){
                    ToastUtils.createNormalToast(NoPayActivity.this, "支付成功");
                }else{
                    ToastUtils.createNormalToast(NoPayActivity.this, memo);
                }
                back(memo, result, resultStatus);
            }
            return false;

        }
    });
    private int time1=60;
    private int time2;
    private String order_name;
    private Bundle b;


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

        startTime();
    }

    private void initData() {
        Intent intent = getIntent();
        b = intent.getExtras();
        id = b.getString("id");
        String card_number = b.getString("card_number");
        String discount_before_amount = b.getString("discount_before_amount");
        String discount_after_amount = b.getString("discount_after_amount");
        order_name = b.getString("order_number");
        String ctime = b.getString("ctime");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String current =  format.format(System.currentTimeMillis());
        String[] arr1= ctime.split("\\:");
        String[] arr =current.split("\\:");
        if(TextUtils.equals(arr[0],arr1[0])){
           int i =  Integer.parseInt(arr[1])- Integer.parseInt(arr1[1]);
            if(i<15){
               int total = 840+ Integer.parseInt(arr1[1])*60+Integer.parseInt(arr1[2])-Integer.parseInt(arr[1])*60+Integer.parseInt(arr[2]);
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
        checkOrder();
    }



    private void checkOrder(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("order_number",order_name);
        hashMap.put("pay_channel","ali_pay");
        new NewHttpRequest(this, Constans.URL_zxg + Constans.PAY, Constans.checkOrder, "jsonObject", 1,
                hashMap, true, this).executeTask();
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
                        tvActivityNopaytime.setText("支付超时");
                    }

                } else {
                    tvActivityNopaytime.setText("未支付 （"+time2+":"+time1+"s）" );
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }

    /*
    *
    *
    * */

    private void back(String memo, String result, String resultStatus){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("memo",memo);
        hashMap.put("result",result);
        hashMap.put("resultStatus",resultStatus);
        new NewHttpRequest(this, Constans.URL_zxg+Constans.PAY, Constans.back, "jsonObject", 2,
                hashMap, true, this).executeTask();
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        switch (taskId){
            case 1:
                try {
                    final String orderInfo = (String) jsonObject.getJSONObject("data").get("response");
                    ThreadPool.getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                            PayTask payTask = new PayTask(NoPayActivity.this);
                            Map<String, String> result = payTask.payV2(orderInfo,true);
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case 2:
                Intent intent = new Intent();
                intent.putExtras(b);
                intent.setClass(this,DetailsActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {

    }
}
