package com.huijiayou.huijiayou.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.RechargeDetailDailogAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lugg on 2017/3/6.
 */

public class RechargeDetailsDialog {

    Context context;
    public ArrayList<RechargeDetailDailogAdapter.Recharge> rechargeArrayList;
    public RechargeDetailsDialog(Context context,int moneyMonth,int month){
        this.context = context;
        rechargeArrayList = initData(moneyMonth,month);
    }

    public ArrayList<RechargeDetailDailogAdapter.Recharge> initData(int moneyMonth,int month){
        ArrayList<RechargeDetailDailogAdapter.Recharge> recharges = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy / MM/dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        RechargeDetailDailogAdapter.Recharge recharge = null;
        for (int i = 1; i <= month; i++){
            recharge = new RechargeDetailDailogAdapter.Recharge();
            recharge.setMoney(moneyMonth+ " (" + i + "/" + month + "" + ")");
            if (i == 1){
                recharge.setTiem("支付后2小时内");
            }else {
                recharge.setTiem(simpleDateFormat.format(calendar.getTime()));
            }
            recharges.add(recharge);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            calendar.add(Calendar.MONTH,1);
        }
        return recharges;
    }

    public void create(){
        final Dialog dialog = new Dialog(context,R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_recharge_details, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_dialogRechargeDetails_time);
        ImageButton imgBtnClost = (ImageButton) view.findViewById(R.id.imgBtn_dialogRechargeDetails_close);
        imgBtnClost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new RechargeDetailDailogAdapter(rechargeArrayList));
        dialog.setContentView(view);
        dialog.show();
    }

}
