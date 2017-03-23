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
import com.huijiayou.huijiayou.bean.Message;
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

    ArrayList<Message> messageArrayList;
    MessageAdapter messageAdapter;

    TextView lastSelectedTextView;
    View lastSelectedView;

    int page = 1;
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
        tvRight.setVisibility(View.GONE);

        initView();
    }

    private void initView() {
        recyclerView_activityMessage_list.setLayoutManager(new LinearLayoutManager(this));
        lastSelectedTextView = tv_activityMessage_all;
        lastSelectedView = view_activityMessage_all;
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
        String userId = PreferencesUtil.getPreferences("user_id","");
        hashMap.put("user_id",userId);
        hashMap.put("mtype",type);
        hashMap.put("page",page);
        new NewHttpRequest(this, Constans.URL_MESSAGE, Constans.message_lst, "jsonObject", lstTaskId, hashMap, true, this).executeTask();
    }

    private void markAll(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences("user_id","");
        hashMap.put("user_id",userId);
        hashMap.put("mtype",type);
        new NewHttpRequest(this, Constans.URL_MESSAGE, Constans.message_markAll, "jsonObject", markAllTaskId, hashMap, true, this).executeTask();
    }


    @OnClick({R.id.tv_commonTitle_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_commonTitle_right:
                markAll();
                break;
            case R.id.tv_activityMessage_all:
                page = 1;
                type = type_all;
                updateState(tv_activityMessage_all,view_activityMessage_all);
                break;
            case R.id.tv_activityMessage_transaction:
                page = 1;
                type = type_transaction;
                updateState(tv_activityMessage_transaction,view_activityMessage_transaction);
                break;
            case R.id.tv_activityMessage_activity:
                page = 1;
                type = type_activity;
                updateState(tv_activityMessage_activity,view_activityMessage_activity);
                break;
            case R.id.tv_activityMessage_system:
                page = 1;
                type = type_system;
                updateState(tv_activityMessage_system,view_activityMessage_system);
                break;
        }
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            if (taskId == lstTaskId){
                messageArrayList = new Gson().fromJson(jsonObject.get("lst").toString(),
                        new TypeToken<ArrayList<Message>>() {}.getType());
                messageAdapter = new MessageAdapter(messageArrayList, this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message = messageArrayList.get((Integer) v.getTag());
                        Intent intent = new Intent(MessageActivity.this,MessageDetailActivity.class);
                        intent.putExtra("message",message);
                        startActivity(intent);
                    }}, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message = messageArrayList.get((Integer) v.getTag());
                        if ("1".equals(message.getJump_type())){ //去加油
                            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                            intent.putExtra("type", HomeFragment.TAG);
                            startActivity(intent);
                            finish();
                        }else if ("2".equals(message.getJump_type())){ // 查看订单详情 TODO

                        }else if ("3".equals(message.getJump_type())){//查看活动详情 h5  TODO

                        }else if ("4".equals(message.getJump_type())){ //邀请好友
                            Intent intent = new Intent(MessageActivity.this, InvitationActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                recyclerView_activityMessage_list.setAdapter(messageAdapter);
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
