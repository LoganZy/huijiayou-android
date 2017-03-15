package com.wanglibao.huijiayou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.activity.LoginActivity;
import com.wanglibao.huijiayou.config.Constans;
import com.wanglibao.huijiayou.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lugg on 2017/2/24.
 */

public class GasFragment extends Fragment {

    @Bind(R.id.bt_fragment_gas_login)
    Button btFragmentGasLogin;
    @Bind(R.id.bt_fragment_gas_pay)
    Button btFragmentGasPay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gas, container, false);
        ButterKnife.bind(this, view);
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

    @OnClick({R.id.bt_fragment_gas_login, R.id.bt_fragment_gas_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_fragment_gas_login:
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

        boolean isPaySupported = Constans.WXapi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtils.createLongToast(getActivity(),"您没有安装微信或者微信版本太低");
            return;
        }
        Constans.WXapi.registerApp(Constans.WX_APP_ID);
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
                Constans.WXapi.sendReq(req);
            }else{
                Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                ToastUtils.createNormalToast(getActivity(),"返回错误"+json.getString("retmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
