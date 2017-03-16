package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/11.
 */

public class OilCardAdapter extends RecyclerView.Adapter<OilCardAdapter.MyViewHolder> {

    private Context context;
    private List<OilCardEntity> oilCardEntityList;
    private int showType;
    public static final int SHOWTYPE_MYOILCARD = 1;
    public static final int SHOWTYPE_SELECTOILCARD = 2;

    public OilCardAdapter(Context context, List<OilCardEntity> list,int showType){
        this.context = context;
        this.oilCardEntityList = list;
        this.showType = showType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity_oil_card,parent));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 0 && showType == SHOWTYPE_SELECTOILCARD){
            holder.imgView_itemActivityOilCard_selected.setVisibility(View.VISIBLE);
        }else{
            holder.imgView_itemActivityOilCard_selected.setVisibility(View.GONE);
        }

        holder.tv_itemActivityOilCard_card.setText(oilCardEntityList.get(position).getOil_card_number());
        holder.tv_itemActivityOilCard_nameAndType.setText(oilCardEntityList.get(position).getUser_name());

        Drawable drawable = null;
        if ("中石化".equals(oilCardEntityList.get(position).getName())){
            drawable = context.getResources().getDrawable(R.mipmap.ic_pay_sinopec);
        }else if ("中石油".equals(oilCardEntityList.get(position).getName())){
            drawable = context.getResources().getDrawable(R.mipmap.ic_pay_cnpc);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.tv_itemActivityOilCard_nameAndType.setCompoundDrawables(drawable,null,null,null);
    }

    @Override
    public int getItemCount() {
        return oilCardEntityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.imgView_itemActivityOilCard_selected)
        ImageView imgView_itemActivityOilCard_selected;
        @Bind(R.id.tv_itemActivityOilCard_nameAndType)
        TextView tv_itemActivityOilCard_nameAndType;
        @Bind(R.id.tv_itemActivityOilCard_card)
        TextView tv_itemActivityOilCard_card;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public class OilCardEntity{
        private String name;  //油卡类型名字  中石化  or  中石油
        private String oil_card_number;  //卡号
        private String user_name;  //用户的名字

        public OilCardEntity(String name, String oil_card_number, String user_name) {
            this.name = name;
            this.oil_card_number = oil_card_number;
            this.user_name = user_name;
        }

        public OilCardEntity() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOil_card_number() {
            return oil_card_number;
        }

        public void setOil_card_number(String oil_card_number) {
            this.oil_card_number = oil_card_number;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
}
