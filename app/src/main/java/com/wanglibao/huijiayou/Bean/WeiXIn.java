package com.wanglibao.huijiayou.Bean;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class WeiXIn {
    /*{
"access_token":"ACCESS_TOKEN",
"expires_in":7200,
"refresh_token":"REFRESH_TOKEN",
"openid":"OPENID",
"scope":"SCOPE"
}*/
    public static String access_token;
    public static int expires_in;
    public static String refresh_token;
    public static String openid;
    public static String scope;

    public static int getExpires_in() {
        return expires_in;
    }

    public static void setExpires_in(int expires_in) {
        WeiXIn.expires_in = expires_in;
    }

    public static String getScope() {
        return scope;
    }

    public static void setScope(String scope) {
        WeiXIn.scope = scope;
    }

    public static String getRefresh_token() {
        return refresh_token;
    }

    public static void setRefresh_token(String refresh_token) {
        WeiXIn.refresh_token = refresh_token;
    }

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
