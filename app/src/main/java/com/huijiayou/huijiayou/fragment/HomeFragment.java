package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.huijiayou.huijiayou.activity.OilCardActivity;
import com.huijiayou.huijiayou.activity.PaymentActivity;
import com.huijiayou.huijiayou.adapter.CityAdapter;
import com.huijiayou.huijiayou.adapter.CityAdapter.City;
import com.huijiayou.huijiayou.adapter.HomePageAdapter;
import com.huijiayou.huijiayou.adapter.ProductAdapter;
import com.huijiayou.huijiayou.adapter.ProductAdapter.Product;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    @Bind(R.id.recyclerView_fragmentHome_product)
    RecyclerView recyclerView_fragmentHome_product;

    @Bind(R.id.recyclerView_fragmentHome_city)
    RecyclerView recyclerView_fragmentHome_city;

    @Bind(R.id.tv_fragmentHome_botton)
    TextView tv_fragmentHome_botton;

    @Bind(R.id.viewPager_fragmentHome_product)
    ViewPager viewPager_fragmentHome_product;

    private int productListTaskId = 4;
    private int getCityTaskId = 5;

    ArrayList<ProductAdapter.Product> productArrayList = new ArrayList<>();
    ArrayList<CityAdapter.City> cityTotalArrayList = new ArrayList<>();
    ArrayList<CityAdapter.City> cityCurrentArrayList = new ArrayList<>();
    ProductAdapter productAdapter;
    CityAdapter cityAdapter;
    Drawable up,down;
    public TextView lastSelectedProductTextView,lastSelectedCityTextView;
    public City lastSelectedCity = new City();
    public Product lastSelectedProduct = new Product();
    public Product temporarySelectedProduct = new Product();

    final int productCityShowType_complete = 2;//有产品 城市
    final int productCityShowType_noCity = 1;//有产品 但没城市
    final int productCityShowType_noProductCity = 0;//产品只有一个，而且没有城市
    int productCityShowType = productCityShowType_complete;

    LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getActivity());
    LinearLayoutManager linearLayoutManagerCity = new LinearLayoutManager(getActivity());

    HomePageAdapter homePageAdapter;
    ArrayList<HomePageAdapter.Product> homeProductArrayList = new ArrayList<>();

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

        viewPager_fragmentHome_product.setPageMargin(50);
        viewPager_fragmentHome_product.setOffscreenPageLimit(3);
        viewPager_fragmentHome_product.setPageTransformer(true, new
                ScaleInTransformer());

       //new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.LOGINSTATUS,"jsonObject",2,false,this).executeTask();
        linearLayoutManagerCity = new LinearLayoutManager(getActivity());
        recyclerView_fragmentHome_city.setLayoutManager(linearLayoutManagerCity);
        linearLayoutManagerProduct = new LinearLayoutManager(getActivity());
        recyclerView_fragmentHome_product.setLayoutManager(linearLayoutManagerProduct);
        getCity();
    }

    private void getProductList(){
        HashMap<String,Object> hashMap = new HashMap<>();
        long time = System.currentTimeMillis();
        hashMap.put("time",time);
        hashMap.put("sign","");
        hashMap.put("belong",lastSelectedProduct.getBelong());
        if (lastSelectedCity != null && !TextUtils.isEmpty(lastSelectedCity.getCity_id())){
            hashMap.put("city_id",lastSelectedCity.getCity_id());
        }
        new NewHttpRequest(getActivity(), Constans.URL_zxg+Constans.OILCARD,Constans.productList,
                "jsonObject",productListTaskId,hashMap,false,this).executeTask();
    }
    private void getCity(){
        HashMap<String,Object> hashMap = new HashMap<>();
        long time = System.currentTimeMillis();
        hashMap.put("time",time);
        hashMap.put("sign","");

        new NewHttpRequest(getActivity(), Constans.URL_zxg+Constans.PRODUCT,Constans.getCity,
                "jsonObject",getCityTaskId,hashMap,false,this).executeTask();
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
//                new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.LOGINSTATUS,"jsonObject",2,false,this).executeTask();
                startActivity(new Intent(getActivity(), OilCardActivity.class));
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
    public void netWorkError() {}

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
                if(jsonObject.getInt("status") == 0){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("mobile","13552408894");
                    new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.MESSAGEAUTH,"jsonObject",0,hashMap,false,this).executeTask();
                }else{
                    ToastUtils.createLongToast(getActivity(),"已登录");
                }
            }else if (taskId == getCityTaskId){
                cityTotalArrayList = new Gson().fromJson(jsonObject.getJSONArray("list").toString(),
                        new TypeToken<ArrayList<CityAdapter.City>>() {}.getType());
                initProductCityData(cityTotalArrayList); //将获取到的数据拆分成两部分
                productAdapter = new ProductAdapter(getActivity(), productArrayList, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        temporarySelectedProduct = productArrayList.get(position);
                        updateCityData(position);
                        if (productCityShowType == productCityShowType_noCity){
                            lastSelectedProduct = temporarySelectedProduct;
                            lastSelectedCity = null;
                            initTextProductCity(lastSelectedProduct.getName(),"全国");
                            hideCover();
                            getProductList();
                        }
                        if (lastSelectedProductTextView != null){
                            lastSelectedProductTextView.setTextColor(getResources().getColor(R.color.textColor_51586A));
                            lastSelectedProductTextView.getTextColors();
                        }
                        lastSelectedProductTextView = (TextView) v;
                        lastSelectedProductTextView.setTextColor(getResources().getColor(R.color.textColor_F3844A));
                    }
                },this);
                recyclerView_fragmentHome_product.setAdapter(productAdapter);
                lastSelectedProduct = productArrayList.get(0);
                updateCityData(0);
                if (productArrayList.size() == 1 && cityCurrentArrayList.size() == 0){
                    productCityShowType = productCityShowType_noProductCity;
                    tv_fragmentHome_openRegionChoice.setOnClickListener(null);
                    lastSelectedProduct = productArrayList.get(0);
                    lastSelectedCity = null;
                    initTextProductCity(lastSelectedProduct.getName(),"全国");
                }else if (cityCurrentArrayList == null || cityCurrentArrayList.size() == 0){
                    initTextProductCity(lastSelectedProduct.getName(),"全国");
                }else {
                    lastSelectedCity = cityCurrentArrayList.get(0);
                    initTextProductCity(lastSelectedProduct.getName(),lastSelectedCity.getName());
                }
                getProductList();
            }else if (taskId == productListTaskId){
                homeProductArrayList = new Gson().fromJson(jsonObject.getJSONArray("list").toString(),
                        new TypeToken<ArrayList<HomePageAdapter.Product>>() {}.getType());
                homePageAdapter = new HomePageAdapter(homeProductArrayList,getActivity());
                viewPager_fragmentHome_product.setAdapter(homePageAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initProductCityData(ArrayList<City> cityArrayList){
        ArrayList<City> cityes = new ArrayList<>();
        Product product = null;
        HashMap<String,String> hashMap = new HashMap<>();
        for (int i = 0; i < cityArrayList.size(); i++){
            City city = cityArrayList.get(i);
            String name = city.getName();
            int index = name.indexOf("|");
            if (index >= 0){
                hashMap.put(name.substring(0,index),city.getBelong());
                city.setName(name.substring(index+1));
                cityes.add(city);
            }
        }
        cityTotalArrayList = cityes;
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        productArrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            product = new Product();
            product.setName(entry.getKey());
            product.setBelong(entry.getValue());
            productArrayList.add(product);
        }
    }

    public void initTextProductCity(String product,String city){
        tv_fragmentHome_openRegionChoice.setText(product+" | "+ city);
    }

    private void updateCityData(int position){
        cityCurrentArrayList = CityAdapter.findCityByBelong(productArrayList.get(position).getBelong(),cityTotalArrayList);
        if (cityCurrentArrayList.size() == 0){
            recyclerView_fragmentHome_city.setVisibility(View.GONE);
            productCityShowType = productCityShowType_noCity;
        }else{
            recyclerView_fragmentHome_city.setVisibility(View.VISIBLE);
            productCityShowType = productCityShowType_complete;
        }
//        if (cityAdapter == null){
        cityAdapter = new CityAdapter(getActivity(), cityCurrentArrayList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                lastSelectedCity = cityCurrentArrayList.get(position);
                lastSelectedProduct = temporarySelectedProduct;
                initTextProductCity(lastSelectedProduct.getName(),lastSelectedCity.getName());
                hideCover();
                if (lastSelectedCityTextView != null){
                    lastSelectedCityTextView.setTextColor(getResources().getColor(R.color.textColor_51586A));
                }
                lastSelectedCityTextView = (TextView) v;
                lastSelectedCityTextView.setTextColor(getResources().getColor(R.color.textColor_F3844A));

                getProductList();
            }
        },this);
        recyclerView_fragmentHome_city.setAdapter(cityAdapter);
//        }else {
//            cityAdapter.notifyDataSetChanged();
//            recyclerView_fragmentHome_city.setAdapter(cityAdapter);
//        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createLongToast(getActivity(),msg.getMessage());
    }


    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MAX_SCALE = 1.2f;
        private static final float MIN_SCALE = 1.0f;//0.85f

        @Override
        public void transformPage(View view, float position) {
            //setScaleY只支持api11以上
            if (position < -1){
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
//              Log.e("TAG", view + " , " + position + "");
                float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(MAX_SCALE-MIN_SCALE);
                view.setScaleX(scaleFactor);
                //每次滑动后进行微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
                if(position>0){
                    view.setTranslationX(-scaleFactor*2);
                }else if(position<0){
                    view.setTranslationX(scaleFactor*2);
                }
                view.setScaleY(scaleFactor);

            } else
            { // (1,+Infinity]

                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);

            }
        }

    }
}
