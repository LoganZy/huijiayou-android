package com.huijiayou.huijiayou.adapter;

import android.content.Context;
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
 * Created by lugg on 2017/3/14.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder>{
    Context context;
    ArrayList<City> cityArrayList;

    public CityAdapter(Context context, ArrayList<City> cityArrayList) {
        this.context = context;
        this.cityArrayList = cityArrayList;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home_city,parent,false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        String text = cityArrayList.get(position).getName();
        int index = text.indexOf("|");
        if (index >= 0){
            text = text.substring(index);
        }
        holder.tv_itemFragmentHomeCity_cityName.setText(text);
    }

    @Override
    public int getItemCount() {
        return cityArrayList.size();
    }

    public class City {
        private String name;
        private String city_id;
        private String belong;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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
    }


    class CityHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_itemFragmentHomeCity_cityName)
        TextView tv_itemFragmentHomeCity_cityName;

        public CityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
