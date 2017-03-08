package com.wanglibao.huijiayou.request;

import com.wanglibao.huijiayou.Bean.WeiXIn;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * Created by lugg on 2017/2/27.
 */

public interface RequestInterface {
    @GET("sns/oauth2/access_token")
    Call getAccess_token(@QueryMap Map<String,String> map);

    @GET("sns/userinfo")
    Call getImformation(@QueryMap Map<String,String> map2);

    @GET("pub_v2/app/app_pay.php")
    Call getPay(@Query("plat") String android);
}
