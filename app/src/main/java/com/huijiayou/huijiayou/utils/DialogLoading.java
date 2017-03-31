package com.huijiayou.huijiayou.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;

import com.huijiayou.huijiayou.R;


/**
 * Created by lugg on 2017/1/18.
 */

public class DialogLoading {

    private Dialog dialog;
    Activity activity;
    AnimationDrawable animationDrawable;

    public DialogLoading(Activity activity){
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog_bgTransparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setContentView(R.layout.dialog_loading);
        animationDrawable = (AnimationDrawable) dialog.findViewById(R.id.img_dialogLoading_view).getBackground();
    }

    public void show(){
        if (dialog != null){
            dialog.show();
            if (animationDrawable != null && !animationDrawable.isRunning()){
                animationDrawable.start();
            }
        }
    }

    public void dismiss(){
        if (dialog != null){
            dialog.dismiss();
            if (animationDrawable != null && animationDrawable.isRunning()){
                animationDrawable.stop();
            }
        }
    }

    public boolean isShow(){
        if (dialog != null){
            return dialog.isShowing();
        }else{
            return false;
        }
    }

}
