package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.PreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class SplashActivity extends BaseActivity implements NewHttpRequest.RequestCallback{

    @Bind(R.id.iv_welcome)
    ImageView iv_welcome;

    Handler handler = new Handler();
    boolean isGetLoginStatus = false;
    boolean isCountDown = false;

    int loginStatusTaskId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpha);

        handler.postDelayed(runnable,4000);
        new NewHttpRequest(this, Constans.URL_wyh+Constans.ACCOUNT,Constans.LOGINSTATUS,"jsonObject",loginStatusTaskId,false,this).executeTask();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isCountDown = true;
            if (isCountDown && isGetLoginStatus){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }
    };

    public void skip(View view){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        if (taskId == loginStatusTaskId){
            try {
                if(jsonObject.getInt("status") == 0){
                    MyApplication.isLogin = false;
                    PreferencesUtil.putPreferences(Constans.USER_ID,"");
                    PreferencesUtil.putPreferences(Constans.USER_TOKEN,"");
                    PreferencesUtil.putPreferences(Constans.USER_INVITE_CODE,"");
                    PreferencesUtil.putPreferences(Constans.USER_PHONE,"");
                    PreferencesUtil.putPreferences(Constans.ISLOGIN,false);
                }else{
                    MyApplication.isLogin = true;
                    PreferencesUtil.putPreferences(Constans.ISLOGIN,true);
                }
                isGetLoginStatus = true;
                if (isCountDown && isGetLoginStatus){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        isGetLoginStatus = true;
        if (isCountDown && isGetLoginStatus){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
