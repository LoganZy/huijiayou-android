package com.huijiayou.huijiayou.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.LoginActivity;

/**
 * Created by lugg on 2017/3/30.
 */

public class HomeFristStartDialog {
    Activity activity;

    public HomeFristStartDialog(Activity activity) {
        this.activity = activity;
    }

    public void ShowDialog(){
        final Dialog dialog = new Dialog(activity, R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_home_frist_start, null);
        Button button = (Button) view.findViewById(R.id.btn_dialogHomeFristStart_get);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.startActivity(new Intent(activity, LoginActivity.class));
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
