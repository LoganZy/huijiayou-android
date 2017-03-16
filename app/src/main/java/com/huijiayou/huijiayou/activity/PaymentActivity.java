package com.huijiayou.huijiayou.activity;

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

import com.huijiayou.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

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


//    @Bind(R.id.tv_activityPayment_agreement)
//    TextView tv_activityPayment_agreement;


    private boolean shouldStopChange = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("确认订单");

        init();
//        tv_activityPayment_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tv_activityPayment_agreement.getPaint().setAntiAlias(true);

//        edit_activityPayment_card.addTextChangedListener(new TextWatcher() {
////            int after;
////            CharSequence beforeText;
//            int beforeStart,beforeCount;
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                this.after = after;
////                this.beforeText = s.toString();
//
////                beforeCount = count;
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                beforeStart = start;
////                if (s == null) {
////                    return;
////                }
////                if (after == 1 || after == 0){
////                    if (after == 0){
////                        String value = beforeText.toString().substring(beforeStart,beforeStart+beforeCount);
////                        if (" ".equals(value)){
////                            value = beforeText.toString().substring(0,beforeStart-1);
////                            if (s.length() > start){
////                                value = value + s.toString().substring(beforeStart);
////                            }
////                            s = value;
////                        }
////                    }
////                    StringBuffer stringBuffer = new StringBuffer();
////                    String text = s.toString().replace(" ","");
////                    boolean isAdd = false;
////                    while (text.length() != 0){
////                        if (text.length() >= 4){
////                            stringBuffer.append(text.substring(0,4)+" ");
////                        }else{
//////                            stringBuffer.append()
////                        }
////                        text = text.substring(4,text.length());
////                        isAdd = true;
////                    }
////                    if (isAdd){
////                        edit_activityPayment_card.setText(stringBuffer);
////                    }
////                }else {
////                    edit_activityPayment_card.setSelection(s.length());
////                }
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                format(s);
//            }
//            private void format(Editable editable) {
//                if (shouldStopChange) {
//                    shouldStopChange = false;
//                    return;
//                }
//
//                shouldStopChange = true;
//
//                String str = editable.toString().trim().replaceAll(" ", "");
//                int len = str.length();
//                int courPos;
//
//                StringBuilder builder = new StringBuilder();
//                for (int i = 0; i < len; i++) {
//                    builder.append(str.charAt(i));
//                    if (i == 3 || i == 7 || i == 11 || i == 15 || i == 19) {
//                        if (i != len - 1)
//                            builder.append(" ");
//                    }
//                }
//                courPos = builder.length();
//                edit_activityPayment_card.setText(builder.toString());
//
//                edit_activityPayment_card.setSelection(courPos);
//            }
//        });




//        edit_activityPayment_card.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s == null || s.length() == 0)
//                    return;
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < s.length(); i++) {
//                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
//                        continue;
//                    } else {
//                        sb.append(s.charAt(i));
//                        if ((sb.length() == 4 || sb.length() == 9)
//                                && sb.charAt(sb.length() - 1) != ' ') {
//                            sb.insert(sb.length() - 1, ' ');
//                        }
//                    }
//                }
//                if (!sb.toString().equals(s.toString())) {
//                    int index = start + 1;
//                    if (sb.charAt(start) == ' ') {
//                        if (before == 0) {
//                            index++;
//                        } else {
//                            index--;
//                        }
//                    } else {
//                        if (before == 1) {
//                            index--;
//                        }
//                    }
//                    edit_activityPayment_card.setText(sb.toString());
//                    edit_activityPayment_card.setSelection(index);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                int len = edit_activityPayment_card.getText().length();
////                if (len > 13) {
////                    int selEndIndex = Selection.getSelectionEnd(edit_activityPayment_card.getText());
////                    String str = edit_activityPayment_card.getText().toString();
////                    //截取新字符串
////                    String newStr = str.substring(0, 13);
////                    edit_activityPayment_card.setText(newStr);
////
////                }
//
//            }
//        });
    }

    private void init(){
        if (true){ // 判断是否有油卡
            edit_activityPayment_card.setEnabled(false);
        }

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
        }
    }
}
