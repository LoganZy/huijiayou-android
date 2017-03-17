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
import com.huijiayou.huijiayou.fragment.HomeFragment;

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
    HomeFragment homeFragment;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, View.OnClickListener onClickListener, HomeFragment homeFragment) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
        this.homeFragment = homeFragment;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home_product,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        if (position == 0){
            holder.tv_itemFragmentHomeProduct_productName.setTextColor(context.getResources().getColor(R.color.textColor_F3844A));
            homeFragment.lastSelectedProductTextView =holder.tv_itemFragmentHomeProduct_productName;
        }else{
            holder.tv_itemFragmentHomeProduct_productName.setTextColor(context.getResources().getColor(R.color.textColor_51586A));
        }

        holder.tv_itemFragmentHomeProduct_productName.setTag(position);
        holder.tv_itemFragmentHomeProduct_productName.setOnClickListener(onClickListener);
        holder.tv_itemFragmentHomeProduct_productName.setText(productArrayList.get(position).getName());
        Drawable drawable = null;
        if (Constans.ID_ZHONGSHIHUA.equals(productArrayList.get(position).getBelong())){
            drawable = context.getResources().getDrawable(R.mipmap.ic_popover_sinopec);
        }else if (Constans.ID_ZHONGSHIYOU.equals(productArrayList.get(position).getBelong())){
            drawable = context.getResources().getDrawable(R.mipmap.ic_popover_cnpc);
        }else{
            drawable = context.getResources().getDrawable(R.mipmap.ic_popover_cnpc);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.tv_itemFragmentHomeProduct_productName.setCompoundDrawables(null,null,drawable,null);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class Product{
        private String name;
        private String belong;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBelong() {
            return belong;
        }

        public void setBelong(String belong) {
            this.belong = belong;
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_itemFragmentHomeProduct_productName)
        TextView tv_itemFragmentHomeProduct_productName;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
