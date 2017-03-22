package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.adapter.RecordAdapter;
import com.huijiayou.huijiayou.bean.Record;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * Created by lugg on 2017/2/24.
 */

public class OrderFragment extends Fragment {
    public static final String TAG = "OrderFragment";

    @Bind(R.id.bt_fragment_order_login)
    Button btFragmentGasLogin;
    @Bind(R.id.bt_fragment_gas_pay)
    Button btFragmentGasPay;
    @Bind(R.id.tv_activityRecord_money)
    TextView tvActivityRecordMoney;
    @Bind(R.id.tv_activityRecord_cent)
    TextView tvActivityRecordCent;
    @Bind(R.id.lv_activity_record_bill)
    ListView lvActivityRecordBill;

    private List<Record> recordList ;
    private String Url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
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
            ToastUtils.createLongToast(getActivity(),"您没有安装微信或者微信版本太低");
            return;
        }
        MyApplication.msgApi.registerApp(Constans.WX_APP_ID);
        JSONObject json = null;
        try {
            json = new JSONObject(response.toString());


            if(null != json && !json.has("retcode") ){
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId			= json.getString("appid");
                req.partnerId		= json.getString("partnerid");
                req.prepayId		= json.getString("prepayid");
                req.nonceStr		= json.getString("noncestr");
                req.timeStamp		= json.getString("timestamp");
                req.packageValue	= json.getString("package");
                req.sign			= json.getString("sign");
                req.extData			= "app data"; // optional
                ToastUtils.createNormalToast(getActivity(),"正常调起支付");
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                MyApplication.msgApi.sendReq(req);
            }else{
                Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                ToastUtils.createNormalToast(getActivity(),"返回错误"+json.getString("retmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void initView() {
        List list = new ArrayList();
        for (int i=0;i<20;i++){
            list.add("hahhah"+i);
        }
        RecordAdapter recordAdapter = new RecordAdapter(getActivity(),list);
        lvActivityRecordBill.setAdapter(recordAdapter);
    }


    private void initData() {
        //获取头部节省的钱数
        getSaveMoney();
        //获取列表需要展示的加油记录
        recordList = getRecord(Url);
    }

    private List<Record> getRecord(String url) {

        return null;
    }

    private void getSaveMoney() {

    }

}
