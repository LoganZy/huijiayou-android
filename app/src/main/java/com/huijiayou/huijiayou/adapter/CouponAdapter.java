package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/20.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder>{

    ArrayList<Coupon> coupons;
    Context context;
    View.OnClickListener onClickListener;

    public CouponAdapter(ArrayList<Coupon> coupons, Context context, View.OnClickListener onClickListener) {
        this.coupons = coupons;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_coupon,parent,false));
    }

    @Override
    public void onBindViewHolder(CouponViewHolder holder, int position) {
        holder.rl_itemActivityCoupon_view.setTag(position);
        holder.rl_itemActivityCoupon_view.setOnClickListener(onClickListener);
        Coupon coupon = coupons.get(position);
        holder.tv_itemActivityCoupon_name.setText(coupon.getPackets_name());
        Drawable drawable = null;
        if ("0".equals(coupon.getPackets_type())){//0直抵
            double amount = Double.parseDouble(coupon.getAmount());
            BigDecimal bigDecimal = new BigDecimal(amount);
            amount = bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
            holder.tv_itemActivityCoupon_moneyNumber.setText(amount+"");
            holder.tv_itemActivityCoupon_condition.setText("满"+coupon.getLimit_money()+"元可用"); //TODO + 限几期可用
            holder.tv_itemActivityCoupon_moneyTag.setVisibility(View.VISIBLE);
            drawable = context.getResources().getDrawable(R.mipmap.ic_coupon_list);
        }else if ("1".equals(coupon.getPackets_type())){//1折扣 绿色
            double rate = Double.parseDouble(coupon.getRate()) * 10;
            BigDecimal bigDecimal = new BigDecimal(rate);
            holder.tv_itemActivityCoupon_moneyNumber.setText(bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).doubleValue()+"");
            holder.tv_itemActivityCoupon_condition.setVisibility(View.INVISIBLE);
            holder.tv_itemActivityCoupon_discountTag.setVisibility(View.VISIBLE);
            drawable = context.getResources().getDrawable(R.mipmap.ic_coupon_list_green);
        }else {//0直抵 //TODO  判断是否为 过期的
            holder.tv_itemActivityCoupon_moneyTag.setVisibility(View.VISIBLE);
            drawable = context.getResources().getDrawable(R.mipmap.ic_coupon_list);
        }
        holder.tv_itemActivityCoupon_oilTypeCondition.setText("");//TODO  限中石化可用
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.ll_itemActivityCoupon_right.setBackgroundDrawable(drawable);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String start = simpleDateFormat.format(simpleDateFormat1.parse(coupon.getEffective_start()));
            String end = simpleDateFormat.format(simpleDateFormat1.parse(coupon.getEffective_end()));
            holder.tv_itemActivityCoupon_time.setText(start+" - "+ end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public static class Coupon implements Serializable {
        private String id;
        private String user_id;
        private String uuid;
        private String packets_id;
        private String packets_name;
        private String amount;
        private String rate;
        private String effective_start;
        private String effective_end;
        private String limit_desc;
        private String is_use;
        private String create_time;
        private String update_time;
        private String products_id;
        private String packets_type; //0直抵  1折扣
        private String days;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getPackets_id() {
            return packets_id;
        }

        public void setPackets_id(String packets_id) {
            this.packets_id = packets_id;
        }

        public String getPackets_name() {
            return packets_name;
        }

        public void setPackets_name(String packets_name) {
            this.packets_name = packets_name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getEffective_start() {
            return effective_start;
        }

        public void setEffective_start(String effective_start) {
            this.effective_start = effective_start;
        }

        public String getEffective_end() {
            return effective_end;
        }

        public void setEffective_end(String effective_end) {
            this.effective_end = effective_end;
        }

        public String getLimit_desc() {
            return limit_desc;
        }

        public void setLimit_desc(String limit_desc) {
            this.limit_desc = limit_desc;
        }

        public String getIs_use() {
            return is_use;
        }

        public void setIs_use(String is_use) {
            this.is_use = is_use;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getProducts_id() {
            return products_id;
        }

        public void setProducts_id(String products_id) {
            this.products_id = products_id;
        }

        public String getPackets_type() {
            return packets_type;
        }

        public void setPackets_type(String packets_type) {
            this.packets_type = packets_type;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getLimit_money() {
            return limit_money;
        }

        public void setLimit_money(String limit_money) {
            this.limit_money = limit_money;
        }

        public String getLimit_node() {
            return limit_node;
        }

        public void setLimit_node(String limit_node) {
            this.limit_node = limit_node;
        }

        public String getUse_time() {
            return use_time;
        }

        public void setUse_time(String use_time) {
            this.use_time = use_time;
        }

        public String getUse_order_id() {
            return use_order_id;
        }

        public void setUse_order_id(String use_order_id) {
            this.use_order_id = use_order_id;
        }

        private String limit_money;
        private String limit_node;
        private String use_time;
        private String use_order_id;
    }

    class CouponViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_itemActivityCoupon_name)
        TextView tv_itemActivityCoupon_name;

        @Bind(R.id.tv_itemActivityCoupon_condition)
        TextView tv_itemActivityCoupon_condition;

        @Bind(R.id.tv_itemActivityCoupon_oilTypeCondition)
        TextView tv_itemActivityCoupon_oilTypeCondition;

        @Bind(R.id.tv_itemActivityCoupon_moneyNumber)
        TextView tv_itemActivityCoupon_moneyNumber;

        @Bind(R.id.tv_itemActivityCoupon_moneyTag)
        TextView tv_itemActivityCoupon_moneyTag;

        @Bind(R.id.tv_itemActivityCoupon_discountTag)
        TextView tv_itemActivityCoupon_discountTag;

        @Bind(R.id.tv_itemActivityCoupon_time)
        TextView tv_itemActivityCoupon_time;

        @Bind(R.id.ll_itemActivityCoupon_right)
        LinearLayout ll_itemActivityCoupon_right;

        @Bind(R.id.rl_itemActivityCoupon_view)
        RelativeLayout rl_itemActivityCoupon_view;
        public CouponViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    /**
     * 作为item之间的间隔
     */
    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }
}
