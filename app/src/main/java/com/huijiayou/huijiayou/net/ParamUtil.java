package com.huijiayou.huijiayou.net;

import android.text.TextUtils;

import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.utils.MD5;
import com.huijiayou.huijiayou.utils.PreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by baozi on 2016/6/22.
 */
public class ParamUtil {
    static String[] letters = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    ParamUtil() {

    }

    public static JSONObject getJSONObjectParams(HashMap<String, Object> params) {
        if (params == null) {
            return null;
        }
        boolean isMD5 = params.get("sign") == null ? false : true;
        JSONObject jsonObject = new JSONObject();
        try {
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            ArrayList<String> arrayList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                jsonObject.put(key,value);
                arrayList.add(key);
            }
            Collections.sort(arrayList);
            if (isMD5){
                arrayList.remove("sign");
                StringBuffer value = new StringBuffer();
                for (int i = 0; i < arrayList.size(); i++){
                    value.append(params.get(arrayList.get(i)) + "&");
                }
                String token = PreferencesUtil.getPreferences(Constans.USER_TOKEN,"");
                boolean isLogin = PreferencesUtil.getPreferences(Constans.ISLOGIN,false);
                if (TextUtils.isEmpty(token) || !isLogin){
                    token = "HUIJIAYOU_TOKEN";
                }
                value.append(token);
                jsonObject.put("sign", MD5.md5(value.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
