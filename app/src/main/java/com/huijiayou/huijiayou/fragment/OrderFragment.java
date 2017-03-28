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

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.CloseDealActivity;
import com.huijiayou.huijiayou.activity.DetailsActivity;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.activity.NoPayActivity;
import com.huijiayou.huijiayou.adapter.RecordAdapter;
import com.huijiayou.huijiayou.bean.Record;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import retrofit2.http.HEAD;

/**
 * Created by lugg on 2017/2/24.
 */
public class OrderFragment extends Fragment {
    public static final String TAG = "OrderFragment";

    @Bind(R.id.bt_fragment_order_login)
    Button btFragmentGasLogin;
    @Bind(R.id.bt_fragment_gas_pay)
    Button btFragmentGasPay;
   // @Bind(R.id.tv_activityRecord_money)
    //TextView tvActivityRecordMoney;
    //@Bind(R.id.tv_activityRecord_cent)
       // TextView tvActivityRecordCent;
    @Bind(R.id.lv_activity_record_bill)
    ListView lvActivityRecordBill;
    @Bind(R.id.Fragment_record)
    RelativeLayout FragmentRecord;
    @Bind(R.id.ll_fragmentUser_login)
    LinearLayout llFragmentUserLogin;
    private List<Record> recordList;
    private String Url;
//    private PullToRefreshListView putorefresh;
//    private ILoadingLayout headerView;
//    private ILoadingLayout footerView;
    private RecordAdapter recordAdapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
//        putorefresh = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
//        putorefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        initHeaderViewAndFooterView();
//        listView = putorefresh.getRefreshableView();
        View headview = View.inflate(getActivity(),R.layout.order_head_layout,null);
        listView.addHeaderView(headview);
//        putorefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                initData();
//            }
//        });
        return view;
    }

    private void initHeaderViewAndFooterView() {
        // 获取一个可以修改头部的HeaderView的代理
        boolean includeStart = true;
        boolean includeEnd = false;
//        headerView = putorefresh.getLoadingLayoutProxy(includeStart, includeEnd);
//        headerView.setPullLabel("下拉以刷新");		// 设置头部未完全拉出来的时候的文本描述
//        headerView.setReleaseLabel("松开以刷新");	// 设置头部完全拉出来的时候的文本描述
//        headerView.setRefreshingLabel("正在刷新...");	// 设置正在刷新的时候显示的文本

        includeStart = false;
        includeEnd = true;
//        footerView = putorefresh.getLoadingLayoutProxy(includeStart, includeEnd);
//        footerView.setPullLabel("上拉以加载更多");		// 设置头部未完全拉出来的时候的文本描述
//        footerView.setReleaseLabel("松开以加载更多");	// 设置头部完全拉出来的时候的文本描述
//        footerView.setRefreshingLabel("正在加载更多...");	// 设置正在刷新的时候显示的文本
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
        isLoginOrno();
    }

    private void isLoginOrno() {
     /*   new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.LOGINSTATUS, Constans.JSONOBJECT, 1, false, new NewHttpRequest.RequestCallback() {
            @Override
            public void netWorkError() {

            }

            @Override
            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                try {
                    int status =jsonObject.getInt("status");
                    if (status==1) {
                        llFragmentUserLogin.setVisibility(View.GONE);
                        FragmentRecord.setVisibility(View.VISIBLE);
                        if(recordAdapter!=null){

                            recordAdapter.getList().addAll(recordList);

                            recordAdapter.notifyDataSetChanged();
                        }

                        recordAdapter = new RecordAdapter(getActivity(), recordList);
                        listView.setAdapter(recordAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Record record = recordList.get(position-2);
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
                                b.putString("pay_time",record.getPay_time());
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

                    } else {
                        llFragmentUserLogin.setVisibility(View.VISIBLE);
                        FragmentRecord.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(int code, MessageEntity msg, int taskId) {

            }
        }).executeTask();
*/

        if (MyApplication.isLogin) {
            llFragmentUserLogin.setVisibility(View.GONE);
            FragmentRecord.setVisibility(View.VISIBLE);
            if(recordAdapter!=null){

                recordAdapter.getList().addAll(recordList);

                recordAdapter.notifyDataSetChanged();
            }

            recordAdapter = new RecordAdapter(getActivity(), recordList);
            listView.setAdapter(recordAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Record record = recordList.get(position-2);
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
                    b.putString("pay_time",record.getPay_time());
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

        } else {
            llFragmentUserLogin.setVisibility(View.VISIBLE);
            FragmentRecord.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bt_fragment_order_login, R.id.bt_fragment_gas_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_fragment_order_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));

                break;
            case R.id.bt_fragment_gas_pay:

                WXpay();
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
   /*     List list = new ArrayList();
        for (int i=0;i<20;i++){
            list.add("hahhah"+i);
        }*/
        //isLoginOrno();

    }


    private void initData() {
        //获取头部节省的钱数
        getSaveMoney();
        getRecord();
    }

    private void getRecord() {
        recordList =new ArrayList<Record>();
        recordList = new ArrayList<>();
        String status = "0";
        final Record record = new Record();
        record.setStatus("0");
        record.setCard_number("1000000111");
        record.setDiscount_after_amount("35000");
        record.setTotal_time("6");
        record.setCount("2");
        record.setProduct_name("中国石油");
        record.setDiscount_before_amount("35000");
        record.setBelong("2");
        record.setCtime("20170612");
        record.setUser_name("小刚");
        record.setPay_time("20170204");
        record.setOrder_number("010000000000");
        if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
            record.setType(1);
        } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
            record.setType(2);
        }
        recordList.add(record);
        status = "4";
        Record record1 = new Record();
        record1.setStatus("4");
        record1.setCard_number("1000000111");
        record1.setDiscount_after_amount("35000");
        record1.setTotal_time("6");
        record1.setCount("3");
        record1.setProduct_name("中国石油");
        record1.setDiscount_before_amount("35000");
        record1.setBelong("2");
        record1.setCtime("20170612");
        record1.setUser_name("小磊");
        record1.setPay_time("20170304");
        record1.setOrder_number("010000000000");
        if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
            record1.setType(1);
        } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
            record1.setType(2);
        }
        recordList.add(record1);

        status = "1";
        Record record2 = new Record();
        record2.setStatus("1");
        record2.setCard_number("1000000111");
        record2.setDiscount_after_amount("35000");
        record2.setTotal_time("6");
        record2.setCount("3");
        record2.setProduct_name("中国石油");
        record2.setDiscount_before_amount("35000");
        record2.setBelong("2");
        record2.setCtime("20170612");
        record2.setUser_name("小空");
        record2.setPay_time("20170205");
        record2.setOrder_number("010000000000");
        if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
            record2.setType(1);
        } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
            record2.setType(2);
        }
        recordList.add(record2);


        status = "3";
        Record record3 = new Record();
        record3.setStatus("3");
        record3.setCard_number("1000000111");
        record3.setDiscount_after_amount("35000");
        record3.setTotal_time("6");
        record3.setCount("3");
        record3.setProduct_name("中国石油");
        record3.setDiscount_before_amount("35000");
        record3.setBelong("2");
        record3.setCtime("20170612");
        record3.setUser_name("小空");
        record3.setPay_time("20170205");
        record3.setOrder_number("010000000000");
        if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
            record3.setType(1);
        } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
            record3.setType(2);
        }
        recordList.add(record3);


        status = "2";
        Record record4= new Record();
        record4.setStatus("2");
        record4.setCard_number("1000000111");
        record4.setDiscount_after_amount("35000");
        record4.setTotal_time("6");
        record4.setCount("3");
        record4.setProduct_name("中国石油");
        record4.setDiscount_before_amount("35000");
        record4.setBelong("2");
        record4.setCtime("20170612");
        record4.setUser_name("小空");
        record4.setPay_time("20170205");
        record4.setOrder_number("010000000000");
        if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
            record4.setType(1);
        } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
            record4.setType(2);
        }
        recordList.add(record4);



        int pages = 0;
//        if (recordAdapter == null || putorefresh.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
//            // 如果是初始化，或者是下拉刷新，则都是获取第0页数据
//            pages = 0;
//        } else if (putorefresh.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
//            // 如果是上拉加载更多
//            pages = recordAdapter.getCount();
        }
     /*   HashMap<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("sign", "");
        map.put("pages", pages);
        new NewHttpRequest(getActivity(), Constans.URL_zxg + Constans.ORDER, Constans.getOrderList, Constans.JSONOBJECT, 1, map, true, new NewHttpRequest.RequestCallback() {
             @Override
             public void netWorkError() {

             }

             @Override
             public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                 if(taskId==1){


                     try {
                         JSONArray jsonArray1 =  jsonObject.getJSONArray("list");
                     *//*       Object object=jsonObject.get("list");
                            if (object==null){
                               JSONObject jsonObject1 =  jsonObject.getJSONObject("list");
                            }
                            JSONArray jsonArray1 = (JSONArray) object;*//*
                         LogUtil.i("请求成功");
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
                             record.setBelong(jsonObject1.getString("belong"));
                             record.setCard_number(jsonObject1.getString("card_number"));
                             record.setOrder_number(jsonObject1.getString("order_number"));
                             record.setPay_time(jsonObject1.getString("pay_time"));
                             record.setUser_name(jsonObject1.getString("user_name"));
                             String status = jsonObject1.getString("status");
                             if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2") || TextUtils.equals(status, "4")) {
                                 record.setType(1);
                             } else if (TextUtils.equals(status, "1") || TextUtils.equals(status, "3")) {
                                 record.setType(2);
                             }
                             recordList.add(record);

                         }
                          putorefresh.onRefreshComplete();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                 }

             }

             @Override
             public void requestError(int code, MessageEntity msg, int taskId) {

                 putorefresh.onRefreshComplete();
                ToastUtils.createNormalToast( msg.getMessage());
             }
         }).executeTask();*/
//    }

    private void getSaveMoney() {

    }

}
