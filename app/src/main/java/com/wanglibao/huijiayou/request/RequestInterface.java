package com.wanglibao.huijiayou.request;

import com.wanglibao.huijiayou.Bean.WeiXIn;
import com.wanglibao.huijiayou.config.Constans;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lugg on 2017/2/27.
 */

public interface RequestInterface {

    //public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //@POST("sns/oauth2/access_token")
   // Call<WeiXIn> getAccess_token(@Query("appid")Constans.App, @Query("openid") String openId, @Query("sign") String sign, @Query("t") String  t, Callback<StatusBean> callback);

}
