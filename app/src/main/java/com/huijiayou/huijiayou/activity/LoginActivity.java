package com.huijiayou.huijiayou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.HEAD;

import static com.huijiayou.huijiayou.config.Constans.USER_ID;
import static com.huijiayou.huijiayou.config.Constans.USER_TOKEN;


public class LoginActivity extends Activity implements NewHttpRequest.RequestCallback {

    @Bind(R.id.WXLogin)
    ImageButton WXLogin;
    @Bind(R.id.tv_activityLogin_sendPhoneCode)
    TextView tvActivityLoginSendPhoneCode;
    @Bind(R.id.ll_activity_login_invit)
    LinearLayout ll_login_invit;
    @Bind(R.id.edit_activityLogin_phone)
    EditText editActivityLoginPhone;
    @Bind(R.id.edit_activityLogin_phoneCode)
    EditText editActivityLoginPhoneCode;
    @Bind(R.id.edit_activityLogin_invit)
    EditText editActivityLoginInvit;
    public static String uuid;
    @Bind(R.id.ima_activityLogin_back)
    ImageButton imaActivityLoginBack;
    private int time = 60;
    private String telephone;
    private String SMScode;
    private String key;
    private Handler handler = new Handler() {
    };
    private String invit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        WXLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WetLogin();
            }
        });

        imaActivityLoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setEditTextInhibitInputSpace(editActivityLoginPhone);

//        edittext 关于电话号码的逻辑
        editActivityLoginPhone.addTextChangedListener(new TextWatcher() {
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
                    editActivityLoginPhone.setText(sb.toString());
                    editActivityLoginPhone.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = editActivityLoginPhone.getText().length();
                if (len > 13) {
                    int selEndIndex = Selection.getSelectionEnd(editActivityLoginPhone.getText());
                    String str = editActivityLoginPhone.getText().toString();
                    //截取新字符串
                    String newStr = str.substring(0, 13);
                    editActivityLoginPhone.setText(newStr);

                }

            }
        });
        /*
        * 点击获取验证码
        *
        * */
        tvActivityLoginSendPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephone = editActivityLoginPhone.getText().toString().trim();
                //editActivityLoginPhoneCode.setText(" ");

                if (TextUtils.isEmpty(telephone) || telephone == null) {
                    ToastUtils.createNormalToast(LoginActivity.this, "请输入手机号！");
                } else if (!telephone.startsWith("1") || telephone.length() != 13) {
                    ToastUtils.createNormalToast(LoginActivity.this, "手机号码格式不正确，请重新输入！");
                } else if (TextUtils.isEmpty(SMScode)) {
                    ToastUtils.createNormalToast(LoginActivity.this, "请输入短信接收到的验证码");

                    time = 60;
                    //向服务器请求
                    startTime();
                    telephone = telephone.replaceAll(" ", "");
                    getVerificationCode(telephone);
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
                            tvActivityLoginSendPhoneCode.setEnabled(true);
                            tvActivityLoginSendPhoneCode.setText("重新获取");
                        } else {
                            tvActivityLoginSendPhoneCode.setEnabled(false);
                            tvActivityLoginSendPhoneCode.setText(time + "s");
                            handler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //code = ((SendAuth.Resp) resp).code;
    }


    /*
    * 微信登录逻辑
    *
    * */
    private void WetLogin() {


        boolean isPaySupported = MyApplication.msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtils.createLongToast(LoginActivity.this, "您没有安装微信或者微信版本太低");
            return;
        }

  /*      uuid = UUID.randomUUID().toString();
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = uuid;
        MyApplication.msgApi.sendReq(req);
        finish();

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        MyApplication.msgApi.sendReq(req);
        finish();*/
         String openid = PreferencesUtil.getPreferences(Constans.OPENID, "1");
            if (TextUtils.equals(openid, "1")) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            MyApplication.msgApi.sendReq(req);
            LoginActivity.this.finish();
        }
            String token = PreferencesUtil.getPreferences(Constans.ACCESSTOKEN, "1");
            //String openid = PreferencesUtil.getPreferences(Constans.OPENID, "1");
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constans.ACCESSTOKEN, token);
        map.put(Constans.OPENID, openid);
        new NewHttpRequest(this, Constans.URL_wyh + Constans.ACCOUNT, Constans.WEIXIN_AUTH_POST, Constans.JSONOBJECT, 3, map, true, new NewHttpRequest.RequestCallback() {
            @Override
            public void netWorkError() {
            }
            @Override
            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                switch (taskId) {
                    case 3:
                        try {

                            // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            int isbind = jsonObject.getInt("is_bind");
                            LogUtil.i("++++++++++++" + isbind + "++++++++++++++++++++");
                            if (isbind == 1) {
                                String token = (String) jsonObject.get("token");
                                PreferencesUtil.putPreferences("token", token);
                                MyApplication.isLogin = true;
                                String weixinHead = jsonObject.getString("weixin_head");
                                String weixinName = jsonObject.getString("weixin_name");
                                String Phone = jsonObject.getString("phone");
                                PreferencesUtil.putPreferences("phone", Phone);
                                PreferencesUtil.putPreferences(Constans.NICKNAME, weixinName);
                                PreferencesUtil.putPreferences(Constans.HEADIMGURL, weixinHead);
                                ToastUtils.createNormalToast(isbind + "");
                                LoginActivity.this.finish();
                            } else if (isbind == 0) {
                                Intent intent = new Intent();
                                //intent.setAction("getUserInfo");
                                String unionid = PreferencesUtil.getPreferences(Constans.UNIONID, "1");
                                String nickname = PreferencesUtil.getPreferences(Constans.NICKNAME, "1");
                                String headimgurl = PreferencesUtil.getPreferences(Constans.HEADIMGURL, "1");
                                intent.putExtra(Constans.UNIONID, unionid);
                                intent.putExtra(Constans.NICKNAME, nickname);
                                intent.putExtra(Constans.HEADIMGURL, headimgurl);
                                intent.setClass(LoginActivity.this, WXBindActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }

            @Override
            public void requestError(int code, MessageEntity msg, int taskId) {
                ToastUtils.createNormalToast(msg.getMessage());
            }
        }).executeTask();


    }


    /*
    * 获取短信验证码
    *
    * */
    private void getVerificationCode(String callNumber) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", callNumber);
        new NewHttpRequest(this, Constans.URL_wyh + Constans.ACCOUNT, Constans.MESSAGEAUTH, "jsonObject", 1, map, false, this).executeTask();


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

    /*
    * 点击登录
    * */
    @OnClick(R.id.btn_activityLogin_login)
    public void onClick() {
        invit = editActivityLoginInvit.getText().toString().trim();
        if (TextUtils.isEmpty(invit) || invit == null) {
            invit = "";
        }
        telephone = editActivityLoginPhone.getText().toString().trim();
        SMScode = editActivityLoginPhoneCode.getText().toString().trim();
        // invit = editActivityLoginInvit.getText().toString().trim();
        if (TextUtils.isEmpty(telephone) || telephone == null) {
            ToastUtils.createNormalToast(LoginActivity.this, "请输入手机号！");
        } else if (!telephone.startsWith("1") || telephone.length() != 13) {
            ToastUtils.createLongToast(LoginActivity.this, "手机号码格式不正确，请重新输入！");
        } else if (TextUtils.isEmpty(SMScode)) {
            ToastUtils.createNormalToast(LoginActivity.this, "请输入短信接收到的验证码");
        } else {
            //请求网络
            //ToastUtils.createNormalToast(LoginActivity.this, "手机正确，谢谢输入！");
            telephone = telephone.replaceAll(" ", "");
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", telephone);
            map.put("sms_key", key);
            map.put("sms_code", SMScode);
            map.put("invite_code", invit);
            new NewHttpRequest(this, Constans.URL_wyh + Constans.ACCOUNT, Constans.SIGNIN, Constans.JSONOBJECT, 2, map, this).executeTask();

        }

    }

    @Override
    public void netWorkError() {
        LogUtil.i("错误了");
    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        switch (taskId) {
            case 1:

                try {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String callNum = jsonObject1.getString("call_num");
                    key = jsonObject1.getString("key");
                    int is_registed = jsonObject1.getInt("is_registed");
                    if (is_registed == 0) {
                        ll_login_invit.setVisibility(View.VISIBLE);
                    }
                    int code = jsonObject1.getInt("code");
                    ToastUtils.createNormalToast("您已经获取了" + code + "次验证码");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                    String userId = jsonObject2.getString("id");
                    String Phone = jsonObject2.getString("phone");
                    String registerMode = jsonObject2.getString("register_mode");
                    String weixinUninid = jsonObject2.getString("weixin_unionid");
                    String weixinHead = jsonObject2.getString("weixin_head");
                    String weixinName = jsonObject2.getString("weixin_name");
                    String token = (String) jsonObject2.get("token");
                    String invite_code = (String) jsonObject2.get("invite_code");
                    String phone = (String) jsonObject2.get("phone");
                    PreferencesUtil.putPreferences(USER_ID, userId);
                    PreferencesUtil.putPreferences(USER_TOKEN, token);
                    PreferencesUtil.putPreferences(Constans.USER_INVITE_CODE, invite_code);
                    PreferencesUtil.putPreferences(Constans.USER_PHONE, phone);
                    PreferencesUtil.putPreferences("phone",Phone);
                    PreferencesUtil.putPreferences(Constans.NICKNAME,weixinName);
                    PreferencesUtil.putPreferences(Constans.HEADIMGURL,weixinHead);
                    ToastUtils.createNormalToast(Phone);
                    MyApplication.isLogin = true;
                    PreferencesUtil.putPreferences(Constans.ISLOGIN,true);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }

    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        switch (taskId) {
            case 1:
                ToastUtils.createNormalToast(LoginActivity.this, msg.getMessage());
            case 2:
                ToastUtils.createNormalToast(LoginActivity.this, msg.getMessage());
        }
    }
}