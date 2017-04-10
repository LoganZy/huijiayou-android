package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity implements NewHttpRequest.RequestCallback{

    @Bind(R.id.edit_activityFeeback_input)
    EditText edit_activityFeeback_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("意见反馈");
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edit_activityFeeback_input.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    HashMap<String,Object> hashMap = new HashMap<String, Object>();
                    String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
                    hashMap.put("user_id",userId);
                    hashMap.put("concent",text);
                    new NewHttpRequest(FeedbackActivity.this, NetConfig.ACCOUNT, NetConfig.feedBackForUser, "jsonObject", 1,hashMap ,true, FeedbackActivity.this).executeTask();
                }else{
                    ToastUtils.createNormalToast(FeedbackActivity.this,"请输入反馈内容");
                }
            }
        });
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        ToastUtils.createNormalToast(this,"建议已提交,感谢您的使用");
        finish();
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createNormalToast(this,msg.getMessage());
    }
}
