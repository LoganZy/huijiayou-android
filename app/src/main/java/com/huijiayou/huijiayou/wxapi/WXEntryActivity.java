package com.huijiayou.huijiayou.wxapi;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.weixin.callback.WXCallbackActivity;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;


/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }
//    微信请求第三方登录时，回回调该方法
    @Override
    public void onReq(BaseReq baseReq) {

        finish();
    }
//      微信返回给第三方的请求结果
    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        if (baseResp != null) {
            Constans.resp = baseResp;
        }
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("微信支付结果码:" + baseResp.errCode);
            builder.show();

            //请求服务器
        }
        switch(baseResp.errCode) {

            case BaseResp.ErrCode.ERR_OK:
                result ="分享成功";
                ToastUtils.createNormalToast(this,result);
                //		      可用以下两种方法获得code
                //      resp.toBundle(bundle);
                //      Resp sp = new Resp(bundle);
                //      String code = sp.code;<span style="white-space:pre">
                //      或者
                String code = ((SendAuth.Resp) baseResp).code;
                //上面的code就是接入指南里要拿到的code
                LogUtil.i(code);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                ToastUtils.createLongToast(this,result);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                ToastUtils.createLongToast(this,result);
                finish();
                break;
            default:
                result = "已经返回";
                ToastUtils.createLongToast(this,result);
                finish();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
        finish();
    }
}
