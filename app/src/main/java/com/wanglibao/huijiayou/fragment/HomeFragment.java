package com.wanglibao.huijiayou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.activity.PaymentActivity;
import com.wanglibao.huijiayou.config.Constans;
import com.wanglibao.huijiayou.net.MessageEntity;
import com.wanglibao.huijiayou.net.NewHttpRequest;
import com.wanglibao.huijiayou.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/2/24.
 */

public class HomeFragment extends Fragment implements View.OnClickListener,NewHttpRequest.RequestCallback{

    @Bind(R.id.tv_fragmentHome_openRegionChoice)
    TextView tv_fragmentHome_openRegionChoice;

    @Bind(R.id.imgBtn_fragmentHome_message)
    ImageButton imgBtn_fragmentHome_message;

    @Bind(R.id.tv_fragmentHome_addGasoline)
    TextView tv_fragmentHome_addGasoline;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        tv_fragmentHome_openRegionChoice.setOnClickListener(this);
        imgBtn_fragmentHome_message.setOnClickListener(this);
        tv_fragmentHome_addGasoline.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_fragmentHome_openRegionChoice:
                long currentTime = System.currentTimeMillis();
                String token = "token";
                test();
                break;
            case R.id.imgBtn_fragmentHome_message:

                break;
            case R.id.tv_fragmentHome_addGasoline:
                startActivity(new Intent(getActivity(), PaymentActivity.class));
                break;
        }
    }
    private int getMessageAuthTaskId = 1;
    private int loginTaskId = 2;
    private int productListTaskId = 3;

    private String key;
    private int code;
    private void test(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile","13552408894");
        new NewHttpRequest(getActivity(), Constans.URL+Constans.ACCOUNT,Constans.MESSAGEAUTH,"jsonObject",
                getMessageAuthTaskId,hashMap,false,this).executeTask();
    }

    @Override
    public void netWorkError() {
        LogUtil.i("netWorkError");
    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        if (taskId == getMessageAuthTaskId){
            try {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                key = (String) jsonObject1.get("key");
                code = (int) jsonObject1.get("code");
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("username","13552408894");
                hashMap.put("sms_key",key);
                hashMap.put("sms_code",code);

                new NewHttpRequest(getActivity(),Constans.URL+Constans.ACCOUNT,Constans.SIGNIN,"jsonObject",loginTaskId,
                        hashMap,false,this).executeTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (taskId == loginTaskId){


        }else if (taskId == productListTaskId){

        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        LogUtil.i("requestError");
    }
}
