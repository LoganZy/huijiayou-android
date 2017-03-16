package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/14.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    Context context;
    ArrayList<Product> productArrayList;
    View.OnClickListener onClickListener;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, View.OnClickListener onClickListener) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home_product,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.tv_itemFragmentHomeProduct_productName.setText(productArrayList.get(position).getProduct_name());
        Drawable drawable = null;
        if (Constans.ID_ZHONGSHIHUA.equals(productArrayList.get(position).getBelong())){
            drawable = context.getResources().getDrawable(R.mipmap.ic_popover_sinopec);
        }else if (Constans.ID_ZHONGSHIYOU.equals(productArrayList.get(position).getBelong())){
            drawable = context.getResources().getDrawable(R.mipmap.ic_popover_cnpc);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.tv_itemFragmentHomeProduct_productName.setCompoundDrawables(null,null,drawable,null);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class Product{
        private String id;
        private String ctime;
        private String utime;
        private String product_name;
        private String product_time;
        private String product_discount;
        private String is_trade;
        private String city_id;
        private String belong;
        private String oil_trade;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getBelong() {
            return belong;
        }

        public void setBelong(String belong) {
            this.belong = belong;
        }

        public String getOil_trade() {
            return oil_trade;
        }

        public void setOil_trade(String oil_trade) {
            this.oil_trade = oil_trade;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_time() {
            return product_time;
        }

        public void setProduct_time(String product_time) {
            this.product_time = product_time;
        }

        public String getProduct_discount() {
            return product_discount;
        }

        public void setProduct_discount(String product_discount) {
            this.product_discount = product_discount;
        }

        public String getIs_trade() {
            return is_trade;
        }

        public void setIs_trade(String is_trade) {
            this.is_trade = is_trade;
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_itemFragmentHomeProduct_productName)
        TextView tv_itemFragmentHomeProduct_productName;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
