package com.huijiayou.huijiayou.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.WXBindActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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


/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    //定义一个过滤器；
    private IWXAPI api;
    private static String get_access_token = "";
/*    public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static String GetUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";*/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bundle b =  msg.getData();
                    id = b.getString(Constans.OPENID);
                    token = b.getString(Constans.ACCESSTOKEN);
                    unionid = b.getString(Constans.UNIONID);
                    nickname = b.getString(Constans.NICKNAME);
                    headimgurl = b.getString(Constans.HEADIMGURL);
                   // LogUtil.i(id +"+++++++++++++++++++++"+ token);
                    HashMap<String,Object> map =new HashMap<>();
                    map.put(Constans.ACCESSTOKEN, token);
                    map.put(Constans.OPENID, id);
                    new NewHttpRequest(WXEntryActivity.this, NetConfig.ACCOUNT, NetConfig.WEIXIN_AUTH_POST, Constans.JSONOBJECT,1, map, true, new NewHttpRequest.RequestCallback() {
                        @Override
                        public void netWorkError() {
                            ToastUtils.createNormalToast("链接失败");
                        }
                        @Override
                        public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                            switch (taskId){
                                case 1:

                                    try {
                                        // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                        int isbind = jsonObject.getInt("is_bind");
                                        LogUtil.i("++++++++++++"+isbind+"++++++++++++++++++++");
                                        if(isbind==1){
                                            String token = (String) jsonObject.get("token");
                                            String invite_code = (String) jsonObject.get("invite_code");
                                            String id= jsonObject.getString("id");
                                            String phone= jsonObject.getString("phone");
                                            PreferencesUtil.putPreferences(Constans.USER_INVITE_CODE, invite_code);
                                            PreferencesUtil.putPreferences(Constans.USER_PHONE, phone);
                                            PreferencesUtil.putPreferences(Constans.USER_TOKEN,token);
                                            PreferencesUtil.putPreferences(Constans.USER_ID,id);
                                            MyApplication.isLogin = true;
                                            PreferencesUtil.putPreferences(Constans.ISLOGIN,true);
                                           // ToastUtils.createNormalToast("账号已经绑定");
                                            //发送广播
                                            //startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                                            finish();

                                        }else if(isbind==0){

                                            Message msg = new Message();
                                            msg.what=2;
                                            handler.sendMessage(msg);
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

                    break;
                case 2:
                    Intent intent =new Intent();
                    //intent.setAction("getUserInfo");
                    intent.putExtra(Constans.UNIONID, unionid);
                    intent.putExtra(Constans.NICKNAME, nickname);
                    intent.putExtra(Constans.HEADIMGURL, headimgurl);
                    intent.setClass(WXEntryActivity.this,WXBindActivity.class);
                    startActivity(intent);
                    WXEntryActivity.this.finish();
                    break;

            }

            //

        }
    };
    private String id;
    private String token;
    private String nickname;
    private String unionid;
    private String headimgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }
//    微信请求第三方登录时，回回调该方法
    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }
//      微信返回给第三方的请求结果
    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        switch(baseResp.errCode) {


            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
                //ToastUtils.createNormalToast(this,result);
                //		      可用以下两种方法获得code
                //      resp.toBundle(bundle);
                //      Resp sp = new Resp(bundle);
                //      String code = sp.code;<span style="white-space:pre">
                //      或者

                //上面的code就是接入指南里要拿到的code
                //ToastUtils.createNormalToast("请求到code了");

                    String code = ((SendAuth.Resp) baseResp).code;
                   // String code = ((SendAuth.Resp) baseResp).code;
                    get_access_token = getCodeRequest(code);
                    Thread thread = new Thread(downloadRun);

                    thread.start();

      /*      try {
                    thread.join();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }*/

                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },6000);*/
                // finish();

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消发送";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                ToastUtils.createLongToast(this,result);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                ToastUtils.createLongToast(this,result);
                finish();
                break;
            default:
                result = "网络异常";
                ToastUtils.createLongToast(this,result);
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
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
                LogUtil.e("json------->"+josn);
                JSONObject json1 = new JSONObject(josn);
                    //String errcode = (String)json1.get("errcode");
                    String accessToken = (String) json1.get("access_token");
                    String openid = (String) json1.get("openid");
                    PreferencesUtil.putPreferences(Constans.ACCESSTOKEN, accessToken);
                    PreferencesUtil.putPreferences(Constans.OPENID, openid);
                    String get_user_info_url = getUserInfo(accessToken, openid);
                    WXGetUserInfo(get_user_info_url,accessToken,openid);


            } else {

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String getUserInfo(String access_token,String openid){
        String result = null;
        result= "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
        return result;
    }
    public static String getCodeRequest(String code) {


       String result =  "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constans.WX_APP_ID+"&secret="+Constans.AppSecret+"&code="+urlEnodeUTF8(code)+"&grant_type=authorization_code";

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
   * 获取用户的基本信息
   *
   * */
    private  void   WXGetUserInfo(String get_user_info_url,String accessToken,String weixinid){
        HttpClient get_user_info_httpClient = new DefaultHttpClient();
        String openid="";
        String nickname="";
        String headimgurl="";
        String unionid = "";
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
                unionid = json1.getString("unionid");
                PreferencesUtil.putPreferences(Constans.UNIONID,unionid);
                PreferencesUtil.putPreferences(Constans.NICKNAME,nickname);
                PreferencesUtil.putPreferences(Constans.HEADIMGURL,headimgurl);

                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString(Constans.OPENID,weixinid);
                b.putString(Constans.ACCESSTOKEN,accessToken);
                b.putString(Constans.UNIONID,unionid);
                b.putString(Constans.NICKNAME,nickname);
                b.putString(Constans.HEADIMGURL,headimgurl);
                msg.setData(b);
                msg.what=1;
                handler.sendMessage(msg);


                //sendBroadcast(intent);
                //handler.sendEmptyMessage(2);
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
    //微信登录
    public  Runnable downloadRun = new Runnable() {

        @Override
        public void run() {
            WXGetAccessToken();

        }
    };

}
