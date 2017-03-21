package com.huijiayou.huijiayou.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.CloseDealActivity;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.activity.WXBindActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.DialogLoading;
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
public class WXEntryActivity extends Activity implements IWXAPIEventHandler  {
    //定义一个过滤器；
    private IWXAPI api;
    private static String get_access_token = "";
    public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static String GetUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    public static BaseResp resp;
    private  boolean isBand = false ;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    // 这里的orderid是一个全局变量
                    //login(accessToken,openid);
                    Bundle bundle = msg.getData();
                    String accessToken = bundle.getString(Constans.ACCESSTOKEN);
                    String openid = bundle.getString(Constans.OPENID);
                    /*String get_user_info_url = getUserInfo(accessToken, openid);
                    WXGetUserInfo(get_user_info_url);*/
                    //ToastUtils.createNormalToast("accessToken="+accessToken+"openid"+openid);
                    login(openid,accessToken);
                    break;
            }
        }
    };
    private DialogLoading dialogLoading;

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
     /*   if (baseResp != null) {
            LoginActivity.resp= baseResp;
        }*/
    /*  if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("微信支付结果码:" + baseResp.errCode);
            builder.show();

            //请求服务器
        }*/
          /*  dialogLoading = new DialogLoading(this);
            dialogLoading.show();
            handler.postDelayed(new Runnable() {
             @Override
              public void run() {
                 dialogLoading.dismiss();
              }
            },6000);*/
        switch(baseResp.errCode) {


            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
                ToastUtils.createNormalToast(this,result);
                //		      可用以下两种方法获得code
                //      resp.toBundle(bundle);
                //      Resp sp = new Resp(bundle);
                //      String code = sp.code;<span style="white-space:pre">
                //      或者
                String code = ((SendAuth.Resp) baseResp).code;
                //上面的code就是接入指南里要拿到的code
                 String conde1= PreferencesUtil.getPreferences("CODE","1");
                if(TextUtils.equals(conde1,"1")){
                    PreferencesUtil.putPreferences("CODE",code);
                    get_access_token = getCodeRequest(code);
                }else if(TextUtils.equals(code,conde1)){
                    Intent intent =new Intent();
                    //intent.setAction("getUserInfo");
                    String unionid  = PreferencesUtil.getPreferences(Constans.UNIONID,"1");
                    String nickname = PreferencesUtil.getPreferences(Constans.NICKNAME,"1");
                    String headimgurl = PreferencesUtil.getPreferences(Constans.HEADIMGURL,"1");
                    intent.putExtra(Constans.UNIONID,unionid);
                    intent.putExtra(Constans.NICKNAME,nickname);
                    intent.putExtra(Constans.HEADIMGURL,headimgurl);
                    intent.setClass(this,WXBindActivity.class);
                    startActivity(intent);
                    //WXEntryActivity.this.finish();
                }



                    Thread thread=new Thread(downloadRun);

                    thread.start();
            try {
                    thread.join();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                finish();

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
                    //String get_user_info_url = getUserInfo(accessToken, openid);
                    //WXGetUserInfo(get_user_info_url);
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constans.ACCESSTOKEN,accessToken);
                    bundle.putString(Constans.OPENID,openid);
                    msg.setData(bundle);
                    msg.arg1 = 0;
                    handler.sendMessage(msg);



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
   * 获取用户的基本信息
   *
   * */
    private  void   WXGetUserInfo(String get_user_info_url){
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
                //发送广播
                Intent intent =new Intent();
                //intent.setAction("getUserInfo");
                intent.putExtra(Constans.UNIONID,unionid);
                intent.putExtra(Constans.NICKNAME,nickname);
                intent.putExtra(Constans.HEADIMGURL,headimgurl);
                intent.setClass(this,WXBindActivity.class);
                startActivity(intent);
                WXEntryActivity.this.finish();
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

    private void login(final String id,final String Token) {

        //请求服务器是否绑定
        HashMap<String, Object>  map = new HashMap<>();
        map.put("openid",id);
        map.put("access_token",Token);
        LogUtil.i("--------------"+id+"++++"+Token+"---------------");
        new NewHttpRequest(this, Constans.URL_wyh + Constans.ACCOUNT, Constans.WEIXIN_AUTH_POST, Constans.JSONOBJECT, 1, map, true, new NewHttpRequest.RequestCallback() {
            @Override
            public void netWorkError() {

            }
            @Override
            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                switch (taskId){
                    case 1:

                        try {
                           // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String isbind = jsonObject.getString("is_bind");
                            LogUtil.i("++++++++++++"+isbind+"++++++++++++++++++++");
                            if(TextUtils.equals("1",isbind)){
                                String token = (String) jsonObject.get("token");
                                PreferencesUtil.putPreferences("token",token);
                                ToastUtils.createLongToast(WXEntryActivity.this,"已经绑定");
                                String id= jsonObject.getString("id");
                                String weixin_unionid= jsonObject.getString("weixin_unionid");
                                String weixin_head = jsonObject.getString("weixin_head");
                                String weixin_name = jsonObject.getString("weixin_name");
                                PreferencesUtil.putPreferences("id",id);
                                PreferencesUtil.putPreferences(Constans.NICKNAME,weixin_name);
                                PreferencesUtil.putPreferences(Constans.HEADIMGURL,weixin_head);
                            }else if(TextUtils.equals("0",isbind)){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String get_user_info_url=getUserInfo(Token,id);
                                        WXGetUserInfo(get_user_info_url);
                                    }
                                }).start();
                                String message =  jsonObject.getString("msg");
                                ToastUtils.createNormalToast(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }

            @Override
            public void requestError(int code, MessageEntity msg, int taskId) {

            }
        }).executeTask();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String get_user_info_url=getUserInfo(Token,id);
                WXGetUserInfo(get_user_info_url);
            }
        }).start();
    }

}
