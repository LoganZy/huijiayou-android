
package com.wanglibao.huijiayou.utils;

import android.util.Log;

/**
 * LogUtil
 *
 * @author 333
 */

public class LogUtil {
    private static final boolean DEBUG = com.wanglibao.huijiayou.config.Constans.DEBUG;
    public static final String TAG = "HuiJiaYou";

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            Log.w(TAG, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    public static void v(String msg) {
        if (DEBUG)
            Log.v(TAG, msg);
    }

}

