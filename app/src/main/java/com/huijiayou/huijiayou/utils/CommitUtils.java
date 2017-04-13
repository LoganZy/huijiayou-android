package com.huijiayou.huijiayou.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by lugg on 2017/3/2.
 */

public class CommitUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static double bigDecimal2(double value, int digit){
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(digit,BigDecimal.ROUND_UP).doubleValue();
    }

    public static double decimal1(double vlaue){
        return Double.parseDouble(new DecimalFormat("0.0").format(vlaue));
    }

    public static double decimal2(double vlaue){
        return Double.parseDouble(new DecimalFormat("0.00").format(vlaue));
    }

    public static String getVersion(Context context){
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
