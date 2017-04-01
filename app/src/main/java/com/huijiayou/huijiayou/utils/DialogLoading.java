package com.huijiayou.huijiayou.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import com.huijiayou.huijiayou.R;


/**
 * Created by lugg on 2017/1/18.
 */

public class DialogLoading {

    private Dialog dialog;
    Context context;
    AnimationDrawable animationDrawable;

    public DialogLoading(Context context){
        this.context = context;
        dialog = new Dialog(context, R.style.dialog_bgTransparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setContentView(R.layout.dialog_loading);
        animationDrawable = (AnimationDrawable) dialog.findViewById(R.id.img_dialogLoading_view).getBackground();
    }

    public void show(){
        if (dialog != null){
            dialog.show();
            if (animationDrawable != null){
                if (!animationDrawable.isRunning()){
                    animationDrawable.start();
                }
            }else{
                animationDrawable = (AnimationDrawable) dialog.findViewById(R.id.img_dialogLoading_view).getBackground();
                animationDrawable.start();
            }
        }
    }

    public void dismiss(){
        if (dialog != null){
            dialog.dismiss();
            if (animationDrawable != null){
                if (animationDrawable.isRunning()){
                    animationDrawable.stop();
                }
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
