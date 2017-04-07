package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.CloseDealActivity;
import com.huijiayou.huijiayou.activity.DetailsActivity;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.activity.MainActivity;
import com.huijiayou.huijiayou.activity.NoPayActivity;
import com.huijiayou.huijiayou.activity.PayingActivity;
import com.huijiayou.huijiayou.adapter.RecordAdapter;
import com.huijiayou.huijiayou.bean.Record;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.huijiayou.huijiayou.widget.LoadingHeader;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import retrofit2.Response;

/**
 * Created by lugg on 2017/2/24.
 */
public class OrderFragment extends Fragment{
    public static final String TAG = "OrderFragment";

    @Bind(R.id.bt_fragment_order_login)
    Button btFragmentGasLogin;
    @Bind(R.id.bt_fragment_gas_pay)
    Button btFragmentGasPay;
    @Bind(R.id.tv_activityRecord_money1)
    TextView tvActivityRecordMoney1;
    @Bind(R.id.tv_activityRecord_cent)
    TextView tvActivityRecordCent;
    @Bind(R.id.lv_activity_record_bill)
    ListView lvActivityRecordBill;
    @Bind(R.id.Fragment_record)
    RelativeLayout FragmentRecord;
    @Bind(R.id.ll_fragmentUser_login)
    LinearLayout llFragmentUserLogin;
    @Bind(R.id.ptr_fragmentOrder_pulltorefresh)
    PtrClassicFrameLayout frameLayout;
    @Bind(R.id.ll_fragmentUser_noOder)
    LinearLayout llFragmentNoOder;
    private List<Record> recordList;
    private String Url;
    private RecordAdapter recordAdapter=null;
    private int pages=1;
    private boolean isHadMore;
    private ArrayList<Record> list;
    private View view1;
    private LoadingHeader footer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        isHadMore = true;
        pages=1;
        initView();
        initListion();
       // orderFragmentIsLoginOrno();
        return view;
    }

    private void initListion() {
        lvActivityRecordBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record = (Record) parent.getItemAtPosition(position);
                String status = record.getStatus();
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("id", record.getId());
                b.putString("card_number", record.getCard_number());
                b.putString("discount_before_amount", record.getDiscount_before_amount());
                b.putString("discount_after_amount", record.getDiscount_after_amount());
                b.putString("order_number", record.getOrder_number());
                b.putString("ctime", record.getCtime());
                b.putString("product_name", record.getProduct_name());
                b.putString("belong", record.getBelong());
                b.putString("count", record.getCount());
                b.putString("total_time", record.getTotal_time());
                b.putString("user_name",record.getUser_name());
                switch (Integer.parseInt(status)) {
                    case 0:

                    case 2:
                        intent.setClass(getActivity(), NoPayActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(), DetailsActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(getActivity(), PayingActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(getActivity(), CloseDealActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        break;
                }
            }
        });
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
    public void onResume() {
        super.onResume();
        if(!this.isHidden()){
            orderFragmentIsLoginOrno();

        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void orderFragmentIsLoginOrno() {


        if (PreferencesUtil.getPreferences(Constans.ISLOGIN,false)) {
            initData();

            //llFragmentUserLogin.setVisibility(View.GONE);
            //设置上拉刷新
           setPulltoRefresh();
         /*   if(recordAdapter!=null){

                recordAdapter.getList().addAll(recordList);

                recordAdapter.notifyDataSetChanged();
            }*/

        } else {
            llFragmentUserLogin.setVisibility(View.VISIBLE);
            FragmentRecord.setVisibility(View.GONE);
            llFragmentNoOder.setVisibility(View.GONE);
        }
    }

    private void setPulltoRefresh() {

        //实例化自定义头部
        LoadingHeader header = new LoadingHeader(getActivity());

        //刷新时保留头部
        frameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        frameLayout.setKeepHeaderWhenRefresh(true);
        //设置刷新头部
        footer = new LoadingHeader(getActivity());

        footer.setPadding(0, -footer.getHeight(), 0, 0);
        //footer.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
        frameLayout.setFooterView(footer);
        frameLayout.addPtrUIHandler(footer);

        frameLayout.setRatioOfHeaderHeightToRefresh(1.0f);
        frameLayout.setHeaderView(header);
        frameLayout.addPtrUIHandler(header);


        frameLayout.disableWhenHorizontalMove(true);//解决横向滑动冲突
        frameLayout.setPtrHandler(new PtrHandler2() {
            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                frameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRecord(2);
                        frameLayout.refreshComplete();
                        recordAdapter.notifyDataSetChanged();
                    }
                },1200);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler2.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRecord(1);
                        frameLayout.refreshComplete();
                        recordAdapter.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bt_fragment_order_login, R.id.bt_fragment_gas_pay,R.id.bt_fragment_order_gotoOil})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_fragment_order_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));

                break;
            case R.id.bt_fragment_gas_pay:

                WXpay();
                break;
            case R.id.bt_fragment_order_gotoOil:
                ((MainActivity)getActivity()).setHomeCheck();
                break;
        }
    }

    private void WXpay() {
/*        Retrofit retorfit = new Retrofit.Builder()
                .baseUrl(Constans.WXBaseUrl)
                .build();
        RequestInterface requestInterface = retorfit.create(RequestInterface.class);
         Call call =  requestInterface.getPay("android");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    wechatPay(response);
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });*/
    }

    private void wechatPay(Response response) {

        boolean isPaySupported = MyApplication.msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtils.createLongToast(getActivity(), "您没有安装微信或者微信版本太低");
            return;
        }
        MyApplication.msgApi.registerApp(Constans.WX_APP_ID);
        JSONObject json = null;
        try {
            json = new JSONObject(response.toString());


            if (null != json && !json.has("retcode")) {
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
                req.extData = "app data"; // optional
                ToastUtils.createNormalToast(getActivity(), "正常调起支付");
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                MyApplication.msgApi.sendReq(req);
            } else {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                ToastUtils.createNormalToast(getActivity(), "返回错误" + json.getString("retmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initView() {
    }
    private void initData() {
        //获取头部节省的钱数

        getRecord(0);
        getSaveMoney();
    }
    /*
    *
    * Mode 0: adapter为空的时候
    *       1：下拉刷新
    *       2：上拉加载更多
    *
    * */
    private void getRecord(final int Mode) {


        if (Mode==0|| Mode==1) {
            // 如果是初始化，或者是下拉刷新，则都是获取第0页数据
            pages = 1;
            isHadMore = true;
        } else if (Mode==2) {
            // 如果是上拉加载更多
            if(isHadMore){
                pages = pages +1;
            }

        }

        recordList = new ArrayList<Record>();
        //int pages = 1;
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("sign", "");
        map.put("pages", pages);
        new NewHttpRequest(getActivity(), NetConfig.ORDER, NetConfig.getOrderList, Constans.JSONOBJECT, 1, map, false, new NewHttpRequest.RequestCallback() {
             @Override
             public void netWorkError() {

             }

             @Override
             public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                 if(taskId==1){


                     try {
                         JSONArray jsonArray1 =  jsonObject.getJSONArray("list");


                         LogUtil.i("请求成功");
                         recordList=new ArrayList<Record>();
                         for(int i =0;i<jsonArray1.length();i++){
                             JSONObject jsonObject1 =jsonArray1.getJSONObject(i);
                             Record record = new Record();
                             record.setId(jsonObject1.getString("id"));
                             record.setDiscount_after_amount(jsonObject1.getString("discount_after_amount"));
                             record.setCount(jsonObject1.getString("count"));
                             record.setStatus(jsonObject1.getString("status"));
                             record.setDiscount_before_amount(jsonObject1.getString("discount_before_amount"));
                             record.setTotal_time(jsonObject1.getString("total_time"));
                             record.setProduct_name(jsonObject1.getString("product_name"));
                             record.setCard_number(jsonObject1.getString("card_number"));
                             record.setOrder_number(jsonObject1.getString("order_number"));
                             record.setBelong("2");
                             record.setUser_name(jsonObject1.getString("user_name"));
                             record.setCtime(jsonObject1.getString("ctime"));
                             String status = jsonObject1.getString("status");
                             if (jsonObject1.getString("card_number").length()==16){
                                 record.setBelong("2");
                             }else{
                                 record.setBelong("1");
                             }
                             if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
                                 record.setType(0);
                             } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
                                 record.setType(1);
                             }
                             recordList.add(record);

                         }
                         setadapter(recordList,Mode);
                          //putorefresh.onRefreshComplete();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                 }

             }

             @Override
             public void requestError(int code, MessageEntity msg, int taskId) {
                 frameLayout.refreshComplete();

                ToastUtils.createNormalToast( msg.getMessage());
             }
         }).executeTask();
   }

    private void setadapter(List<Record> recordList,int Mode) {

        if(Mode==2){
            isHadMore = false;
            list.addAll(recordList);
            recordAdapter = new RecordAdapter(getActivity(), list);
            lvActivityRecordBill.setAdapter(recordAdapter);

        }else {
            isHadMore = true;
            list  = new ArrayList<>();
            list.addAll(recordList);
            recordAdapter = new RecordAdapter(getActivity(), recordList);
            lvActivityRecordBill.setAdapter(recordAdapter);
            if (list.size()==0){
                llFragmentUserLogin.setVisibility(View.GONE);
                llFragmentNoOder.setVisibility(View.VISIBLE);
                FragmentRecord.setVisibility(View.GONE);
            }else{
                llFragmentUserLogin.setVisibility(View.GONE);
                FragmentRecord.setVisibility(View.VISIBLE);
                llFragmentNoOder.setVisibility(View.GONE);
            }

        }

    }

    private void getSaveMoney() {
        HashMap<String ,Object> map1 =new HashMap<>();
        map1.put("time",System.currentTimeMillis());
        map1.put("sign","");
        new NewHttpRequest(getActivity(), NetConfig.ORDER, NetConfig.GETUSERSAVEMONEY, Constans.JSONOBJECT, 2, map1, true, new NewHttpRequest.RequestCallback() {


            @Override
            public void netWorkError() {

            }

            @Override
            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                if(taskId==2){
                    String money1="00.";
                    String money2="00";
                    String money = null;
                    try {
                        money = jsonObject.getString("money");
                            LogUtil.i(money);

                            String[] arr = money.split("\\.");
                            money1 = arr[0];
                            money2 = arr[1];
                            tvActivityRecordMoney1.setText(money1+".");
                            tvActivityRecordCent.setText(money2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void requestError(int code, MessageEntity msg, int taskId) {

            }
        }).executeTask();
    }
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }


}
