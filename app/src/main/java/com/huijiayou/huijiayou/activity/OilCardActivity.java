package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.OilCardAdapter;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的油卡
 */
public class OilCardActivity extends BaseActivity implements NewHttpRequest.RequestCallback{

    @Bind(R.id.btn_activityOilCard_addOilCard)
    Button btn_activityOilCard_addOilCard;

    @Bind(R.id.recyclerView_activityOilCard_list)
    RecyclerView recyclerView_activityOilCard_list;

    private List<OilCardAdapter.OilCardEntity> oilCardEntityList = new ArrayList<>();
    private OilCardAdapter oilCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_card);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("我的油卡");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_activityOilCard_list.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOilCardList();
    }

    private void getOilCardList() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");
        new NewHttpRequest(this, Constans.URL_zxg+Constans.OILCARD, Constans.getOilCardList,
                "jsonObject", 1, hashMap,true, this).executeTask();
    }

    public void addOilCard(View view){
        startActivity(new Intent(this,AddOilCardActivity.class));
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        if (taskId == 1){
            try {
                oilCardEntityList = new Gson().fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<OilCardAdapter.OilCardEntity>>() {}.getType());
                oilCardAdapter = new OilCardAdapter(this,oilCardEntityList,OilCardAdapter.SHOWTYPE_MYOILCARD);
                recyclerView_activityOilCard_list.setAdapter(oilCardAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {

    }
}
