package com.wanglibao.huijiayou.xxtea;

import android.util.Base64;

import org.json.JSONObject;

/**
 * @Desc: 参数xxtea加密处理
 * @Author: Jason
 * @Date: 2016/7/28
 * @Time: 10:37
 */
public class ParamHanding {

    private static final String PHPKEY = "wlAZ+az'Az";
    private static final String KEY_CODE = "7007";
    private static final String TAG = ParamHanding.class.getSimpleName();


    public static String paramJsonHanding(JSONObject jsonObject) {
//        LogUtil.e(TAG + "---------paramJsonHanding----" + jsonObject.toString());
        byte[] encrypt = XXTEA.encrypt(jsonObject.toString(), PHPKEY);
        String base64 = new String(Base64.encode(encrypt, Base64.DEFAULT));
        String result = KEY_CODE + base64;
//        LogUtil.e("加密------->" + result);
        return result;
    }

}
