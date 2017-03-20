package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.OilCardAdapter;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.widget.PaymentActivityOilCarDialog;
import com.huijiayou.huijiayou.widget.RechargeDetailsDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaymentActivity extends BaseActivity implements View.OnClickListener,NewHttpRequest.RequestCallback {

    @Bind(R.id.iv_activityPayment_suc)
    ImageView iv_activityPayment_suc;  //支付成功后 显示

    @Bind(R.id.tv_activityPayment_price)
    TextView tv_activityPayment_price; //单月价格

    @Bind(R.id.tv_activityPayment_size)
    TextView tv_activityPayment_size;  //选择的月数

    @Bind(R.id.tv_activityPayment_discountMoney)
    TextView tv_activityPayment_discountMoney; //折后金额  “折后金额:2900元”

    @Bind(R.id.tv_activityPayment_saveMoney)
    TextView tv_activityPayment_saveMoney;  //节省金额  “节省:300元”

    @Bind(R.id.tv_activityPayment_paymentDetail)
    TextView tv_activityPayment_paymentDetail; //点击 弹出充值明细Dialog
    //-----------end-----------支付页 准备支付时 上面部分

    @Bind(R.id.rl_activityPayment_inputCard)
    RelativeLayout rl_activityPayment_inputCard;  //支付第一步  输入加油卡号

    @Bind(R.id.edit_activityPayment_card)
    EditText edit_activityPayment_card;  //接收加油卡号的输入，在已有加油卡时 不可编辑 点击弹出加油卡列表

    @Bind(R.id.imgBtn_activityPayment_next)
    ImageButton imgBtn_activityPayment_next; //点击进入下一步

    @Bind(R.id.tv_activityPayment_cardTag)
    TextView tv_activityPayment_cardTag;  //输入加油卡号时 判断输入是否正确
    //---------end----------支付页  准备支付   第一步  输入加油卡号

    @Bind(R.id.rl_activityPayment_coupon)
    RelativeLayout rl_activityPayment_coupon; //支付第二步  显示油卡的信息和选择优惠券

    @Bind(R.id.tv_activityPayment_coupon_card)
    TextView tv_activityPayment_coupon_card;  //显示加油卡号 和 油卡类型的图标 drawableRight

    @Bind(R.id.tv_activityPayment_coupon_userName)
    TextView tv_activityPayment_coupon_userName;  //油卡持有人姓名

    @Bind(R.id.tv_activityPayment_coupon_coupon)
    TextView tv_activityPayment_coupon_coupon;  //优惠券  点击弹出优惠券列表

    @Bind(R.id.rb_activityPayment_coupon_option)
    CheckBox rb_activityPayment_coupon_option;  //选择项  是否同意协议，默认选中

    @Bind(R.id.tv_activityPayment_coupon_agreement)
    TextView tv_activityPayment_coupon_agreement;  //充值协议  点击查看协议h5页

    @Bind(R.id.btn_activityPayment_coupon_payment)
    Button btn_activityPayment_coupon_payment;  //下一步按钮
    //--------end-----------------------第二步  显示油卡信息

    @Bind(R.id.rl_activityPayment_payment)
    RelativeLayout rl_activityPayment_payment;  //第三部  选择支付方式

    @Bind(R.id.rg_activityPayment_option)
    RadioGroup rg_activityPayment_option;

    @Bind(R.id.rb_activityPayment_wechat)
    RadioButton rb_activityPayment_wechat; //微信支付

    @Bind(R.id.rb_activityPayment_alipay)
    RadioButton rb_activityPayment_alipay; //支付宝支付

    @Bind(R.id.rb_activityPayment_payment_option)
    CheckBox rb_activityPayment_payment_option;  //选择项  是否同意协议，默认选中

    @Bind(R.id.tv_activityPayment_payment_agreement)
    TextView tv_activityPayment_payment_agreement;  //充值协议  点击查看协议h5页

    @Bind(R.id.btn_activityPayment_payment_payment)
    Button btn_activityPayment_payment_payment;  //下一步按钮
    //-----------end------------第三部  选择支付方式

    @Bind(R.id.rl_activityPayment_success)
    RelativeLayout rl_activityPayment_success; //第四步  支付完成

    PaymentActivityOilCarDialog paymentActivityOilCarDialog;

    int moneyMonth,product_id,month;
    double total,discountTotal,saveMoney;
    public String oilCard;

    int getOilCardListTaskId = 1;
    int getOilCardInfoTaskId = 2;
    int bindCardTaskId = 3;

    public int addOilCarRequestCode = 100;

    private ArrayList<OilCardAdapter.OilCardEntity> oilCardEntityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("确认订单");
        init();
    }

    private void init(){
        Intent intent = getIntent();
        moneyMonth = intent.getIntExtra("moneyMonth",0);
        product_id = Integer.parseInt(intent.getStringExtra("product_id"));
        month = Integer.parseInt(intent.getStringExtra("month"));
        total = intent.getDoubleExtra("total",0);
        discountTotal = intent.getDoubleExtra("discountTotal",0);
        saveMoney = intent.getDoubleExtra("saveMoney",0);
        tv_activityPayment_price.setText(moneyMonth+"");
        tv_activityPayment_size.setText(month+"");
        tv_activityPayment_discountMoney.setText("折后金额:"+discountTotal+"元");
        tv_activityPayment_saveMoney.setText("节省:"+saveMoney+"元");
        getOilCardList();


        imgBtn_activityPayment_next.setOnClickListener(this);
        btn_activityPayment_coupon_payment.setOnClickListener(this);
        btn_activityPayment_payment_payment.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (rl_activityPayment_inputCard.isShown() || rl_activityPayment_success.isShown()){
            super.onBackPressed();
        }else if (rl_activityPayment_coupon.isShown()){
            rl_activityPayment_coupon.setVisibility(View.GONE);
            rl_activityPayment_inputCard.setVisibility(View.VISIBLE);
        }else if (rl_activityPayment_payment.isShown()){
            rl_activityPayment_payment.setVisibility(View.GONE);
            rl_activityPayment_coupon.setVisibility(View.VISIBLE);
        }

    }

    public void rechargeDetailsDialog(View view){
        new RechargeDetailsDialog(this,moneyMonth,month).create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtn_activityPayment_next:
                rl_activityPayment_inputCard.setVisibility(View.GONE);
                rl_activityPayment_coupon.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_activityPayment_coupon_payment:
                rl_activityPayment_coupon.setVisibility(View.GONE);
                rl_activityPayment_payment.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_activityPayment_payment_payment:
                rl_activityPayment_payment.setVisibility(View.GONE);
                rl_activityPayment_success.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_activityPayment_card:
                if (paymentActivityOilCarDialog == null){
                    paymentActivityOilCarDialog = new PaymentActivityOilCarDialog(this,oilCardEntityList);
                }
                paymentActivityOilCarDialog.show();
                break;
        }
    }

    public void setOilCard(String card){
        edit_activityPayment_card.setText(card);
    }

    private void getOilCardList() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        new NewHttpRequest(this, Constans.URL_zxg+ Constans.OILCARD, Constans.getOilCardList,
                "jsonObject", getOilCardListTaskId, hashMap,true, this).executeTask();
    }

    @Override
    public void netWorkError() {}

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            if (taskId == getOilCardListTaskId){
                oilCardEntityList = new Gson().fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<OilCardAdapter.OilCardEntity>>() {}.getType());
                if (oilCardEntityList != null && oilCardEntityList.size() > 0){ // 有油卡
                    edit_activityPayment_card.setFocusable(false);
                    edit_activityPayment_card.setOnClickListener(this);
                }
            }else if (taskId == getOilCardInfoTaskId){

            }else if (taskId == bindCardTaskId){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == getOilCardListTaskId){

        }else if (taskId == getOilCardInfoTaskId){

        }else if (taskId == bindCardTaskId){

        }
    }
}
