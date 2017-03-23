package com.huijiayou.huijiayou.utils;

import android.app.Activity;
import android.app.Dialog;

import com.huijiayou.huijiayou.R;


/**
 * Created by lugg on 2017/1/18.
 */

public class DialogLoading {

    private Dialog dialog;
    Activity activity;

    public DialogLoading(Activity activity){
        this.activity = activity;
        if (dialog == null){
            dialog = new Dialog(activity, R.style.dialog_bgTransparent);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setContentView(R.layout.dialog_loading);
        }
    }

    public Dialog GetDialog(){
        if (dialog != null && !dialog.isShowing()){
            dialog.show();
        }
        return dialog;
    }
//    public void dismiss(){
//        if (dialog != null && dialog.isShowing()){
//            dialog.dismiss();
//        }
//    }

//    public boolean isShow(){
//        return dialog.isShowing();
//    }

}
