package com.huijiayou.huijiayou.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login_error, null);
        ImageButton imgBenClose = (ImageButton) view.findViewById(R.id.imgBtn_dialogLoginError_close);
        Button btn = (Button) view.findViewById(R.id.btn_dialogLoginError_readLogin);
        imgBenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setContentView(view);
    }

}
