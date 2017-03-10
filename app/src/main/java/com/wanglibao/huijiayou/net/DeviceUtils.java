package com.wanglibao.huijiayou.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.mcxiaoke.packer.helper.PackerNg;
import com.wanglibao.huijiayou.utils.DeviceUtil;


/**
 * DESC: 获取设备的一些信息
 * User: Jason
 * Date: 16/7/2
 * Time: 上午11:10
 */
public class DeviceUtils {

    private static String VERSION ;//软件版本
    private static String MANUFACTURE ;//手机生产商
    private static String MODEL;//型号
    private static String CHANNEL_ID;//渠道ID
    private static String SDK_VERSION;//sdk版本
    private static String NETWORKTYPE;//网络状态
    private static String TERMINAL = "wlbAPP";//本APP的标志

    public static String getHeadInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        VERSION = getVersion(context);
        sb.append(VERSION).append("/");

        MANUFACTURE = DeviceUtil.getManufacture();
        sb.append(MANUFACTURE).append("_");

        MODEL = android.os.Build.MODEL.replace(" ", "");
        sb.append(MODEL).append("_android/");

//        CHANNEL_ID = SharedPreferenceUtil.getWlibaoConfigData(context,"channelId");
        CHANNEL_ID = PackerNg.getMarket(context);

        if(!TextUtils.isEmpty(CHANNEL_ID)) {
            sb.append(CHANNEL_ID).append("/");
        } else {
            // 默认渠道wanglibao
            CHANNEL_ID = "wanglibao/";
            sb.append(CHANNEL_ID);
        }
//        LogUtil.e("CHANNEL_ID---->" + CHANNEL_ID);

        SDK_VERSION = android.os.Build.VERSION.RELEASE;
        sb.append(SDK_VERSION).append("/");

        NETWORKTYPE = DeviceUtil.getNetWorkType(context);
        sb.append(NETWORKTYPE).append("/").append(TERMINAL);

        return sb.toString();
    }

    /**
     * 获取APP版本号
     * @return
     */
    public static String getVersion(Context context) {
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
