package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.bean.Message;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageDetailActivity extends BaseActivity {

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
        tv_ActivityMessageDetail_time.setText(simpleDateFormat1.format(message.getCreated_at()));
        tvTitle.setText(message.getTitle());
        tv_ActivityMessageDetail_content.setText(message.getContent());

    }
}
