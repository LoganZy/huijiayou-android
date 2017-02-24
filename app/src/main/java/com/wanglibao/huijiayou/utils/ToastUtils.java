package com.wanglibao.huijiayou.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
import com.wanglibao.huijiayou.MyApplication;

/**
 * @author: Jason
 * @date: 15/9/10.
 * @time: 14:49.
 * toast工具类
 */
public class ToastUtils {

    private static Toast toast;
    /**
     *
     * @param context  The Class's context , where  want to use this tool
     * @param message  The message will be show
     */
    public static Toast createNormalToast(Context context , String message){
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            toast.setText(message);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return toast;
    }

    public static Toast createLongToast(Context context , String message){
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            toast.setText(message);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return toast;
    }

    public static Toast createNormalToast(String message) {
        return createNormalToast(MyApplication.getContext(),message);
    }
}
