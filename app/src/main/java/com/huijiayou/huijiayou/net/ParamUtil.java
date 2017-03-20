package com.huijiayou.huijiayou.net;

import android.text.TextUtils;

import com.huijiayou.huijiayou.utils.MD5;
import com.huijiayou.huijiayou.utils.PreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        for (int i = 0; i < letters.length; i++){
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            ArrayList<String> arrayList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.indexOf(letters[i]) == 0){ //按首字母排序
                    try {
                        jsonObject.put(key, value);
                        arrayList.add(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int j = 0; j < arrayList.size(); j++){
                params.remove(arrayList.get(j));
            }
        }
        try {
            if (isMD5){
                JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                jsonObject1.remove("sign");
                Iterator<String> iterator = jsonObject1.keys();
                StringBuffer value = new StringBuffer();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    value.append(jsonObject1.get(key)+"&");
                }
                String token = PreferencesUtil.getPreferences("token","");
                if (TextUtils.isEmpty(token)){
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
