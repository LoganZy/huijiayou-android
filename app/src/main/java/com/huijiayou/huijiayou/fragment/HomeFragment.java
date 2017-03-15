package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.MainActivity;
import com.huijiayou.huijiayou.activity.PaymentActivity;
import com.huijiayou.huijiayou.adapter.CityAdapter;
import com.huijiayou.huijiayou.adapter.ProductAdapter;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    @Bind(R.id.rl_fragmentHome_region)
    RelativeLayout rl_fragmentHome_region;

    @Bind(R.id.imgBtn_fragmentHome_closeRegion)
    ImageButton imgBtn_fragmentHome_closeRegion;

    @Bind(R.id.recyclerView_fragmentHome_oil)
    RecyclerView recyclerView_fragmentHome_oil;

    @Bind(R.id.recyclerView_fragmentHome_region)
    RecyclerView recyclerView_fragmentHome_region;

    @Bind(R.id.tv_fragmentHome_botton)
    TextView tv_fragmentHome_botton;

    private int productListTaskId = 4;
    private int getCity = 5;

    ArrayList<ProductAdapter.Product> productArrayList = new ArrayList<>();
    ArrayList<CityAdapter.City> cityArrayList = new ArrayList<>();
    ProductAdapter productAdapter;
    CityAdapter cityAdapter;
    Drawable up,down;

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i("HomeFragment.onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        tv_fragmentHome_openRegionChoice.setOnClickListener(this);
        imgBtn_fragmentHome_message.setOnClickListener(this);
        tv_fragmentHome_addGasoline.setOnClickListener(this);
        imgBtn_fragmentHome_closeRegion.setOnClickListener(this);
        tv_fragmentHome_botton.setOnClickListener(this);

        getProductList();
        getCity();
    }

    private void getProductList(){
        HashMap<String,Object> hashMap = new HashMap<>();
        long time = System.currentTimeMillis();
        hashMap.put("time",time);
        hashMap.put("sign","");

        new NewHttpRequest(getActivity(), Constans.URL_zxg+Constans.OILCARD,Constans.productList,
                "jsonObject",productListTaskId,hashMap,false,this).executeTask();
    }
    private void getCity(){
        HashMap<String,Object> hashMap = new HashMap<>();
        long time = System.currentTimeMillis();
        hashMap.put("time",time);
        hashMap.put("sign","");

        new NewHttpRequest(getActivity(), Constans.URL_zxg+Constans.PRODUCT,Constans.getCity,
                "jsonObject",getCity,hashMap,false,this).executeTask();
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
                if (rl_fragmentHome_region.isShown()){
                    hideCover();
                }else{
                    showCover();
                }
                break;
            case R.id.imgBtn_fragmentHome_message:
                new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.LOGINSTATUS,"jsonObject",2,false,this).executeTask();
                break;
            case R.id.tv_fragmentHome_addGasoline:
                startActivity(new Intent(getActivity(), PaymentActivity.class));
                break;
            case R.id.imgBtn_fragmentHome_closeRegion:
            case R.id.tv_fragmentHome_botton:
                hideCover();
                break;
        }
    }

    public void showCover(){
        rl_fragmentHome_region.setVisibility(View.VISIBLE);
        mainActivity.showCover();
        if (down == null){
            down = getResources().getDrawable(R.mipmap.ic_navbar_arrow_down);
            down.setBounds(0,0,down.getMinimumWidth(),down.getMinimumHeight());
        }
        tv_fragmentHome_openRegionChoice.setCompoundDrawables(null,null,down,null);
    }

    public void hideCover(){
        rl_fragmentHome_region.setVisibility(View.GONE);
        mainActivity.hideCover();
        if (up == null){
            up = getResources().getDrawable(R.mipmap.ic_navbar_arrow_up);
            up.setBounds(0,0,up.getMinimumWidth(),up.getMinimumHeight());
        }
        tv_fragmentHome_openRegionChoice.setCompoundDrawables(null,null,up,null);
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            if (taskId == 0){
                JSONObject jsonObject1 = jsonObject.getJSONObject(Constans.DATA);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("username","13552408894");
                hashMap.put("sms_key",jsonObject1.getString("key"));
                hashMap.put("sms_code",jsonObject1.getString("code"));
                new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.SIGNIN,
                        "jsonObject",1,hashMap,false,this).executeTask();

            }else if (taskId == 1){
                ToastUtils.createLongToast(getActivity(),jsonObject.getString("message"));
            }else if (taskId == 2){
//                if(jsonObject.getInt("status") == 0){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("mobile","13552408894");
                    new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.MESSAGEAUTH,"jsonObject",0,hashMap,false,this).executeTask();
//                }else{
                    ToastUtils.createLongToast(getActivity(),"已登录");
//                }
            }else if (taskId == productListTaskId){
                productArrayList = new Gson().fromJson(jsonObject.getJSONArray("list").toString(),
                        new TypeToken<ArrayList<ProductAdapter.Product>>() {}.getType());
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView_fragmentHome_oil.setLayoutManager(linearLayoutManager);
                productAdapter = new ProductAdapter(getActivity(), productArrayList, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                recyclerView_fragmentHome_oil.setAdapter(productAdapter);
//                recyclerView_fragmentHome_oil.setOn
            }else if (taskId == getCity){
                cityArrayList = new Gson().fromJson(jsonObject.getJSONArray("list").toString(),
                        new TypeToken<ArrayList<CityAdapter.City>>() {}.getType());
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView_fragmentHome_region.setLayoutManager(linearLayoutManager);
                cityAdapter = new CityAdapter(getActivity(),cityArrayList);
                recyclerView_fragmentHome_region.setAdapter(cityAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createLongToast(getActivity(),msg.getMessage());
    }
}
