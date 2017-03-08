package com.wanglibao.huijiayou.activity;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.adapter.RecordAdapter;
import com.wanglibao.huijiayou.bean.Record;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordActivity extends BaseActivity {

    @Bind(R.id.tv_activityRecord_money)
    TextView tvActivityRecordMoney;
    @Bind(R.id.tv_activityRecord_cent)
    TextView tvActivityRecordCent;
    @Bind(R.id.lv_activity_record_bill)
    ListView lvActivityRecordBill;
    private List<Record> recordList ;
    private String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("加油记录");
        initData();
        initView();
    }

    private void initView() {

        RecordAdapter  recordAdapter = new RecordAdapter(RecordActivity.this,recordList);
        lvActivityRecordBill.setAdapter(recordAdapter);
    }


    private void initData() {
        //获取头部节省的钱数
        getSaveMoney();
        //获取列表需要展示的加油记录
        recordList = getRecord(Url);
    }

    private List<Record> getRecord(String url) {

        return null;
    }

    private void getSaveMoney() {

    }



}
