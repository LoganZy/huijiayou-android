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
            Object sign = jsonObject.get("sign");
            if (TextUtils.isEmpty(sign.toString())){
                String session_id = PreferencesUtil.getPreferences("session_id","");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1 = jsonObject;
                jsonObject1.remove("sign");
                if (!TextUtils.isEmpty(session_id) && session_id.indexOf("=") >= 0){
                    session_id = session_id.substring(session_id.indexOf("=")+1);
                    jsonObject.put("sign", MD5.md5(jsonObject1.toString() +"&"+session_id));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
