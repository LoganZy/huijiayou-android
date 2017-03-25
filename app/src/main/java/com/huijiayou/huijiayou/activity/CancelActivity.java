package com.huijiayou.huijiayou.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CancelActivity extends BaseActivity {

    @Bind(R.id.bt_activityCancle_out)
    Button btActivityCancleOut;
    @Bind(R.id.tv_activityCancle_telephone)
    TextView tvActivityCancleTelephone;
    @Bind(R.id.tv_activityCancle_name)
    TextView tvActivityCancleName;
    @Bind(R.id.tv_activityCancle_version)
    TextView tvActivityCancleVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        ButterKnife.bind(this);
        initTitle();
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("设置");
        initData();
        initView();
    }

    private void initView() {
        btActivityCancleOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewHttpRequest(CancelActivity.this, Constans.URL_wyh + Constans.ACCOUNT, Constans.SIGNOUT, Constans.JSONOBJECT, 1, new NewHttpRequest.RequestCallback() {
                    @Override
                    public void netWorkError() {

                    }

                    @Override
                    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                        try {
                            String message =jsonObject.getString("message");
                            if (TextUtils.equals(message,"success")){
                                ToastUtils.createNormalToast("安全退出");
                                MyApplication.isLogin=false;
                               // PreferencesUtil.putPreferences(Constans.OPENID,"1");
                                //PreferencesUtil.putPreferences("sigincode",0);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void requestError(int code, MessageEntity msg, int taskId) {

                    }
                }).executeTask();


            }
        });
    }

    private void initData() {
        String telephone = PreferencesUtil.getPreferences("phone", "0");
        tvActivityCancleTelephone.setText(telephone);
        String nickName = PreferencesUtil.getPreferences(Constans.NICKNAME, "nickname");
        tvActivityCancleName.setText(nickName);
        //获取版本号

        // ---get the package info---
        PackageManager pm = getPackageManager();
        try {
            PackageInfo  pi = pm.getPackageInfo(getPackageName(), 0);
            String versionName = pi.versionName;
            tvActivityCancleVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("VersionInfo"+ "Exception"+ e);
        }
    }
}
