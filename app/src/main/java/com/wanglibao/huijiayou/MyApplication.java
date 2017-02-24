package com.wanglibao.huijiayou;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedList;
import java.util.List;

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
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        msgApi.registerApp("wxd930ea5d5a258f4f");
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
