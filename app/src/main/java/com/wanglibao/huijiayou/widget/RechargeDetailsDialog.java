package com.wanglibao.huijiayou.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wanglibao.huijiayou.R;

/**
 * Created by lugg on 2017/3/6.
 */

public class RechargeDetailsDialog {

    Context context;

    public RechargeDetailsDialog(Context context){
        this.context = context;
    }

    public void create(){
        Dialog dialog = new Dialog(context,R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_recharge_details, null);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
    }

}
