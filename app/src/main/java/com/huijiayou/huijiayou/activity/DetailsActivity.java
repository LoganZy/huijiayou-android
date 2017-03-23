package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.DetailAdapter;
import com.huijiayou.huijiayou.bean.OrderDetail;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.widget.SVListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends BaseActivity implements NewHttpRequest.RequestCallback {

    @Bind(R.id.img_activityDetail_card)
    ImageView imgActivityDetailCard;
    @Bind(R.id.tv_activityDetail_cardNum)
    TextView tvActivityDetailCardNum;
    @Bind(R.id.tv_activityDetail_productName)
    TextView tvActivityDetailProductName;
    @Bind(R.id.tv_activityDetail_process)
    TextView tvActivityDetailProcess;
    @Bind(R.id.tv_activityDetail_befor)
    TextView tvActivityDetailBefor;
    @Bind(R.id.tv_activityDetail_after)
    TextView tvActivityDetailAfter;
    @Bind(R.id.tv_activityDetail_orderNum)
    TextView tvActivityDetailOrderNum;
    @Bind(R.id.tv_activityDetail_ictime)
    TextView tvActivityDetailIctime;
    @Bind(R.id.tv_activityDetail_recharge_time)
    TextView tvActivityDetailRechargeTime;
    @Bind(R.id.myLv_activityDetail)
    SVListView myLvActivityDetail;
    private List<OrderDetail> list;
    private String rechargetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("交易详情");
        initView();
        initData();
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
        String count = b.getString("count");
        String total_time = b.getString("total_time");
        String product_name = b.getString("product_name");
        if (TextUtils.equals(belong, "2")) {
            imgActivityDetailCard.setBackgroundResource(R.mipmap.ic_details_cnpc);
        } else {
            imgActivityDetailCard.setBackgroundResource(R.mipmap.ic_details_sinopec);
        }
        tvActivityDetailCardNum.setText(card_number);
        tvActivityDetailAfter.setText(discount_after_amount);
        tvActivityDetailBefor.setText(discount_before_amount);
        tvActivityDetailOrderNum.setText(order_name);
        tvActivityDetailIctime.setText(ctime);
        tvActivityDetailProcess.setText(count + " / " + total_time);
        tvActivityDetailProductName.setText(product_name);

        HashMap<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("order_id", id);
        map.put("sign", "");
       // new NewHttpRequest(this, Constans.URL_zxg + Constans.ORDER, Constans.getOrderInfo, Constans.JSONOARRAY, 1, map, true, this).executeTask();



    }

    private void initView() {
        list = new ArrayList<>();
        for(int i = 0;i<4;i++){

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId("1");
            orderDetail.setTotal_time("10");
            orderDetail.setRecharge_amount("");
            orderDetail.setRecharge_time("20170623");
            orderDetail.setStatus(""+i);
            list.add(orderDetail);

        }
        DetailAdapter detailAdapter = new DetailAdapter(this,list);
        myLvActivityDetail.setAdapter(detailAdapter);
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        list = new ArrayList<OrderDetail>();
        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("oil_info");
            rechargetime = jsonArray1.getJSONObject(1).getString("recharge_amount");
            tvActivityDetailRechargeTime.setText(rechargetime);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                String id = jsonObject1.getString("id");
                String total_time = jsonObject1.getString("total_time");
                String recharge_amount = jsonObject1.getString("recharge_amount");
                String recharge_time = jsonObject1.getString("recharge_time");
                String status = jsonObject1.getString("status");
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(id);
                orderDetail.setRecharge_amount(recharge_amount);
                orderDetail.setRecharge_time(recharge_time);
                orderDetail.setStatus(status);
                list.add(orderDetail);
                /*
                *
                * "id": "87",
                    "task_status": "0",
                    "total_time": "1",
                 "recharge_amount": "200.00",
                "recharge_time": "2017-03-13 11:18:11",
                 "order_id": "63",
                "status": null,
                "status_name": "待充值"
                * */
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {

    }
}
