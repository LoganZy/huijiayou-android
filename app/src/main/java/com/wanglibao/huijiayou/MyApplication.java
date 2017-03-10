package com.wanglibao.huijiayou;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lugg on 2017/2/22.
 */

public class MyApplication extends Application {

    private static Context context;
    public static SharedPreferences preferences;

    public List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        JPushInterface.setDebugMode(true);//极光推送
        JPushInterface.init(this);//极光推送

        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx8afbd309ff35e712", "80564228d4bb7ce52d462cf80c996476");//ok
        PlatformConfig.setQQZone("1105947235", "0VBI1ejZjatqOTOo"); //ok
        //final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
       // msgApi.registerApp("wx9bcf508fbe5af427");
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Context getContext(){
        return context;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }
}
