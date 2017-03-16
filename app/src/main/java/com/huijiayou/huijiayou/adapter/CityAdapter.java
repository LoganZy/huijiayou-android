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
    View.OnClickListener onClickListener;
    public CityAdapter(Context context, ArrayList<City> cityArrayList, View.OnClickListener onClickListener) {
        this.context = context;
        this.cityArrayList = cityArrayList;
        this.onClickListener = onClickListener;
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
            text = text.substring(index+1);
        }
        holder.tv_itemFragmentHomeCity_cityName.setText(text);
        holder.tv_itemFragmentHomeCity_cityName.setOnClickListener(onClickListener);
        holder.tv_itemFragmentHomeCity_cityName.setTag(position);
    }

    @Override
    public int getItemCount() {
        return cityArrayList.size();
    }

    public static class City {
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

    public static ArrayList<City> findCityByBelong(String belong,ArrayList<City> cityArrayList){
        ArrayList<City> arrayList = new ArrayList<>();
        for (int i = 0; i < cityArrayList.size(); i++){
            if (cityArrayList.get(i).getBelong().equals(belong)){
                arrayList.add(cityArrayList.get(i));
            }
        }
        return arrayList;
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
