package com.huijiayou.huijiayou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/19.
 */

public class RechargeDetailDailogAdapter extends RecyclerView.Adapter<RechargeDetailDailogAdapter.RechargeViewHolder>{

    ArrayList<Recharge> recharges;

    public RechargeDetailDailogAdapter(ArrayList<Recharge> recharges){
        this.recharges = recharges;
    }

    @Override
    public RechargeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RechargeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recharge_detail_dialog,parent,false));
    }

    @Override
    public void onBindViewHolder(RechargeViewHolder holder, int position) {
        holder.tv_itemRechargeDetailDialog_money.setText(recharges.get(position).getMoney());
        holder.tv_itemRechargeDetailDialog_time.setText(recharges.get(position).getTiem());
    }

    @Override
    public int getItemCount() {
        return recharges.size();
    }

    public static class Recharge{
        private String money;
        private String tiem;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTiem() {
            return tiem;
        }

        public void setTiem(String tiem) {
            this.tiem = tiem;
        }
    }

    public class RechargeViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_itemRechargeDetailDialog_money)
        TextView tv_itemRechargeDetailDialog_money;

        @Bind(R.id.tv_itemRechargeDetailDialog_time)
        TextView tv_itemRechargeDetailDialog_time;

        public RechargeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
