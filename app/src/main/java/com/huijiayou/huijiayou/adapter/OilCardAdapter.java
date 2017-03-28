package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    View.OnClickListener onClickListener;
    public OilCardAdapter(Context context, List<OilCardEntity> list,int showType,View.OnClickListener onClickListener){
        this.context = context;
        this.oilCardEntityList = list;
        this.showType = showType;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity_oil_card,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OilCardEntity oilCardEntity = oilCardEntityList.get(position);
        holder.tv_itemActivityOilCard_card.setText(addSpace(oilCardEntity.getOil_card_number()));
        holder.tv_itemActivityOilCard_nameAndType.setText(oilCardEntity.getUser_name());

        Drawable drawable = null;

        if (oilCardEntity.getName().contains("石化")){
            drawable = context.getResources().getDrawable(R.mipmap.ic_pay_sinopec);
        }else if (oilCardEntity.getName().contains("石油")){
            drawable = context.getResources().getDrawable(R.mipmap.ic_pay_cnpc);
        }else{
            drawable = context.getResources().getDrawable(R.mipmap.ic_pay_cnpc);
        }
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.tv_itemActivityOilCard_nameAndType.setCompoundDrawables(drawable,null,null,null);
        if ("0".equals(oilCardEntity.is_show)){ //0 此油卡不可用
            holder.tv_itemActivityOilCard_outOfCommission.setVisibility(View.VISIBLE);
        }else{
            holder.rl_itemActivityOilCard_view.setTag(position);
            holder.rl_itemActivityOilCard_view.setOnClickListener(onClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return oilCardEntityList.size();
    }

    private String addSpace(String text){
        StringBuffer spaceText = new StringBuffer();
        while (text.length() > 0){
            if (text.length() >= 4){
                spaceText.append(text.substring(0,4)+" ");
                text = text.substring(4,text.length());
            }else {
                spaceText.append(text);
                break;
            }
        }
        return spaceText.toString();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.imgView_itemActivityOilCard_selected)
        ImageView imgView_itemActivityOilCard_selected;
        @Bind(R.id.tv_itemActivityOilCard_nameAndType)
        TextView tv_itemActivityOilCard_nameAndType;
        @Bind(R.id.tv_itemActivityOilCard_card)
        TextView tv_itemActivityOilCard_card;
        @Bind(R.id.rl_itemActivityOilCard_view)
        RelativeLayout rl_itemActivityOilCard_view;
        @Bind(R.id.tv_itemActivityOilCard_outOfCommission)
        TextView tv_itemActivityOilCard_outOfCommission;
        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public class OilCardEntity{
        private String name;  //油卡类型名字  中石化  or  中石油
        private String oil_card_number;  //卡号
        private String oil_user_name;  //用户的名字
        private String is_show;  //是否显示
        private String id;  //id

        public String getOil_user_name() {
            return oil_user_name;
        }

        public void setOil_user_name(String oil_user_name) {
            this.oil_user_name = oil_user_name;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public OilCardEntity(String name, String oil_card_number, String user_name) {
            this.name = name;
            this.oil_card_number = oil_card_number;
            this.oil_user_name = user_name;
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
            return oil_user_name;
        }

        public void setUser_name(String user_name) {
            this.oil_user_name = user_name;
        }
    }
}
