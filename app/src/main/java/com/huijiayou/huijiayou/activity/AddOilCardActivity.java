package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


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
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oil_card);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("新增加油卡");

        type = getIntent().getStringExtra("type");
        edit_activityAddOilCard_inputCard.addTextChangedListener(new TextWatcher() {
            int beforeTextLength=0;
            int onTextLength=0;
            boolean isChanged = false;

            int location=0;//记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if(buffer.length()>0){
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if(s.charAt(i) == ' '){
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if(onTextLength == beforeTextLength || onTextLength <= 3 || isChanged){
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isChanged){
                    String text = s != null && s.length() > 0 ? s.toString().replace(" ","") : "";
                    String fristChar = s != null && s.length() > 0 ? s.toString().substring(0,1) : "";
                    if ("1".equals(fristChar) && text.length() == 19){
                        getOilCardInfo(text);
                    }else if ("9".equals(fristChar) && text.length() == 16){
                        getOilCardInfo(text);
                    }else{
                        hideUserNameBind("");
                    }
                    location = edit_activityAddOilCard_inputCard.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if(buffer.charAt(index) == ' '){
                            buffer.deleteCharAt(index);
                        }else{
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        //银行卡号的话需要改这里
                        if((index == 4 || index == 9 || index == 14 || index == 19 || index == 24)){
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if(konggeNumberC>konggeNumberB){
                        location+=(konggeNumberC-konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if(location>str.length()){
                        location = str.length();
                    }else if(location < 0){
                        location = 0;
                    }

                    edit_activityAddOilCard_inputCard.setText(str);
                    Editable etable = edit_activityAddOilCard_inputCard.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    public void bindCard(View view){
        String cord = edit_activityAddOilCard_inputCard.getText().toString().replace(" ","");
        if (cord != null && (cord.length() == 16 || cord.length() == 19)){
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("time",System.currentTimeMillis());
            hashMap.put("sign","");
            hashMap.put("oil_card",cord);

            new NewHttpRequest(this, NetConfig.OILCARD, NetConfig.bindCard, "jsonObject", bindCardTaskId, hashMap, true, this).executeTask();
        }else{
            ToastUtils.createLongToast(this,"请输入正确的油卡号");
        }
    }

    private void getOilCardInfo(String oil_card){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("sign","");

        hashMap.put("oil_card",oil_card);

        new NewHttpRequest(this, NetConfig.OILCARD, NetConfig.getOilCardInfo, "jsonObject", getOilCardInfoTaskId, hashMap, true, this).executeTask();
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
                if ("OilCard".equals(type)){
                    finish();
                }else if ("Payment".equals(type)){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    finish();
                }
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
