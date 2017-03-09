package com.wanglibao.huijiayou;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

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
