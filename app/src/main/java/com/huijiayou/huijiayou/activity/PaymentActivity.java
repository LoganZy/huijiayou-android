package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.CouponAdapter;
import com.huijiayou.huijiayou.adapter.OilCardAdapter;
import com.huijiayou.huijiayou.adapter.RechargeDetailDailogAdapter;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.threadpool.ThreadPool;
import com.huijiayou.huijiayou.utils.CommitUtils;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.huijiayou.huijiayou.widget.PaymentActivityOilCarDialog;
import com.huijiayou.huijiayou.widget.RechargeDetailsDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    public RelativeLayout rl_activityPayment_inputCard;  //支付第一步  输入加油卡号

    @Bind(R.id.edit_activityPayment_card)
    EditText edit_activityPayment_card;  //接收加油卡号的输入，在已有加油卡时 不可编辑 点击弹出加油卡列表

//    @Bind(R.id.imgBtn_activityPayment_next)
//    ImageButton imgBtn_activityPayment_next; //点击进入下一步

    @Bind(R.id.tv_activityPayment_cardTag)
    TextView tv_activityPayment_cardTag;  //输入加油卡号时 判断输入是否正确
    //---------end----------支付页  准备支付   第一步  输入加油卡号

    @Bind(R.id.rl_activityPayment_coupon)
    public RelativeLayout rl_activityPayment_coupon; //支付第二步  显示油卡的信息和选择优惠券

    @Bind(R.id.tv_activityPayment_coupon_card)
    TextView tv_activityPayment_coupon_card;  //显示加油卡号 和 油卡类型的图标 drawableRight

    @Bind(R.id.ll_activityPayment_coupon_userName)
    RelativeLayout ll_activityPayment_coupon_userName;

    @Bind(R.id.tv_activityPayment_coupon_userName)
    TextView tv_activityPayment_coupon_userName;  //油卡持有人姓名

    @Bind(R.id.tv_activityPayment_coupon_coupon)
    TextView tv_activityPayment_coupon_coupon;  //优惠券  点击弹出优惠券列表

    @Bind(R.id.rb_activityPayment_coupon_option)
    CheckBox rb_activityPayment_coupon_option;  //选择项  是否同意协议，默认选中

    @Bind(R.id.tv_activityPayment_coupon_agreement)
    TextView tv_activityPayment_coupon_agreement;  //充值协议  点击查看协议h5页

    @Bind(R.id.ll_activityPayment_coupon_payment)
    LinearLayout ll_activityPayment_coupon_payment;

    @Bind(R.id.tv_activityPayment_coupon_oil)
    TextView tv_activityPayment_coupon_oil;

    @Bind(R.id.tv_activityPayment_coupon_payment_money)
    TextView tv_activityPayment_coupon_payment_money;

    @Bind(R.id.cb_activityPayment_coupon_oil)
    CheckBox cb_activityPayment_coupon_oil;
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

    @Bind(R.id.recyclerView_activityPayment_success_time)
    RecyclerView recyclerView_activityPayment_success_time;

    PaymentActivityOilCarDialog paymentActivityOilCarDialog;

    //totalOil 总共有多少油滴
    //oil 本次支付所支付的油滴数
    int moneyMonth,product_id,month,oil,totalOil;
    double total,discountTotal,saveMoney;
    public String oilCard;
    public String oilCardName;
    private boolean isUseOil = false;
    String orderNumber;
    CouponAdapter.Coupon currentCoupon;


    int getOilCardListTaskId = 1;
    int getOilCardInfoTaskId = 2;
    int bindCardTaskId = 3;
    int UserEnableOilTaskId = 4;
    int userPacketsListTaskId = 5;
    int orderTaskId = 6;
    int checkOrderTaskId = 7;
    int payChannelTaskId = 8;
    int backTaskId = 9;

    boolean isGetOilCardInfo = true;

    public int addOilCarRequestCode = 100;
    public int couponRequestCode = 200;

    RechargeDetailsDialog rechargeDetailsDialog;

    String orderInfo;//支付宝支付的关键字段

    public static final String type_pay = "pay";
    public static final String type_order = "order";
    private String type = type_order;

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
        recyclerView_activityPayment_success_time.setLayoutManager(new LinearLayoutManager(this));
        type = intent.getStringExtra("type");
        if (type_pay.equals(type)){
            orderNumber = intent.getStringExtra("orderNumber");
            rl_activityPayment_inputCard.setVisibility(View.GONE);
            rl_activityPayment_payment.setVisibility(View.VISIBLE);
            checkOrder();
        }else{
            getOilCardList();
            cb_activityPayment_coupon_oil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        isUseOil = true;
                        tv_activityPayment_coupon_oil.setVisibility(View.VISIBLE);
                        tv_activityPayment_coupon_oil.setText("油滴已抵扣"+ ((double)oil)/100 +"元");
                    }else {
                        isUseOil = false;
                        tv_activityPayment_coupon_oil.setVisibility(View.GONE);
                    }
                    tv_activityPayment_coupon_payment_money.setText("支付"+calculationMoney()+"元");
                }
            });

            edit_activityPayment_card.addTextChangedListener(new TextWatcher() {
                int beforeTextLength=0;
                int onTextLength=0;
                boolean isChanged = false;

                int location=0;//记录光标的位置
                private char[] tempChar;
                private StringBuffer buffer = new StringBuffer();
                int konggeNumberB = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    beforeTextLength = s.length();
                    if(buffer.length()>0){
                        buffer.delete(0, buffer.length());
                    }
                    konggeNumberB = 0;
                    for (int i = 0; i < s.length(); i++) {
                        if(s.charAt(i) == ' '){
                            konggeNumberB++;
                        }
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    onTextLength = s.length();
                    buffer.append(s.toString());
                    if(onTextLength == beforeTextLength || onTextLength <= 3 || isChanged){
                        isChanged = false;
                        return;
                    }
                    isChanged = true;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(isChanged){
                        String text = s != null && s.length() > 0 ? s.toString().replace(" ","") : "";
                        String fristChar = s != null && s.length() > 0 ? s.toString().substring(0,1) : "";
                        if (isGetOilCardInfo && "1".equals(fristChar) && text.length() == 19){
                            getOilCardInfo(text);
                        }else if (isGetOilCardInfo && "9".equals(fristChar) && text.length() == 16){
                            getOilCardInfo(text);
                        }
                        location = edit_activityPayment_card.getSelectionEnd();
                        int index = 0;
                        while (index < buffer.length()) {
                            if(buffer.charAt(index) == ' '){
                                buffer.deleteCharAt(index);
                            }else{
                                index++;
                            }
                        }

                        index = 0;
                        int konggeNumberC = 0;
                        while (index < buffer.length()) {
                            //银行卡号的话需要改这里
                            if((index == 4 || index == 9 || index == 14 || index == 19 || index == 24)){
                                buffer.insert(index, ' ');
                                konggeNumberC++;
                            }
                            index++;
                        }

                        if(konggeNumberC>konggeNumberB){
                            location+=(konggeNumberC-konggeNumberB);
                        }

                        tempChar = new char[buffer.length()];
                        buffer.getChars(0, buffer.length(), tempChar, 0);
                        String str = buffer.toString();
                        if(location>str.length()){
                            location = str.length();
                        }else if(location < 0){
                            location = 0;
                        }

                        edit_activityPayment_card.setText(str);
                        Editable etable = edit_activityPayment_card.getText();
                        Selection.setSelection(etable, location);
                        isChanged = false;
                    }
                }
            });

            rb_activityPayment_coupon_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        ll_activityPayment_coupon_payment.setBackgroundColor(getResources().getColor(R.color.orange_FF7320));
                        ll_activityPayment_coupon_payment.setEnabled(true);
                    }else{
                        ll_activityPayment_coupon_payment.setBackgroundColor(getResources().getColor(R.color.my_edittext_frame_color));
                        ll_activityPayment_coupon_payment.setEnabled(false);
                    }
                }
            });
            rb_activityPayment_payment_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        btn_activityPayment_payment_payment.setBackgroundColor(getResources().getColor(R.color.orange_FF7320));
                        btn_activityPayment_payment_payment.setEnabled(true);
                    }else{
                        btn_activityPayment_payment_payment.setBackgroundColor(getResources().getColor(R.color.my_edittext_frame_color));
                        btn_activityPayment_payment_payment.setEnabled(false);
                    }
                }
            });
        }
    }

    //计算 实际支付金额
    private double calculationMoney(){
        double money = discountTotal;
        if (currentCoupon != null){
            if ("0".equals(currentCoupon.getPackets_type())){//直抵
                double amount = Double.parseDouble(currentCoupon.getAmount());
                money = money - amount;
            }else if ("1".equals(currentCoupon.getPackets_type())){//折扣
                double rate = Double.parseDouble(currentCoupon.getRate());
                money = money * rate;
            }
        }
        if (isUseOil){
            money = money - ((double)oil)/100;
        }
        return CommitUtils.decimal2(money);
    }

    @Override
    public void onBackPressed() {
        if (rl_activityPayment_inputCard.isShown() || rl_activityPayment_success.isShown()){
            super.onBackPressed();
        }else if (rl_activityPayment_coupon.isShown()){
            getOilCardList();
            rl_activityPayment_coupon.setVisibility(View.GONE);
            rl_activityPayment_inputCard.setVisibility(View.VISIBLE);
        }else if (rl_activityPayment_payment.isShown() && type_order.equals(type)){
            UserEnableOil();
            userPacketsList();
            rl_activityPayment_payment.setVisibility(View.GONE);
            rl_activityPayment_coupon.setVisibility(View.VISIBLE);
        }else if(rl_activityPayment_payment.isShown() && type_pay.equals(type)){
            super.onBackPressed();
        }
    }

    public void rechargeDetailsDialog(View view){
        if (rechargeDetailsDialog == null){
            rechargeDetailsDialog = new RechargeDetailsDialog(this,moneyMonth,month);
        }
        rechargeDetailsDialog.create();
    }

    public void shareCodePicture(View view){
        startActivity(new Intent(this,InvitationShareActivity.class));
    }

    @OnClick({ R.id.ll_activityPayment_coupon_payment, R.id.btn_activityPayment_payment_payment
            , R.id.tv_activityPayment_coupon_coupon,R.id.tv_activityPayment_coupon_agreement
            ,R.id.tv_activityPayment_payment_agreement})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_activityPayment_coupon_payment:
                order();
//                payChannel();
                break;
            case R.id.btn_activityPayment_payment_payment:
                if (!TextUtils.isEmpty(orderInfo)){
                    ThreadPool.getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(NetConfig.URL.indexOf("test") > 0){
                                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);   //沙箱
                            }
                            PayTask payTask = new PayTask(PaymentActivity.this);
                            Map<String, String> result = payTask.payV2(orderInfo,true);
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);

                        }
                    });
                }else{
                    ToastUtils.createNormalToast(this,"缺少支付信息");
                }

                break;
            case R.id.edit_activityPayment_card:
                paymentActivityOilCarDialog = new PaymentActivityOilCarDialog(this,oilCardEntityList);
                paymentActivityOilCarDialog.show();
                break;
            case R.id.tv_activityPayment_coupon_coupon:
                Intent intent = new Intent(this,CouponActivity.class);
                intent.putExtra("type",CouponActivity.SELECTED_TYPE);
                intent.putExtra("product_id",product_id+"");
                intent.putExtra("oilCard",oilCard);
                intent.putExtra("discountTotal",discountTotal+"");
                startActivityForResult(intent,couponRequestCode);
                break;
            case R.id.tv_activityPayment_coupon_agreement:
            case R.id.tv_activityPayment_payment_agreement:
                Intent intent1 = new Intent(this,WebViewActivity.class);
                intent1.putExtra("title","用户协议");
                intent1.putExtra("url",NetConfig.user_agreement);
                startActivity(intent1);
                break;
        }
    }

    private boolean isOpenOilCardListDialog = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == addOilCarRequestCode && resultCode == RESULT_OK){
            isOpenOilCardListDialog = true;
            getOilCardList();
        }else if (requestCode == couponRequestCode && resultCode == RESULT_OK){
            Bundle bundle = data.getBundleExtra("coupon");
            if (bundle != null){
                currentCoupon = (CouponAdapter.Coupon) bundle.getSerializable("coupon");
                tv_activityPayment_coupon_coupon.setText(currentCoupon.getPackets_name());
            }else{
                currentCoupon = null;
                tv_activityPayment_coupon_coupon.setText("不使用优惠券");
            }
            if (totalOil > 0){
                double money = discountTotal;
                double couponDiscountTotal = 0;
                if (currentCoupon != null){
                    if ("0".equals(currentCoupon.getPackets_type())){//直抵
                        double amount = Double.parseDouble(currentCoupon.getAmount());
                        couponDiscountTotal = money - amount;
                        if (totalOil > couponDiscountTotal * 100){
                            oil = (int) (couponDiscountTotal * 100);
                        }
                    }else if ("1".equals(currentCoupon.getPackets_type())){//折扣
                        double rate = Double.parseDouble(currentCoupon.getRate());
                        couponDiscountTotal = money * rate;
                        if (totalOil > couponDiscountTotal * 100){
                            oil = (int) (money * rate * 100);
                        }
                    }
                }else{
                    couponDiscountTotal = money;
                    if (totalOil > couponDiscountTotal * 100){
                        oil = (int) (money * 100);
                    }
                }
                double oilPrice = ((double)oil) / 100;
                cb_activityPayment_coupon_oil.setText("可用"+oil+"油滴抵 ¥"+oilPrice);
                tv_activityPayment_coupon_oil.setText("油滴已抵扣"+oilPrice+"元");
            }else{
                cb_activityPayment_coupon_oil.setVisibility(View.GONE);
                tv_activityPayment_coupon_oil.setVisibility(View.GONE);
            }
            tv_activityPayment_coupon_payment_money.setText("支付"+calculationMoney()+"元");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择油卡的Dialog 调用
     * @param card
     */
    public void setOilCard(String card){
        isGetOilCardInfo = false;
        tv_activityPayment_cardTag.setText("正确");
        rl_activityPayment_inputCard.setVisibility(View.GONE);
        rl_activityPayment_coupon.setVisibility(View.VISIBLE);
        UserEnableOil();
        userPacketsList();
        tv_activityPayment_coupon_card.setText(oilCard);
        if (TextUtils.isEmpty(oilCardName)){
            ll_activityPayment_coupon_userName.setVisibility(View.GONE);
        }else{
            tv_activityPayment_coupon_userName.setText(oilCardName);
        }
        edit_activityPayment_card.setText(card);
        tv_activityPayment_coupon_payment_money.setText("支付"+calculationMoney()+"元");
    }

    private void showPayErrorDialog(){

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
                    edit_activityPayment_card.setHint("请选择您的油卡");
                    if (isOpenOilCardListDialog){
                        paymentActivityOilCarDialog = new PaymentActivityOilCarDialog(this,oilCardEntityList);
                        paymentActivityOilCarDialog.show();
                        isOpenOilCardListDialog = false;
                    }
                }else{
                    edit_activityPayment_card.setHint("请输入加油卡卡号");
                    edit_activityPayment_card.setFocusable(true);
                    edit_activityPayment_card.setOnClickListener(null);
                }
            }else if (taskId == getOilCardInfoTaskId){
                tv_activityPayment_cardTag.setText("正确");
                oilCard = edit_activityPayment_card.getText().toString().replace(" ","");
                oilCardName = jsonObject.getString("username");
                bindCard();
            }else if (taskId == bindCardTaskId){
                UserEnableOil();
                userPacketsList();
                tv_activityPayment_coupon_card.setText(oilCard);
                if (TextUtils.isEmpty(oilCardName)){
                    ll_activityPayment_coupon_userName.setVisibility(View.GONE);
                }else{
                    tv_activityPayment_coupon_userName.setText(oilCardName);
                }

                rl_activityPayment_inputCard.setVisibility(View.GONE);
                rl_activityPayment_coupon.setVisibility(View.VISIBLE);
                tv_activityPayment_coupon_payment_money.setText("支付"+calculationMoney()+"元");

            }else if (taskId == UserEnableOilTaskId){
                Object enableOil = jsonObject.get("enableOil");
                if (enableOil != null && !"null".equals(enableOil.toString()) && !TextUtils.isEmpty(enableOil.toString())){
                    totalOil = Integer.parseInt(enableOil.toString());
                    if (totalOil > 0){
                        double oilPrice = ((double)totalOil) / 100;
                        if (oilPrice > calculationMoney()){
                            oil = (int) (calculationMoney() * 100);
                        }else{
                            oil = totalOil;
                        }
                        oilPrice = ((double)oil) / 100;
                        cb_activityPayment_coupon_oil.setText("可用"+oil+"油滴抵 ¥"+oilPrice);
                        tv_activityPayment_coupon_oil.setText("油滴已抵扣"+oilPrice+"元");
                    }else{
                        oil = 0;
                        cb_activityPayment_coupon_oil.setVisibility(View.GONE);
                        tv_activityPayment_coupon_oil.setVisibility(View.GONE);
                    }
                }else{
                    oil = 0;
                    cb_activityPayment_coupon_oil.setVisibility(View.GONE);
                    tv_activityPayment_coupon_oil.setVisibility(View.GONE);
                }

            }
            else if (taskId == userPacketsListTaskId){
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                ArrayList<CouponAdapter.Coupon> couponArrayList = new Gson().fromJson(jsonArray1.toString(),
                        new TypeToken<ArrayList<CouponAdapter.Coupon>>() {}.getType());
                currentCoupon = null;
                if (couponArrayList != null && couponArrayList.size() > 0){
                    tv_activityPayment_coupon_coupon.setEnabled(true);
                }else{
                    tv_activityPayment_coupon_coupon.setTextColor(getResources().getColor(R.color.gray));
                    tv_activityPayment_coupon_coupon.setText("暂无优惠券可用");
                    tv_activityPayment_coupon_coupon.setEnabled(false);
                }
            }
            else if (taskId == orderTaskId){
                orderNumber = (String) jsonObject.getJSONObject("data").get("order_num");
                checkOrder();
            }else if (taskId == checkOrderTaskId){
                String status = jsonObject.getJSONObject("data").getString("status");
                if ("1".equals(status)){ //支付成功
                    rl_activityPayment_coupon.setVisibility(View.GONE);
                    rl_activityPayment_success.setVisibility(View.VISIBLE);
                    if (rechargeDetailsDialog == null){
                        rechargeDetailsDialog = new RechargeDetailsDialog(this,moneyMonth,month);
                    }
                    recyclerView_activityPayment_success_time.setAdapter(new RechargeDetailDailogAdapter(rechargeDetailsDialog.rechargeArrayList));
                }else if ("0".equals(status)){
                    orderInfo = (String) jsonObject.getJSONObject("data").get("response");
                    rl_activityPayment_coupon.setVisibility(View.GONE);
                    rl_activityPayment_payment.setVisibility(View.VISIBLE);
                    btn_activityPayment_payment_payment.setText("支付"+calculationMoney()+"元");
                }
            }else if (taskId == payChannelTaskId){

            }else if (taskId == backTaskId){
                rl_activityPayment_payment.setVisibility(View.GONE);
                rl_activityPayment_success.setVisibility(View.VISIBLE);
                if (rechargeDetailsDialog == null){
                    rechargeDetailsDialog = new RechargeDetailsDialog(this,moneyMonth,month);
                }
                recyclerView_activityPayment_success_time.setAdapter(new RechargeDetailDailogAdapter(rechargeDetailsDialog.rechargeArrayList));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == getOilCardListTaskId){

        }else if (taskId == getOilCardInfoTaskId){
            tv_activityPayment_cardTag.setText(msg.getMessage());
        }else if (taskId == bindCardTaskId){
            tv_activityPayment_cardTag.setText(msg.getMessage());
        }else{
            ToastUtils.createNormalToast(this,msg.getMessage());
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1){
                Map<String,String> map = (Map<String, String>) msg.obj;
                String memo = map.get("memo");
                String result = map.get("result");
                String resultStatus = map.get("resultStatus");
                if ("9000".equals(resultStatus)){
                    ToastUtils.createNormalToast(PaymentActivity.this, "支付成功");
                }else{
                    ToastUtils.createNormalToast(PaymentActivity.this, memo);
                }
                if (!"6001".equals(resultStatus) && !"6002".equals(resultStatus)){
                    back(memo, result, resultStatus);
                }
            }
            return false;
        }
    });

    /**
     * 获取油卡列表
     */
    private void getOilCardList() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("product_id",product_id);

        new NewHttpRequest(this, NetConfig.OILCARD, NetConfig.getOilCardList,
                "jsonObject", getOilCardListTaskId, hashMap,true, this).executeTask();
    }

    /**
     * 获取油卡信息
     * @param oil_card
     */
    private void getOilCardInfo(String oil_card){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");

        hashMap.put("oil_card",oil_card);

        new NewHttpRequest(this, NetConfig.OILCARD, NetConfig.getOilCardInfo, "jsonObject", getOilCardInfoTaskId, hashMap, true, this).executeTask();
    }

    /**
     * 绑定油卡
     */
    public void bindCard(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("oil_card",oilCard);

        new NewHttpRequest(this, NetConfig.OILCARD, NetConfig.bindCard, "jsonObject", bindCardTaskId, hashMap, true, this).executeTask();
    }

    /**
     * 获取可用油滴
     */
    private void UserEnableOil(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        hashMap.put(Constans.USER_ID,userId);

        new NewHttpRequest(this, NetConfig.ACCOUNT, NetConfig.UserEnableOil, "jsonObject", UserEnableOilTaskId, hashMap, true, this).executeTask();
    }

    /**
     * 获取用户红包列表
     */
    private void userPacketsList(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        hashMap.put(Constans.USER_ID,userId);
        hashMap.put("products_id",product_id);
        hashMap.put("card_num",oilCard);
        hashMap.put("amount",discountTotal);

        new NewHttpRequest(this, NetConfig.ACCOUNT, NetConfig.userPacketsList, "jsonObject", userPacketsListTaskId,
                hashMap, true, this).executeTask();
    }

    /**
     * 下单
     */
    private void order(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("oil_card",oilCard);
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("product_id",product_id);
        hashMap.put("money", moneyMonth);
        if (currentCoupon != null){
            hashMap.put("uuid", currentCoupon.getUuid());
        }
        if (isUseOil){
            hashMap.put("oil_droplets", oil);
        }
        hashMap.put("pay_channel", "ali_pay");

        new NewHttpRequest(this, NetConfig.ORDER, NetConfig.order, "jsonObject", orderTaskId,
                hashMap, true, this).executeTask();
    }

    private void checkOrder(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("order_number",orderNumber);
        hashMap.put("pay_channel","ali_pay");
        new NewHttpRequest(this, NetConfig.PAY, NetConfig.checkOrder, "jsonObject", checkOrderTaskId,
                hashMap, true, this).executeTask();
    }

    private void back(String memo, String result, String resultStatus){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        hashMap.put("memo",memo);
        hashMap.put("result",result);
        hashMap.put("resultStatus",resultStatus);
        new NewHttpRequest(this, NetConfig.PAY, NetConfig.back, "jsonObject", backTaskId,
                hashMap, true, this).executeTask();
    }


//    private void payChannel(){
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("time",System.currentTimeMillis());
//        hashMap.put("sign","");
//        new NewHttpRequest(this, NetConfig.URL_zxg + NetConfig.PAY, NetConfig.payChannel, "jsonObject", payChannelTaskId,
//                hashMap, true, this).executeTask();
//    }
}
