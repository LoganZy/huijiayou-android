package com.huijiayou.huijiayou.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.LoginActivity;

/**
 * Created by lugg on 2017/4/11.
 */

public class LoginErrorDialog {
    
    private static Dialog dialog;
    
    private LoginErrorDialog(){}
    
    public static void showDialog(Activity activity){
        if (dialog == null){
            initDialog(activity);
        }else{
            if (!dialog.isShowing()){
                dialog.show();
            }
        }
    }
    
    private static void initDialog(final Activity activity){
        dialog = new Dialog(activity, R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_login_error, null);
        ImageButton imgBenClose = (ImageButton) view.findViewById(R.id.imgBtn_dialogLoginError_close);
        Button btn = (Button) view.findViewById(R.id.btn_dialogLoginError_readLogin);
        imgBenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                dialog.dismiss();
                dialog = null;
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
