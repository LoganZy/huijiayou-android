package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.huijiayou.huijiayou.config.Constans.getOilCardInfo;

public class AddOilCardActivity extends BaseActivity implements NewHttpRequest.RequestCallback{

    @Bind(R.id.edit_activityAddOilCard_inputCard)
    EditText edit_activityAddOilCard_inputCard;

    @Bind(R.id.rl_activityAddOilCard_name)
    RelativeLayout rl_activityAddOilCard_name;

    @Bind(R.id.tv_activityAddOilCard_name)
    TextView tv_activityAddOilCard_name;

    @Bind(R.id.tv_activityAddOilCard_cardPrompt)
    TextView tv_activityAddOilCard_cardPrompt;

    @Bind(R.id.btn_activityAddOilCard_add)
    Button btn_activityAddOilCard_add;

    int getOilCardInfoTaskId = 1;
    int bindCardTaskId = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oil_card);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("新增加油卡");

        edit_activityAddOilCard_inputCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && (s.length() == 16 || s.length() == 19)){
                    getOilCardInfo(s.toString());
                }else{
                    hideUserNameBind("");
                }
            }
        });
    }

    public void bindCard(View view){
        String cord = edit_activityAddOilCard_inputCard.getText().toString();
        if (cord != null && (cord.length() == 16 || cord.length() == 19)){
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("time",System.currentTimeMillis());
            hashMap.put("sign","");
            hashMap.put("oil_card",cord);

            new NewHttpRequest(this, Constans.URL_zxg+Constans.OILCARD, Constans.bindCard, "jsonObject", bindCardTaskId, hashMap, true, this).executeTask();
        }else{
            ToastUtils.createLongToast(this,"请输入正确的油卡号");
        }
    }

    private void getOilCardInfo(String oil_card){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");

        hashMap.put("oil_card",oil_card);

        new NewHttpRequest(this, Constans.URL_zxg+Constans.OILCARD, getOilCardInfo, "jsonObject", getOilCardInfoTaskId, hashMap, true, this).executeTask();
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            if (taskId == getOilCardInfoTaskId){
                tv_activityAddOilCard_cardPrompt.setText("");
                tv_activityAddOilCard_cardPrompt.setVisibility(View.INVISIBLE);
                String username = jsonObject.getString("username");
                showUserNameBind(username);
            }else if (taskId == bindCardTaskId){
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == getOilCardInfoTaskId){
            hideUserNameBind(msg.getMessage());
        }else if (taskId == bindCardTaskId){
            tv_activityAddOilCard_cardPrompt.setText(msg.getMessage());
            tv_activityAddOilCard_cardPrompt.setVisibility(View.VISIBLE);
        }
    }

    private void hideUserNameBind(String errorMsg){
        tv_activityAddOilCard_cardPrompt.setText(errorMsg);
        tv_activityAddOilCard_cardPrompt.setVisibility(View.VISIBLE);
        tv_activityAddOilCard_name.setText("");
        rl_activityAddOilCard_name.setVisibility(View.GONE);
        btn_activityAddOilCard_add.setVisibility(View.GONE);
    }

    private void showUserNameBind(String userName){
        tv_activityAddOilCard_cardPrompt.setText("");
        tv_activityAddOilCard_cardPrompt.setVisibility(View.INVISIBLE);
        tv_activityAddOilCard_name.setText(userName);
        rl_activityAddOilCard_name.setVisibility(View.VISIBLE);
        btn_activityAddOilCard_add.setVisibility(View.VISIBLE);
    }

}
