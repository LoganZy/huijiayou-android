package com.wanglibao.huijiayou.Bean;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class WeiXIn {
    public static String access_token;
    public static String openid;

    public static void setAccess_token(String access_token) {
        WeiXIn.access_token = access_token;
    }

    public static void setOpenid(String openid) {
        WeiXIn.openid = openid;
    }

    public static String getAccess_token() {
        return access_token;
    }

    public static String getOpenid() {
        return openid;
    }

}
