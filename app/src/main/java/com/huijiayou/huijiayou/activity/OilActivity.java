package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.OilAdapter;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
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

import static com.huijiayou.huijiayou.config.NetConfig.UserOildropInfo;

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

    @Bind(R.id.swipeRefreshLayout_ActivityOil_refresh)
    SwipeRefreshLayout swipeRefreshLayout_ActivityOil_refresh;

    LinearLayoutManager linearLayoutManager;
    TextView lastSelectedTextView;
    View lastSelectedView;

    OilAdapter oilAdapter;
    ArrayList<OilAdapter.Oil> oilArrayList;
    int page = 1;
    boolean isLoad = true;
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
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_ActivityOil_list.setLayoutManager(linearLayoutManager);
        recyclerView_ActivityOil_list.addOnScrollListener(new EndLessOnScrollListener());

        swipeRefreshLayout_ActivityOil_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLoad = true;
                UserOildropInfo(false);
            }
        });
        swipeRefreshLayout_ActivityOil_refresh.setRefreshing(true);
        UserOildropInfo(false);
    }

    private void UserOildropInfo(boolean isLoad){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        hashMap.put(Constans.USER_ID,userId);
        hashMap.put("type",type); //0全部，1获取，2消耗
        hashMap.put("page",page);
        new NewHttpRequest(this, NetConfig.ACCOUNT, UserOildropInfo, "jsonObject", 1, hashMap, isLoad, this).executeTask();
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
        UserOildropInfo(true);
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
                startActivity(new Intent(this,InvitationShareActivity.class));
                break;
        }
    }

    @Override
    public void netWorkError() {
    }
    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        if (taskId == 1){
            if (swipeRefreshLayout_ActivityOil_refresh.isRefreshing())
                swipeRefreshLayout_ActivityOil_refresh.setRefreshing(false);
            try {
                ArrayList<OilAdapter.Oil> oils = new Gson().fromJson(jsonObject.get("list").toString(),
                        new TypeToken<ArrayList<OilAdapter.Oil>>() {}.getType());
                if (page == 1){
                    oilArrayList = oils;
                    oilAdapter = new OilAdapter(oilArrayList,this);
                    recyclerView_ActivityOil_list.setAdapter(oilAdapter);
                }else{
                    oilArrayList.addAll(oils);
                    oilAdapter.notifyDataSetChanged();
                }
                if (oils == null || oils.size() < 20){
                    isLoad = false;
                }else{
                    isLoad = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == 1){
            if (swipeRefreshLayout_ActivityOil_refresh.isRefreshing())
                swipeRefreshLayout_ActivityOil_refresh.setRefreshing(false);
            ToastUtils.createNormalToast(this,msg.getMessage());
        }
    }


    public class EndLessOnScrollListener extends RecyclerView.OnScrollListener{

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //当前RecyclerView显示出来的最后一个的item的position
            int lastPosition = -1;

            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if(layoutManager instanceof GridLayoutManager){
                    //通过LayoutManager找到当前显示的最后的item的position
                    lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else if(layoutManager instanceof LinearLayoutManager){
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else if(layoutManager instanceof StaggeredGridLayoutManager){
                    //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                    //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    lastPosition = findMax(lastPositions);
                }

                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if(isLoad && lastPosition == recyclerView.getLayoutManager().getItemCount()-1){
                    page ++;
                    UserOildropInfo(true);
                }
            }
        }
        //找到数组中的最大值
        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }
}
