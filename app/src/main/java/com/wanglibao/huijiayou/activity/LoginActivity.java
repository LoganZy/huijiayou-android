package com.wanglibao.huijiayou.activity;

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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.config.Constans;
import com.wanglibao.huijiayou.jsonrpc.JsonRPCAsyncTask;
import com.wanglibao.huijiayou.request.RequestInterface;
import com.wanglibao.huijiayou.utils.LogUtil;
import com.wanglibao.huijiayou.utils.ToastUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class LoginActivity extends BaseActivity {

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
    private String weixinCode;
    private Handler handler = new Handler();
    private int time = 60;
    private String telephone;
    private String SMScode;
    private String invite;
    private RequestInterface requestInterface;
    private Retrofit retrofit;

    // private static String get_access_token = "";
    // 获取第一步的code后，请求以下链接获取access_token
    //public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //获取用户个人信息
    // public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != Constans.resp && Constans.resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            // code返回
            weixinCode = ((SendAuth.Resp) Constans.resp).code;
            WXGetAccessToken(weixinCode);

        }
    }

    private void initView() {


        WXLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WetLogin();
                startActivity(new Intent(LoginActivity.this,WXBindActivity.class));
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

           private void getVerificationCode(String callNumber){

               //发送网络请求，请求短信验证码




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
    /*
    * 微信登录逻辑
    *
    * */
    private void WetLogin() {
        Constans.WXapi = WXAPIFactory.createWXAPI(LoginActivity.this, Constans.WX_APP_ID, true);
        if (!Constans.WXapi.isWXAppInstalled()) {
            Toast.makeText(LoginActivity.this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Constans.WXapi.isWXAppSupportAPI()) {
            Toast.makeText(LoginActivity.this, "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return;
        }
        Constans.WXapi.registerApp(Constans.WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        Constans.WXapi.sendReq(req);
    }


    /*
    * 获取微信的token等信息
    *
    * */
    private void WXGetAccessToken(String weixinCode) {

        //注意，服务器主机应该以/结束，
//设置服务器主机
//.addConverterFactory(GsonConverterFactory.create())//配置Gson作为json的解析器
        retrofit = new Retrofit.Builder()
               //注意，服务器主机应该以/结束，
               .baseUrl(Constans.WXBaseUrl)//设置服务器主机
               //.addConverterFactory(GsonConverterFactory.create())//配置Gson作为json的解析器
               .build();
        requestInterface = retrofit.create(RequestInterface.class);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("appid", Constans.WX_APP_ID);
        map.put("secret", Constans.AppSecret);
        map.put("code", weixinCode);
        map.put("grant_type", "authorization_code");
        Call order = requestInterface.getAccess_token(map);
        order.enqueue(new Callback() {
            private String accessToken;
            private String openid;
            @Override
            public void onResponse(Call call, Response response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.toString());
                    accessToken = jsonObject.getString("access_token");

                    openid = jsonObject.getString("open_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                WXGetUserInfo(accessToken ,openid);
            }




            @Override
            public void onFailure(Call call, Throwable t) {
                LogUtil.i(t.getMessage());
            }
        });

    }

    /*
    * 获取用户的基本信息
    *
    * */
    private void WXGetUserInfo(String accessToken, String openid) {

            Map<String ,String> map2 = new LinkedHashMap<String, String>();
            map2.put("access_token",accessToken);
            map2.put("openid",openid);
            Call order2 = requestInterface.getImformation(map2);
            order2.enqueue(new Callback() {
            public String headimgurl;
            public String nickname;
          //  public String openid;

            @Override
            public void onResponse(Call call, Response response) {
             // String result =   response.body().toString();
                JSONObject json  = null;
                try {
                    json = new JSONObject(response.toString());
                    nickname = (String) json.getString("nickname");
                    headimgurl=(String)json.getString("headimgurl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //openid = (String) json.get("openid").toString();

                Intent intent = new  Intent();
                //intent.putExtra("opendi",openid);
                intent.setClass(LoginActivity.this,WXBindActivity.class);
                intent.putExtra("nickname",nickname);
                intent.putExtra("headimgurl",headimgurl);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });


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
        invite  =editActivityLoginInvit.getText().toString().trim();
        if(TextUtils.isEmpty(telephone)||telephone==null){
                ToastUtils.createNormalToast(LoginActivity.this, "请输入手机号！");
        }else if (!telephone.startsWith("1") || telephone.length() != 13) {
                ToastUtils.createLongToast(LoginActivity.this, "手机号码格式不正确，请重新输入！");
        }else if(TextUtils.isEmpty(SMScode)){
            ToastUtils.createNormalToast(LoginActivity.this, "请输入短信接收到的验证码");
        }else{
            //请求网络
            ToastUtils.createNormalToast(LoginActivity.this, "手机正确，谢谢输入！");
        }

    }
}