package com.huijiayou.huijiayou.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.huijiayou.huijiayou.R;

/**
 * Created by lugg on 2017/3/30.
 */

public class PayErrorDialog {
    Dialog dialog;
    Context context;

    public PayErrorDialog(Dialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    public void CreateDialog(){
        dialog = new Dialog(context, R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment_payerr, null);

        dialog.setContentView(view);
    }

}
