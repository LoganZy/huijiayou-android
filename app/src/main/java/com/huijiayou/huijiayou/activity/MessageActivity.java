package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.MessageAdapter;
import com.huijiayou.huijiayou.adapter.MessageTransactionAdapter;
import com.huijiayou.huijiayou.bean.Message;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.fragment.HomeFragment;
import com.huijiayou.huijiayou.fragment.OrderFragment;
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

public class MessageActivity extends BaseActivity implements View.OnClickListener,NewHttpRequest.RequestCallback{

    @Bind(R.id.tv_activityMessage_all)
    TextView tv_activityMessage_all;

    @Bind(R.id.view_activityMessage_all)
    View view_activityMessage_all;

    @Bind(R.id.tv_activityMessage_transaction)
    TextView tv_activityMessage_transaction;

    @Bind(R.id.view_activityMessage_transaction)
    View view_activityMessage_transaction;

    @Bind(R.id.tv_activityMessage_activity)
    TextView tv_activityMessage_activity;

    @Bind(R.id.view_activityMessage_activity)
    View view_activityMessage_activity;

    @Bind(R.id.tv_activityMessage_system)
    TextView tv_activityMessage_system;

    @Bind(R.id.view_activityMessage_system)
    View view_activityMessage_system;

    @Bind(R.id.recyclerView_activityMessage_list)
    RecyclerView recyclerView_activityMessage_list;

    @Bind(R.id.tv_activityMessage_noData)
    TextView tv_activityMessage_noData;

    ArrayList<Message> messageArrayList;
    MessageAdapter messageAdapter;
    MessageTransactionAdapter messageTransactionAdapter;

    TextView lastSelectedTextView;
    View lastSelectedView;

    int page = 0;
    String type_all = "all";
    String type_system = "system";
    String type_activity = "activity";
    String type_transaction = "transaction";
    String type = type_all;

    int lstTaskId = 1;
    int markAllTaskId = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("消息中心");
        tvRight.setText("全部已读");
        tvRight.setTextColor(getResources().getColor(R.color.textColor_51586A));
        tvRight.setVisibility(View.VISIBLE);

        initView();
    }

    private void initView() {
        recyclerView_activityMessage_list.setLayoutManager(new LinearLayoutManager(this));
        lastSelectedTextView = tv_activityMessage_all;
        lastSelectedView = view_activityMessage_all;
    }

    @Override
    protected void onResume() {
        super.onResume();
        lst();
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
        lst();
    }

    private void lst(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        hashMap.put(Constans.USER_ID,userId);
        hashMap.put("mtype",type);
        hashMap.put("page",page);
        new NewHttpRequest(this, NetConfig.MESSAGE, NetConfig.message_lst, "jsonObject", lstTaskId, hashMap, true, this).executeTask();
    }

    private void markAll(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        hashMap.put(Constans.USER_ID,userId);
        hashMap.put("mtype",type);
        new NewHttpRequest(this, NetConfig.MESSAGE, NetConfig.message_markAll, "jsonObject", markAllTaskId, hashMap, true, this).executeTask();
    }


    @OnClick({R.id.tv_commonTitle_right,R.id.tv_activityMessage_all,
            R.id.tv_activityMessage_transaction,R.id.tv_activityMessage_system,
            R.id.tv_activityMessage_activity})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_commonTitle_right:
                markAll();
                break;
            case R.id.tv_activityMessage_all:
                page = 0;
                type = type_all;
                updateState(tv_activityMessage_all,view_activityMessage_all);
                break;
            case R.id.tv_activityMessage_transaction:
                page = 0;
                type = type_transaction;
                updateState(tv_activityMessage_transaction,view_activityMessage_transaction);
                break;
            case R.id.tv_activityMessage_activity:
                page = 0;
                type = type_activity;
                updateState(tv_activityMessage_activity,view_activityMessage_activity);
                break;
            case R.id.tv_activityMessage_system:
                page = 0;
                type = type_system;
                updateState(tv_activityMessage_system,view_activityMessage_system);
                break;
        }
    }

    @Override
    public void netWorkError() {

    }

    View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Message message = messageArrayList.get((Integer) v.getTag());
            Intent intent = new Intent(MessageActivity.this,MessageDetailActivity.class);
            intent.putExtra("message",message);
            startActivity(intent);
        }
    };

    View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Message message = messageArrayList.get((Integer) v.getTag());
            if ("1".equals(message.getJump_type())){ //去加油
                Intent intentHomeFragment = new Intent(MessageActivity.this, MainActivity.class);
                intentHomeFragment.putExtra("type", HomeFragment.TAG);
                startActivity(intentHomeFragment);
                MessageActivity.this.finish();

            }else if ("2".equals(message.getJump_type())){ // 查看订单列表
                Intent intentMainActivity = new Intent(MessageActivity.this, MainActivity.class);
                intentMainActivity.putExtra("type", OrderFragment.TAG);
                startActivity(intentMainActivity);
                MessageActivity.this.finish();

            }else if ("3".equals(message.getJump_type())){//查看订单详情  TODO


            }else if ("4".equals(message.getJump_type())){ //邀请好友
                startActivity(new Intent(MessageActivity.this, InvitationActivity.class));

            }
        }
    };

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            if (taskId == lstTaskId){
                messageArrayList = new Gson().fromJson(jsonObject.getJSONObject("data").get("lst").toString(),
                        new TypeToken<ArrayList<Message>>() {}.getType());
                if (type == type_all || type == type_system){
                    messageAdapter = new MessageAdapter(messageArrayList, this, onItemClick,type);
                    recyclerView_activityMessage_list.setAdapter(messageAdapter);
                }else if (type == type_transaction || type == type_activity){
                    messageTransactionAdapter = new MessageTransactionAdapter(messageArrayList, this, onBtnClick);
                    recyclerView_activityMessage_list.setAdapter(messageTransactionAdapter);
                }else{
                    messageAdapter = new MessageAdapter(messageArrayList, this, onItemClick);
                    recyclerView_activityMessage_list.setAdapter(messageAdapter);
                }
                if (messageArrayList == null || messageArrayList.size() == 0){
                    tv_activityMessage_noData.setVisibility(View.VISIBLE);
                    recyclerView_activityMessage_list.setVisibility(View.GONE);
                }else{
                    tv_activityMessage_noData.setVisibility(View.GONE);
                    recyclerView_activityMessage_list.setVisibility(View.VISIBLE);
                }
            }else if (taskId == markAllTaskId){
                lst();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createNormalToast(this,msg.getMessage());
    }
}
