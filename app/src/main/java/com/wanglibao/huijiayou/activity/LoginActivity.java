package com.wanglibao.huijiayou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanglibao.huijiayou.Bean.WeiXIn;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.config.Constans;
import com.wanglibao.huijiayou.jsonrpc.JsonRPCAsyncTask;
import com.wanglibao.huijiayou.request.RequestInterface;
import com.wanglibao.huijiayou.utils.LogUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.WXLogin)
    ImageButton WXLogin;
    @Bind(R.id.tv_activityLogin_sendPhoneCode)
    TextView tvActivityLoginSendPhoneCode;
    @Bind(R.id.ll_activity_login_invit)
    LinearLayout ll_login_invit;
    private String weixinCode;
    private String WXBaseUrl = "https://api.weixin.qq.com/";
    private Handler handler= new Handler()  ;
    private int time = 60;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String telephone;
    // private static String get_access_token = "";
    // 获取第一步的code后，请求以下链接获取access_token
    //public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //获取用户个人信息
    // public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("登录");
        initView();


    }

    private void initView() {

        // ImageButton WXLogin = (ImageButton) findViewById(R.id.WXLogin);
       // TextView tv_SMSVerify = (TextView) findViewById(R.id.tv_activityLogin_sendPhoneCode);
        EditText et_Telephone = (EditText) findViewById(R.id.edit_activityLogin_phoneCode);
        telephone = et_Telephone.getText().toString().trim();
        tvActivityLoginSendPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示隐藏的邀请栏
                ll_login_invit.setVisibility(View.VISIBLE);
                time = 60;
                //向服务器请求
                startTime();
               // SMSVerify(telephone);
            }
        });
        WXLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WetLogin();
            }
        });


    }

    /*
    * 短信验证的功能
    *
    * */
    private void SMSVerify(String code) {

        String loginUrl = "";
        JsonRPCAsyncTask jsonRPCAsyncTask = new JsonRPCAsyncTask(LoginActivity.this, loginUrl, "messageAuth", null, "jsonObject", 1, code);
        jsonRPCAsyncTask.execute();
    }

    private void WetLogin() {
        Constans.WXapi = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID, true);
        if (!Constans.WXapi.isWXAppInstalled()) {
            Toast.makeText(this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Constans.WXapi.isWXAppSupportAPI()) {
            Toast.makeText(this, "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return;
        }
        Constans.WXapi.registerApp(Constans.WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        Constans.WXapi.sendReq(req);
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

    /*
    * 获取微信的token等信息
    *
    * */
    private void WXGetAccessToken(String weixinCode) {

        Retrofit retrofit = new Retrofit.Builder()
                //注意，服务器主机应该以/结束，
                .baseUrl(WXBaseUrl)//设置服务器主机
                .addConverterFactory(GsonConverterFactory.create())//配置Gson作为json的解析器
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("appid", Constans.WX_APP_ID);
        map.put("secret", Constans.AppSecret);
        map.put("code", weixinCode);
        map.put("grant_type", "authorization_code");
        Call<WeiXIn> order = requestInterface.getAccess_token(map);
        order.enqueue(new Callback<WeiXIn>() {
            @Override
            public void onResponse(Call<WeiXIn> call, Response<WeiXIn> response) {
                WeiXIn weiXIn = response.body();
                String token = weiXIn.access_token;
                String openId = weiXIn.openid;
            }

            @Override
            public void onFailure(Call<WeiXIn> call, Throwable t) {
                LogUtil.i(t.getMessage());
            }
        });
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
}