package com.wanglibao.huijiayou.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by baozi on 2016/6/22.
 */
public class ParamUtil {

    ParamUtil() {

    }

    public static JSONObject getJSONObjectParams(HashMap<String, Object> params) {
        if (params == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
