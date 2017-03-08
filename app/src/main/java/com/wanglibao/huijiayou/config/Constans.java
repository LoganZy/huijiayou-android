package com.wanglibao.huijiayou.config;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * Created by lugg on 2017/2/24.
 */

public class Constans {


    public final static boolean DEBUG = true;
    public static String WXBaseUrl = "https://api.weixin.qq.com/";
    public static String WX_APP_ID = "wx8afbd309ff35e712";
    public static IWXAPI WXapi;
    public static BaseResp resp;
    public static String AppSecret = "a33465db152afd3bdd86c2fb38b7712b";


  // public static final String APP_ID = "wxd930ea5d5a258f4f";

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }

}
