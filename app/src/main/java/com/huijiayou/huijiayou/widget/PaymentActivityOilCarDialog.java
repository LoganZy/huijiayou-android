package com.huijiayou.huijiayou.widget;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.AddOilCardActivity;
import com.huijiayou.huijiayou.activity.PaymentActivity;
import com.huijiayou.huijiayou.adapter.OilCardAdapter;

import java.util.ArrayList;

/**
 * Created by lugg on 2017/3/6.
 */

public class PaymentActivityOilCarDialog {

    Dialog dialog;
    PaymentActivity paymentActivity;
    ArrayList<OilCardAdapter.OilCardEntity> oilCardEntities;
    OilCardAdapter oilCardAdapter;
    ImageView lastSelectedOilCard;
    public PaymentActivityOilCarDialog(final PaymentActivity paymentActivity, final ArrayList<OilCardAdapter.OilCardEntity> oilCardEntities){
        this.paymentActivity = paymentActivity;
        this.oilCardEntities = oilCardEntities;
        oilCardAdapter = new OilCardAdapter(paymentActivity,oilCardEntities,OilCardAdapter.SHOWTYPE_SELECTOILCARD,new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                paymentActivity.oilCard = oilCardEntities.get((Integer) v.getTag()).getOil_card_number();
                paymentActivity.oilCardName = oilCardEntities.get((Integer) v.getTag()).getUser_name();
                paymentActivity.setOilCard(paymentActivity.oilCard);
                if (lastSelectedOilCard != null){
                    lastSelectedOilCard.setVisibility(View.GONE);
                }
                ImageView imageView = (ImageView) v.findViewById(R.id.imgView_itemActivityOilCard_selected);
                imageView.setVisibility(View.VISIBLE);
                lastSelectedOilCard = imageView;
                dialog.dismiss();
            }
        });
        dialog = new Dialog(paymentActivity,R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(paymentActivity).inflate(R.layout.dialog_payment_activity_oil_car, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_dialogPaymentActivityOilCar_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(paymentActivity));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setAdapter(oilCardAdapter);
        Button btn_dialogPaymentActivityOilCar_addOilCard = (Button) view.findViewById(R.id.btn_dialogPaymentActivityOilCar_addOilCard);
        Button btn_dialogPaymentActivityOilCar_cancel = (Button) view.findViewById(R.id.btn_dialogPaymentActivityOilCar_cancel);
        btn_dialogPaymentActivityOilCar_addOilCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentActivity.startActivityForResult(new Intent(paymentActivity, AddOilCardActivity.class),paymentActivity.addOilCarRequestCode);
                dialog.dismiss();
            }
        });

        btn_dialogPaymentActivityOilCar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        WindowManager windowManager = paymentActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

}
