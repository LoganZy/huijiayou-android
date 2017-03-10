package com.wanglibao.huijiayou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WXBindActivity extends BaseActivity {

    @Bind(R.id.img_activity_wxbind)
    ImageView imgActivityWxbind;
    @Bind(R.id.tv_activity_wxbind_name)
    TextView tvActivityWxbindName;
    @Bind(R.id.edit_activity_bind_phone)
    EditText editActivityBindPhone;
    @Bind(R.id.edit_activity_bind_sms)
    EditText editActivityBindSms;
    @Bind(R.id.edit_activity_bind_invit)
    EditText editActivityBindInvit;
    @Bind(R.id.btn_activity_wxbind_Bind)
    Button btnActivityWxbindBind;
    @Bind(R.id.tv_activity_wxbind_sendPhoneCode)
    TextView tvActivityWxbindSendPhoneCode;
    @Bind(R.id.ll_activity_wxbind_invit)
    LinearLayout llActivityWxbindInvit;
    private String telephone;
    private String SMScode;
   // private String invite;
    private int time = 60;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxbind);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setEditTextInhibitInputSpace(editActivityBindPhone);
        // ImageButton WXLogin = (ImageButton) findViewById(R.id.WXLogin);
        // TextView tv_SMSVerify = (TextView) findViewById(R.id.tv_activityLogin_sendPhoneCode);
        // EditText et_Telephone = (EditText) findViewById(R.id.edit_activityLogin_phoneCode);
//        edittext 关于电话号码的逻辑
        editActivityBindPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9)
                                && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    editActivityBindPhone.setText(sb.toString());
                    editActivityBindPhone.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = editActivityBindPhone.getText().length();
                if (len > 13) {
                    int selEndIndex = Selection.getSelectionEnd(editActivityBindPhone.getText());
                    String str = editActivityBindPhone.getText().toString();
                    //截取新字符串
                    String newStr = str.substring(0, 13);
                    editActivityBindPhone.setText(newStr);

                }

            }
        });
        Intent intent = getIntent();

        //接收用户的信息
        getUserInformation(intent);
    }

    private void getUserInformation(Intent intent) {
       String nickname =  intent.getStringExtra("nickname");
        String headimgurl = intent.getStringExtra("headimgurl");
        tvActivityWxbindName.setText(nickname);
        //ImageSize mImageSize = new ImageSize(150,150)
        ImageLoader.getInstance().loadImage(headimgurl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imgActivityWxbind.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @OnClick({R.id.tv_activity_wxbind_sendPhoneCode, R.id.btn_activity_wxbind_Bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_activity_wxbind_sendPhoneCode:
                getSMScode();
                break;
            case R.id.btn_activity_wxbind_Bind:
                bindTelephone();
                break;
        }
    }

    /*
    * 绑定
    * */
    private void bindTelephone() {

        telephone = editActivityBindPhone.getText().toString().trim();
        SMScode = editActivityBindSms.getText().toString().trim();

        if(TextUtils.isEmpty(telephone)||telephone==null){
            ToastUtils.createNormalToast(WXBindActivity.this, "请输入手机号！");
        }else if (!telephone.startsWith("1") || telephone.length() != 13) {
            ToastUtils.createNormalToast(WXBindActivity.this, "手机号码格式不正确，请重新输入！");
        }else if(TextUtils.isEmpty(SMScode)){
            ToastUtils.createNormalToast(WXBindActivity.this, "请输入短信接收到的验证码");
        }else{
            //请求网络
            ToastUtils.createLongToast(WXBindActivity.this, "手机正确，谢谢输入！");
        }
    }

    public void getSMScode() {
        telephone = editActivityBindPhone.getText().toString().trim();
        editActivityBindSms.setText(" ");

        if(TextUtils.isEmpty(telephone)||telephone==null){
            ToastUtils.createNormalToast(WXBindActivity.this, "请输入手机号！");
        }else if (!telephone.startsWith("1") || telephone.length() != 13) {
            ToastUtils.createNormalToast(WXBindActivity.this, "手机号码格式不正确，请重新输入！");
        }else if(TextUtils.isEmpty(SMScode)) {
            ToastUtils.createNormalToast(WXBindActivity.this, "请输入短信接收到的验证码");
            llActivityWxbindInvit.setVisibility(View.VISIBLE);
            time = 60;
            //向服务器请求
            startTime();
        }
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
                if (time <= 0) {
                    handler.removeCallbacksAndMessages(null);
                    tvActivityWxbindSendPhoneCode.setEnabled(true);
                    tvActivityWxbindSendPhoneCode.setText("重新获取");
                } else {
                    tvActivityWxbindSendPhoneCode.setEnabled(false);
                    tvActivityWxbindSendPhoneCode.setText(time + "s");
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }


    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
