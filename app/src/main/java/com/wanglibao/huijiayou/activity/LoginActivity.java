package com.wanglibao.huijiayou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.config.Constans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class LoginActivity extends BaseActivity {
    // public static BaseResp resp;
    // public static IWXAPI WXapi;
    //public static String WX_APP_ID = "wx9bcf508fbe5af427";
    //public static String AppSecret = "a33465db152afd3bdd86c2fb38b7712b";
    private String weixinCode;
    private final static int LOGIN_WHAT_INIT = 1;
    private static String get_access_token = "";
    // 获取第一步的code后，请求以下链接获取access_token
    public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //获取用户个人信息
    public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

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
            /*
			 * 将你前面得到的AppID、AppSecret、code，拼接成URL
			 */
            get_access_token = getCodeRequest(weixinCode);
            Thread thread = new Thread(downloadRun);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /*
    * 获取 token的URL的拼接
    *
    * */
    private String getCodeRequest(String weixinCode)  {
        String result = null;
        try {
            GetCodeRequest = GetCodeRequest.replace("APPID", URLEncoder.encode(Constans.WX_APP_ID,"UTF8")
                    );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            GetCodeRequest = GetCodeRequest.replace("SECRET",
                    URLEncoder.encode(Constans.AppSecret,"UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            GetCodeRequest = GetCodeRequest.replace("CODE",URLEncoder.encode(weixinCode,"UTF8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = GetCodeRequest;
        return result;
    }

    public  Runnable downloadRun = new Runnable() {

        @Override
        public void run() {
            //获取token的方法
            WXGetAccessToken();

        }
    };
    /*
    * 获取微信的token等信息
    *
    * */
    private void WXGetAccessToken() {

    }
}