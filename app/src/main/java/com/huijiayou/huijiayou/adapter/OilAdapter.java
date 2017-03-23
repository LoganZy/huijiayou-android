package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/22.
 */

public class OilAdapter extends RecyclerView.Adapter<OilAdapter.OilViewHolder> {

    ArrayList<Oil> oilArrayList;
    Context context;


    public OilAdapter(ArrayList<Oil> oilArrayList, Context context) {
        this.oilArrayList = oilArrayList;
        this.context = context;
    }

    @Override
    public OilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OilViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity_oil_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(OilViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Oil oil = oilArrayList.get(position);
        holder.tv_itemActivityOilLayout_enableOilDrop.setText("剩余: "+oil.getEnable_oildrop());
        holder.tv_itemActivityOilLayout_from.setText(oil.getFrom_tag());
        if ("1".equals(oil.getType())){ //获取
            holder.tv_itemActivityOilLayout_oildrop.setText("+"+oil.getOildrop_num());
            holder.tv_itemActivityOilLayout_oildrop.setTextColor(context.getResources().getColor(R.color.orange_FF7320));
        }else if ("2".equals(oil.getType())){ //花费
            holder.tv_itemActivityOilLayout_oildrop.setText("-"+oil.getOildrop_num());
            holder.tv_itemActivityOilLayout_oildrop.setTextColor(context.getResources().getColor(R.color.green));
        }else if ("3".equals(oil.getType())){ //故障返还
            holder.tv_itemActivityOilLayout_oildrop.setText("+"+oil.getOildrop_num());
            holder.tv_itemActivityOilLayout_oildrop.setTextColor(context.getResources().getColor(R.color.orange_FF7320));
        }else{
            holder.tv_itemActivityOilLayout_oildrop.setText(oil.getOildrop_num());
            holder.tv_itemActivityOilLayout_oildrop.setTextColor(context.getResources().getColor(R.color.orange_FF7320));
        }
        try {
            holder.tv_itemActivityOilLayout_time.setText(simpleDateFormat.format(simpleDateFormat1.parse(oil.getCtime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return oilArrayList.size();
    }


    public static class Oil{
        private String id;
        private String ctime;
        private String user_id;
        private String type;
        private String from_tag;
        private String from_node_id;
        private String oildrop_num;
        private String enable_oildrop;
        private String get_id;
        private String expend_id;

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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFrom_tag() {
            return from_tag;
        }

        public void setFrom_tag(String from_tag) {
            this.from_tag = from_tag;
        }

        public String getFrom_node_id() {
            return from_node_id;
        }

        public void setFrom_node_id(String from_node_id) {
            this.from_node_id = from_node_id;
        }

        public String getOildrop_num() {
            return oildrop_num;
        }

        public void setOildrop_num(String oildrop_num) {
            this.oildrop_num = oildrop_num;
        }

        public String getEnable_oildrop() {
            return enable_oildrop;
        }

        public void setEnable_oildrop(String enable_oildrop) {
            this.enable_oildrop = enable_oildrop;
        }

        public String getGet_id() {
            return get_id;
        }

        public void setGet_id(String get_id) {
            this.get_id = get_id;
        }

        public String getExpend_id() {
            return expend_id;
        }

        public void setExpend_id(String expend_id) {
            this.expend_id = expend_id;
        }
    }


    class OilViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_itemActivityOilLayout_from)
        TextView tv_itemActivityOilLayout_from;

        @Bind(R.id.tv_itemActivityOilLayout_oildrop)
        TextView tv_itemActivityOilLayout_oildrop;

        @Bind(R.id.tv_itemActivityOilLayout_enableOilDrop)
        TextView tv_itemActivityOilLayout_enableOilDrop;

        @Bind(R.id.tv_itemActivityOilLayout_time)
        TextView tv_itemActivityOilLayout_time;

        public OilViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
