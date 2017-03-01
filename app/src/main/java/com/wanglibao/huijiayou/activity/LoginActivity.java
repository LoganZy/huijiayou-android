package com.wanglibao.huijiayou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanglibao.huijiayou.Bean.WeiXIn;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.config.Constans;
import com.wanglibao.huijiayou.request.RequestInterface;
import com.wanglibao.huijiayou.utils.LogUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends BaseActivity {
    private String weixinCode;
    private String WXBaseUrl = "https://api.weixin.qq.com/";
    // private static String get_access_token = "";
    // 获取第一步的code后，请求以下链接获取access_token
    //public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //获取用户个人信息
   // public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initTitle();
        tvTitle.setText("登录");
        initView();
    }

    private void initView() {
        final Button WXLogin = (Button) findViewById(R.id.WXLogin);
        WXLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WetLogin();
            }
        });
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
        Map<String,String> map = new LinkedHashMap<>();
        map.put("appid",Constans.WX_APP_ID);
        map.put("secret",Constans.AppSecret);
        map.put("code",weixinCode);
        map.put("grant_type","authorization_code");
        Call<WeiXIn> order = requestInterface.getAccess_token(map);
        order.enqueue(new Callback<WeiXIn>() {
            @Override
            public void onResponse(Call<WeiXIn> call, Response<WeiXIn> response) {
                WeiXIn weiXIn  = response.body();
                String token =  weiXIn.access_token;
                String openId = weiXIn.openid;
            }

            @Override
            public void onFailure(Call<WeiXIn> call, Throwable t) {
                LogUtil.i(t.getMessage());
            }
        });
    }
}