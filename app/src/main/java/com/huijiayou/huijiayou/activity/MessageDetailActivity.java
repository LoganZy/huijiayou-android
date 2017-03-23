package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.bean.Message;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageDetailActivity extends BaseActivity implements NewHttpRequest.RequestCallback{

    @Bind(R.id.tv_ActivityMessageDetail_time)
    TextView tv_ActivityMessageDetail_time;

    @Bind(R.id.tv_ActivityMessageDetail_title)
    TextView tv_ActivityMessageDetail_title;

    @Bind(R.id.tv_ActivityMessageDetail_content)
    TextView tv_ActivityMessageDetail_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        initTitle();

        Intent intent = getIntent();
        Message message = (Message) intent.getSerializableExtra("message");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(Long.parseLong(message.getCreated_at()+"000"));
        tv_ActivityMessageDetail_time.setText(simpleDateFormat1.format(date));
        tvTitle.setText(message.getTitle());
        tv_ActivityMessageDetail_content.setText(message.getContent());

        if ("0".equals(message.getRead_status())){
            HashMap<String,Object> hashMap = new HashMap<>();
            String userId = PreferencesUtil.getPreferences("user_id","");
            hashMap.put("user_id",userId);
            hashMap.put("msg_id",message.getId());
            new NewHttpRequest(this, Constans.URL_MESSAGE, Constans.message_mark, "jsonObject", 1, hashMap, false, this).executeTask();
        }
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {

    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createNormalToast(msg.getMessage());
    }
}
