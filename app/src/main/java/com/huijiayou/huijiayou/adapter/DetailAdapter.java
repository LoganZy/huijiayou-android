package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.DetailsActivity;
import com.huijiayou.huijiayou.bean.OrderDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class DetailAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDetail> list;

    public DetailAdapter(DetailsActivity detailsActivity, List<OrderDetail> detailslist) {
        this.context = detailsActivity;
        this.list = detailslist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            OrderDetail orderDetail=list.get(position);
            ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_activity_detail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        int count = position+1;
        holder.tvItemDetailTime.setText(orderDetail.getRecharge_time());
        holder.tvItemDetailCount.setText(orderDetail.getRecharge_amount()+" "+"("+count+"/"+orderDetail.getTotal_time()+")");
        String status = orderDetail.getStatus();

        if (status==null|| TextUtils.equals(status,"0")||TextUtils.equals(status,"2")){
            holder.imgItemDetail.setBackgroundResource(R.mipmap.ic_details_option_n);
            holder.tvItemDetailCount.setTextColor(context.getResources().getColor(R.color.fail));
            holder.tvItemDetailTime.setTextColor(context.getResources().getColor(R.color.fail));
        }else if(TextUtils.equals(status,"1")){
            holder.tvItemDetailCount.setTextColor(context.getResources().getColor(R.color.sucsses));
            holder.tvItemDetailTime.setTextColor(context.getResources().getColor(R.color.sucsses));
            holder.imgItemDetail.setBackgroundResource(R.mipmap.ic_details_option_h);
        }else if(TextUtils.equals(status,"3")){
            holder.imgItemDetail.setBackgroundResource(R.mipmap.ic_details_option);
            holder.tvItemDetailCount.setTextColor(context.getResources().getColor(R.color.loding));
            holder.tvItemDetailTime.setTextColor(context.getResources().getColor(R.color.loding));
        }

        //特殊要求
        if(position==list.size()-1){
            holder.tvItemDetailDown.setVisibility(View.INVISIBLE);
        }
        if(position==0){
            holder.tvItemDetailUp.setVisibility(View.INVISIBLE);
            holder.tvItemDetailDown.setVisibility(View.VISIBLE);
            holder.tvItemDetailTime.setText("支付后2小时内");
        }
        if(list.size()==1){
            holder.tvItemDetailDown.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_item_detail_count)
        TextView tvItemDetailCount;
        @Bind(R.id.tv_item_detail_time)
        TextView tvItemDetailTime;
        @Bind(R.id.tv_item_detail_up)
        TextView tvItemDetailUp;
        @Bind(R.id.img_item_detail)
        ImageView imgItemDetail;
        @Bind(R.id.tv_item_detail_down)
        TextView tvItemDetailDown;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
