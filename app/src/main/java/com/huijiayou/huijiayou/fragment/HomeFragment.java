package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.huijiayou.huijiayou.activity.PaymentActivity;
import com.huijiayou.huijiayou.adapter.CityAdapter;
import com.huijiayou.huijiayou.adapter.CityAdapter.City;
import com.huijiayou.huijiayou.adapter.HomePageAdapter;
import com.huijiayou.huijiayou.adapter.ProductAdapter;
import com.huijiayou.huijiayou.adapter.ProductAdapter.Product;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.CommitUtils;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.huijiayou.huijiayou.widget.RechargeDetailsDialog;
import com.huijiayou.huijiayou.wxapi.ShareUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
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

    public static final String TAG = "HomeFragment";

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

    @Bind(R.id.tv_fragmentHome_money_month)
    TextView tv_fragmentHome_money_month; //一个月多少钱

    @Bind(R.id.tv_fragmentHome_month)
    TextView tv_fragmentHome_month; //分几月支付

    @Bind(R.id.imgBtn_fragmentHome_info)
    ImageButton imgBtn_fragmentHome_info; //点击查看明细

    @Bind(R.id.tv_fragmentHome_price)
    TextView tv_fragmentHome_price; //充值金额

    @Bind(R.id.tv_fragmentHome_rmb)
    TextView tv_fragmentHome_rmb; //上面的节省金额

    @Bind(R.id.tv_fragmentHome_discountAmount)
    TextView tv_fragmentHome_discountAmount; //打折后的金额

    @Bind(R.id.tv_fragmentHome_saveAmount)
    TextView tv_fragmentHome_saveAmount; //节省的金额


    @Bind(R.id.tv_fragmentHomeMmoney_100)
    TextView tv_fragmentHomeMmoney_100;
    @Bind(R.id.tv_fragmentHomeMmoney_200)
    TextView tv_fragmentHomeMmoney_200;
    @Bind(R.id.tv_fragmentHomeMmoney_500)
    TextView tv_fragmentHomeMmoney_500;
    @Bind(R.id.tv_fragmentHomeMmoney_1000)
    TextView tv_fragmentHomeMmoney_1000;
    @Bind(R.id.tv_fragmentHomeMmoney_2000)
    TextView tv_fragmentHomeMmoney_2000;
    @Bind(R.id.tv_fragmentHomeMmoney_3000)
    TextView tv_fragmentHomeMmoney_3000;
    TextView currentSelectedTextView;

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
    HomePageAdapter.Product currentProduct = new HomePageAdapter.Product();

    MainActivity mainActivity;

    public AnimationDrawable animationDrawable;

    double total,saveMoney,discountTotal;
    int moneyMonth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        addOnClickListener(tv_fragmentHome_openRegionChoice,imgBtn_fragmentHome_message,tv_fragmentHome_addGasoline,imgBtn_fragmentHome_closeRegion,
                tv_fragmentHome_botton,tv_fragmentHomeMmoney_100,tv_fragmentHomeMmoney_200,tv_fragmentHomeMmoney_500,
                tv_fragmentHomeMmoney_1000,tv_fragmentHomeMmoney_2000,tv_fragmentHomeMmoney_3000,imgBtn_fragmentHome_info);

        animationDrawable = (AnimationDrawable) imgBtn_fragmentHome_message.getBackground();

        viewPager_fragmentHome_product.setPageMargin(50);
        viewPager_fragmentHome_product.setOffscreenPageLimit(3);
        viewPager_fragmentHome_product.setPageTransformer(true, new
                ScaleInTransformer());
        viewPager_fragmentHome_product.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                currentProduct = homeProductArrayList.get(position);
                calculation(moneyMonth);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
//            HashMap<String,Object> hashMap = new HashMap<>();
//            hashMap.put("mobile","12000000001");
//            new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.MESSAGEAUTH,"jsonObject",0,hashMap,true,this).executeTask();
        linearLayoutManagerCity = new LinearLayoutManager(getActivity());
        recyclerView_fragmentHome_city.setLayoutManager(linearLayoutManagerCity);
        linearLayoutManagerProduct = new LinearLayoutManager(getActivity());
        recyclerView_fragmentHome_product.setLayoutManager(linearLayoutManagerProduct);
        getCity();
    }

    private void addOnClickListener(View... views){
        for (int i = 0; i < views.length; i++){
            views[i].setOnClickListener(this);
        }
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
                "jsonObject",productListTaskId,hashMap,true,this).executeTask();
    }
    private void getCity(){
        HashMap<String,Object> hashMap = new HashMap<>();
        long time = System.currentTimeMillis();
        hashMap.put("time",time);
        hashMap.put("sign","");

        new NewHttpRequest(getActivity(), Constans.URL_zxg+Constans.PRODUCT,Constans.getCity,
                "jsonObject",getCityTaskId,hashMap,true,this).executeTask();
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
                animationDrawable.stop();
//                startActivity(new Intent(getActivity(), MessageActivity.class));
                ShareUtil  shareUtil = new ShareUtil(getActivity(), "", "", "http://clerkkent.duapp.com/HJY/#/game/main", "", new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        ToastUtils.createNormalToast(getActivity(),share_media.toString());
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtils.createNormalToast(getActivity(),share_media.toString());
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtils.createNormalToast(getActivity(),share_media.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ToastUtils.createNormalToast(getActivity(),share_media.toString());
                    }
                });
                shareUtil.showShareDialog();
                break;
            case R.id.tv_fragmentHome_addGasoline:
                Intent intent = new Intent(new Intent(getActivity(), PaymentActivity.class));
                intent.putExtra("moneyMonth",moneyMonth);
                if (!TextUtils.isEmpty(currentProduct.getId())){
                    intent.putExtra("product_id",currentProduct.getId());
                    intent.putExtra("month",currentProduct.getProduct_time());
                }
                intent.putExtra("total",total);
                intent.putExtra("discountTotal",discountTotal);
                intent.putExtra("saveMoney",saveMoney);
                startActivity(intent);
                break;
            case R.id.imgBtn_fragmentHome_closeRegion:
            case R.id.tv_fragmentHome_botton:
                hideCover();
                break;
            case R.id.tv_fragmentHomeMmoney_100:
                calculation(100);
                textViewChecked(tv_fragmentHomeMmoney_100);
                break;
            case R.id.tv_fragmentHomeMmoney_200:
                calculation(200);
                textViewChecked(tv_fragmentHomeMmoney_200);
                break;
            case R.id.tv_fragmentHomeMmoney_500:
                calculation(500);
                textViewChecked(tv_fragmentHomeMmoney_500);
                break;
            case R.id.tv_fragmentHomeMmoney_1000:
                calculation(1000);
                textViewChecked(tv_fragmentHomeMmoney_1000);
                break;
            case R.id.tv_fragmentHomeMmoney_2000:
                calculation(2000);
                textViewChecked(tv_fragmentHomeMmoney_2000);
                break;
            case R.id.tv_fragmentHomeMmoney_3000:
                calculation(3000);
                textViewChecked(tv_fragmentHomeMmoney_3000);
                break;
            case R.id.imgBtn_fragmentHome_info:
                if (!TextUtils.isEmpty(currentProduct.getProduct_time())){
                    new RechargeDetailsDialog(getActivity(), moneyMonth, Integer.parseInt(currentProduct.getProduct_time())).create();
                }
                break;
        }
    }

    /**
     * 根据选择的月数和金额计算总价 折后金额 节省金额
     * @param moneyMonth 每个月多少钱
     */
    private void calculation(int moneyMonth){
        this.moneyMonth = moneyMonth;
        if (!TextUtils.isEmpty(currentProduct.getId())){
            int month = Integer.parseInt(currentProduct.getProduct_time());
            double discount = Double.parseDouble(currentProduct.getProduct_discount());
            total = moneyMonth * month;
            saveMoney = total - total * discount;
            discountTotal = total - saveMoney;
            saveMoney = CommitUtils.bigDecimal2(saveMoney);
            discountTotal = CommitUtils.bigDecimal2(discountTotal);
            tv_fragmentHome_money_month.setText(moneyMonth+"");
            tv_fragmentHome_month.setText(month+"");
            tv_fragmentHome_price.setText("充值金额:"+total);


            tv_fragmentHome_rmb.setText(Html.fromHtml("节省:<font color='#FF7320'>"+saveMoney+"</font>元"));
            tv_fragmentHome_saveAmount.setText(saveMoney+"");
            tv_fragmentHome_discountAmount.setText(discountTotal+"");

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

    private void textViewChecked(TextView view){
        if (currentSelectedTextView != null){
            currentSelectedTextView.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_home_money_n));
            currentSelectedTextView.setTextColor(getResources().getColor(R.color.orange_FF7320));
        }
        view.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_home_money_h));
        view.setTextColor(getResources().getColor(R.color.white));
        currentSelectedTextView = view;
    }

    @Override
    public void netWorkError() {}

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            /*if (taskId == 0){
                JSONObject jsonObject1 = jsonObject.getJSONObject(Constans.DATA);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("username","12000000001");
                hashMap.put("sms_key",jsonObject1.getString("key"));
                hashMap.put("sms_code",jsonObject1.getString("code"));
                new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.SIGNIN,
                        "jsonObject",1,hashMap,true,this).executeTask();

            }else if (taskId == 1){
                ToastUtils.createLongToast(getActivity(),jsonObject.getString("message"));
                JSONObject jsonObject1 = jsonObject.getJSONObject(Constans.DATA);
                String token = (String) jsonObject1.get("token");
                String user_id = (String) jsonObject1.get("id");
                PreferencesUtil.putPreferences("token",token);
                PreferencesUtil.putPreferences("user_id",user_id);
                MyApplication.isLogin = true;
            }else if (taskId == 2){
                if(jsonObject.getInt("status") == 0){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("mobile","12000000001");
                    new NewHttpRequest(getActivity(),Constans.URL_wyh+Constans.ACCOUNT,Constans.MESSAGEAUTH,"jsonObject",0,hashMap,true,this).executeTask();
                }else{
                    ToastUtils.createLongToast(getActivity(),"已登录");
                }
            }else */ if (taskId == getCityTaskId){

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
                temporarySelectedProduct = productArrayList.get(0);
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
                    lastSelectedCity = null;
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
                if (homeProductArrayList != null && homeProductArrayList.size() > 0)
                currentProduct = homeProductArrayList.get(0);
                calculation(500);
                textViewChecked(tv_fragmentHomeMmoney_500);
            }else if (taskId == 10){
                ToastUtils.createNormalToast(getActivity(),jsonObject.toString());
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
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createLongToast(getActivity(),msg.getMessage());
    }
}
