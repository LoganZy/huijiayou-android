package com.huijiayou.huijiayou.activity;

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
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements NewHttpRequest.RequestCallback{

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

    private Handler handler = new Handler();
    private int time = 60;
    private String telephone;
    private String SMScode;
    private String key;
    private int code;
    private static String get_access_token = "";
    private String weixinCode;
    public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static String GetUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    public static BaseResp resp;
    private String accessToken;
    private String openid;
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
                //  startActivity(new Intent(LoginActivity.this,WXBindActivity.class));
            }
        });



        setEditTextInhibitInputSpace(editActivityLoginPhone);
        // ImageButton WXLogin = (ImageButton) findViewById(R.id.WXLogin);
        // TextView tv_SMSVerify = (TextView) findViewById(R.id.tv_activityLogin_sendPhoneCode);
        // EditText et_Telephone = (EditText) findViewById(R.id.edit_activityLogin_phoneCode);
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
                editActivityLoginPhoneCode.setText(" ");

                if(TextUtils.isEmpty(telephone)||telephone==null){
                    ToastUtils.createNormalToast(LoginActivity.this, "请输入手机号！");
                }else if (!telephone.startsWith("1") || telephone.length() != 13) {
                    ToastUtils.createNormalToast(LoginActivity.this, "手机号码格式不正确，请重新输入！");
                }else if(TextUtils.isEmpty(SMScode)) {
                    ToastUtils.createNormalToast(LoginActivity.this, "请输入短信接收到的验证码");
                    ll_login_invit.setVisibility(View.VISIBLE);
                    time = 60;
                    //向服务器请求
                    startTime();
                    telephone = telephone.replaceAll(" ","");
                    getVerificationCode( telephone);
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

        if (null != resp && resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            // code返回
            weixinCode = ((SendAuth.Resp) resp).code;
            LogUtil.i(weixinCode+"------------------------------------------------------");
            get_access_token = getCodeRequest(weixinCode);
            Thread thread=new Thread(downloadRun);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public  Runnable downloadRun = new Runnable() {

        @Override
        public void run() {
            WXGetAccessToken();

        }
    };


    /*
    * 微信登录逻辑
    *
    * */
    private void WetLogin() {


        boolean isPaySupported = MyApplication.msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtils.createLongToast(LoginActivity.this,"您没有安装微信或者微信版本太低");
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        MyApplication.msgApi.sendReq(req);
    }
    public static String getUserInfo(String access_token,String openid){
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
                urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID",
                urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }
    public static String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID",
                urlEnodeUTF8(Constans.WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET",
                urlEnodeUTF8(Constans.AppSecret));
        GetCodeRequest = GetCodeRequest.replace("CODE",urlEnodeUTF8( code));
        result = GetCodeRequest;
        return result;
    }
    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 获取短信验证码
    *
    * */
    private void getVerificationCode(String callNumber){
        HashMap<String, Object>  map = new HashMap<>();
        map.put("mobile",callNumber);
        new NewHttpRequest(this,Constans.URL_wyh+Constans.ACCOUNT,Constans.MESSAGEAUTH,"jsonObject",1, map,false,this).executeTask();


    }

    /*
    * 获取微信的token等信息
    *
    * */
    private  void WXGetAccessToken(){
        HttpClient get_access_token_httpClient = new DefaultHttpClient();

        try {
            HttpGet postMethod = new HttpGet(get_access_token);
            HttpResponse response = get_access_token_httpClient.execute(postMethod); // 执行POST方法
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String str = "";
                StringBuffer sb = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                is.close();
                String josn = sb.toString();
                JSONObject json1 = new JSONObject(josn);
                accessToken = (String) json1.get("access_token");
                openid = (String) json1.get("openid");
                PreferencesUtil.putPreferences(Constans.ACCESSTOKEN,accessToken);
                PreferencesUtil.putPreferences(Constans.OPENID,openid);
            } else {
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String get_user_info_url=getUserInfo(accessToken,openid);
        WXGetUserInfo(get_user_info_url);
    }

    /*
    * 获取用户的基本信息
    *
    * */
    private  void WXGetUserInfo(String get_user_info_url){
        HttpClient get_user_info_httpClient = new DefaultHttpClient();
        String openid="";
        String nickname="";
        String headimgurl="";
        try {
            HttpGet getMethod = new HttpGet(get_user_info_url);
            HttpResponse response = get_user_info_httpClient.execute(getMethod); // 执行GET方法
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String str = "";
                StringBuffer sb = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                is.close();
                String josn = sb.toString();
                JSONObject json1 = new JSONObject(josn);
                openid = (String) json1.get("openid");
                nickname = (String) json1.get("nickname");
                headimgurl=(String)json1.get("headimgurl");
                PreferencesUtil.putPreferences(Constans.NICKNAME,nickname);
                PreferencesUtil.putPreferences(Constans.HEADIMGURL,headimgurl);
                //发送广播
                Intent intent =new Intent();
                intent.setAction("getUserInfo");
                intent.putExtra(Constans.NICKNAME,nickname);
                intent.putExtra(Constans.HEADIMGURL,headimgurl);
                LogUtil.i("返回的json:+++++++++++++++++++++++++++++++++++++++"+josn);
            } else {
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public  void setEditTextInhibitInputSpace(EditText editText) {
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
        telephone = editActivityLoginPhone.getText().toString().trim();
        SMScode = editActivityLoginPhoneCode.getText().toString().trim();
        String invite = editActivityLoginInvit.getText().toString().trim();
        if(TextUtils.isEmpty(telephone)||telephone==null){
                ToastUtils.createNormalToast(LoginActivity.this, "请输入手机号！");
        }else if (!telephone.startsWith("1") || telephone.length() != 13) {
                ToastUtils.createLongToast(LoginActivity.this, "手机号码格式不正确，请重新输入！");
        }else if(TextUtils.isEmpty(SMScode)){
            ToastUtils.createNormalToast(LoginActivity.this, "请输入短信接收到的验证码");
        }else{
            //请求网络
            //ToastUtils.createNormalToast(LoginActivity.this, "手机正确，谢谢输入！");
            telephone = telephone.replaceAll(" ","");
            HashMap<String, Object> map= new HashMap<>();
            map.put("username",telephone);
            map.put("sms_key",key);
            map.put("sms_code",code);
            new NewHttpRequest(this,Constans.URL_wyh+Constans.ACCOUNT,Constans.SIGNIN,Constans.JSONOBJECT,2,map,this).executeTask();
        }

    }

    @Override
    public void netWorkError() {
        LogUtil.i("错误了");
    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId)  {
        switch (taskId){
            case 1:

                try {
                    JSONObject jsonObject1= jsonObject.getJSONObject("data");
                   String callNum = jsonObject1.getString("call_num");
                    key =  jsonObject1.getString("key");
                    code = jsonObject1.getInt("code");
                    ToastUtils.createNormalToast("您已经获取了"+callNum+"次验证码");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            case 2:
                try {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String userId = jsonObject1.getString("id");
                    String  weixinCode =  jsonObject1.getString("weixin");
                    String  registerMode = jsonObject1.getString("register_mode");
                    String  weixinUninid =  jsonObject1.getString("weixin_unionid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        switch (taskId){
            case 1:
                ToastUtils.createNormalToast(LoginActivity.this,msg.getMessage());
            case 2:
                ToastUtils.createNormalToast(LoginActivity.this,msg.getMessage());
        }
    }


}