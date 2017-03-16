package com.huijiayou.huijiayou.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.huijiayou.huijiayou.R;

public class DialogUtil {

    public static Dialog createLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.dialog_bgTransparent);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }

}