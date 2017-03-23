package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {

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
                ToastUtils.createNormalToast(FeedbackActivity.this,"接口还没好");
            }
        });
    }
}
