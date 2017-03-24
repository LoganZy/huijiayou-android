package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.OilAdapter;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.fragment.HomeFragment;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OilActivity extends BaseActivity implements NewHttpRequest.RequestCallback,View.OnClickListener{

    @Bind(R.id.tv_activityOil_all)
    TextView tv_activityOil_all;

    @Bind(R.id.view_activityOil_all)
    View view_activityOil_all;

    @Bind(R.id.tv_activityOil_get)
    TextView tv_activityOil_get;

    @Bind(R.id.view_activityOil_get)
    View view_activityOil_get;

    @Bind(R.id.tv_activityOil_consume)
    TextView tv_activityOil_consume;

    @Bind(R.id.view_activityOil_consume)
    View view_activityOil_consume;

    @Bind(R.id.recyclerView_ActivityOil_list)
    RecyclerView recyclerView_ActivityOil_list;

    @Bind(R.id.btn_activityOil_jiayou)
    Button btn_activityOil_jiayou;

    @Bind(R.id.btn_activityOil_zanyoudi)
    Button btn_activityOil_zanyoudi;

    TextView lastSelectedTextView;
    View lastSelectedView;

    OilAdapter oilAdapter;
    ArrayList<OilAdapter.Oil> oilArrayList;
    int page = 1;
    String type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("油滴");

        lastSelectedTextView = tv_activityOil_all;
        lastSelectedView = view_activityOil_all;
        recyclerView_ActivityOil_list.setLayoutManager(new LinearLayoutManager(this));
        UserOildropInfo();
    }

    private void UserOildropInfo(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        hashMap.put(Constans.USER_ID,userId);
        hashMap.put("type",type); //0全部，1获取，2消耗
        hashMap.put("page",page);
        new NewHttpRequest(this, Constans.URL_wyh+Constans.ACCOUNT, Constans.UserOildropInfo, "jsonObject", 1, hashMap, true, this).executeTask();
    }

    private void updateState(TextView tv, View view){
        tv.setTextColor(getResources().getColor(R.color.orange_FF7320));
        view.setBackgroundColor(getResources().getColor(R.color.orange_FF7320));
        if (lastSelectedView != null && lastSelectedTextView != null){
            lastSelectedView.setBackgroundColor(getResources().getColor(R.color.line_color));
            lastSelectedTextView.setTextColor(getResources().getColor(R.color.textColor_51586A));
        }
        lastSelectedTextView = tv;
        lastSelectedView = view;
        UserOildropInfo();
    }

    @OnClick({R.id.tv_activityOil_all,R.id.tv_activityOil_get,R.id.tv_activityOil_consume,
            R.id.btn_activityOil_jiayou,R.id.btn_activityOil_zanyoudi})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_activityOil_all:
                type = "0";
                page = 1;
                updateState(tv_activityOil_all,view_activityOil_all);
                break;
            case R.id.tv_activityOil_get:
                type = "1";
                page = 1;
                updateState(tv_activityOil_get,view_activityOil_get);
                break;
            case R.id.tv_activityOil_consume:
                type = "2";
                page = 1;
                updateState(tv_activityOil_consume,view_activityOil_consume);
                break;
            case R.id.btn_activityOil_jiayou:
                Intent inten = new Intent(this,MainActivity.class);
                inten.putExtra("type", HomeFragment.TAG);
                startActivity(inten);
                finish();
                break;
            case R.id.btn_activityOil_zanyoudi:
                startActivity(new Intent(this,InvitationActivity.class));
                break;
        }
    }

    @Override
    public void netWorkError() {
    }
    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        if (taskId == 1){
            try {
                oilArrayList = new Gson().fromJson(jsonObject.get("list").toString(),
                        new TypeToken<ArrayList<OilAdapter.Oil>>() {}.getType());
                oilAdapter = new OilAdapter(oilArrayList,this);
                recyclerView_ActivityOil_list.setAdapter(oilAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == 1){
            ToastUtils.createNormalToast(this,msg.getMessage());
        }
    }


}
