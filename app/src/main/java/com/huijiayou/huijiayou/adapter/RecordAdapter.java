package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.bean.Record;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/8 0008.
 */
public class RecordAdapter extends BaseAdapter {
     private  List<Record> list;
  //  List list;
    private  Context context;

    public RecordAdapter(Context context,/*List recordlist*/ List<Record> recordList) {
        this.list = recordList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;
        Record record = (Record) getItem(position);
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 1:
                    convertView = View.inflate(context, R.layout.item_activity_record_message, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(1,holder);
                    break;
                case 2:
                    convertView = View.inflate(context, R.layout.item2_activity_record_message, null);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(2,holder2);
            }
        }else{
            switch (type){
                case 1:
                    holder = (ViewHolder) convertView.getTag(1);
                    holder.tvActivityRecordCompany.setText(record.getProduct_name());
                    holder.tvActivityRecordCardNumber.setText(record.getCard_number());
                    holder.tvActivityRecordCardtype.setText(record.getTotal_time());
                    String status = record.getStatus();
                    if(TextUtils.equals(status,"4")){

                        holder.btActivityRecordGotoPay.setVisibility(View.GONE);
                        holder.tvActivityRecordIndentClose.setVisibility(View.VISIBLE);
                        holder.tvActivityRecordCompletionRate.setVisibility(View.GONE);
                    }else {
                        holder.btActivityRecordGotoPay.setVisibility(View.VISIBLE);
                        holder.tvActivityRecordIndentClose.setVisibility(View.GONE);
                        holder.tvActivityRecordCompletionRate.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    holder2 = (ViewHolder2) convertView.getTag(2);
                    holder2.tvActivityRecordCompany2.setText(record.getProduct_name());
                    holder2.tvActivityRecordCardNumber2.setText(record.getCard_number());
                    holder2.tvActivityRecordCardtype2.setText(record.getTotal_time());
                    holder2.tvActivityRecordPayNumber.setText(record.getDiscount_after_amount());
                    if(TextUtils.equals(record.getCount(),record.getTotal_time())){
                        holder2.tvActivityRecordCompletionRate2.setBackgroundResource(R.mipmap.ic_recording_red);
                        holder2.tvActivityRecordCompletionRate2.setText("");


                    }else {
                        holder2.tvActivityRecordCompletionRate2.setText(record.getCount() + " / " + record.getTotal_time());
                        holder2.tvActivityRecordCompletionRate2.setBackgroundResource(R.drawable.ic_recording_green);
                    }
            }



        }

        return convertView;
       //return View.inflate(context, R.layout.item2_activity_record_message, null);
    }



    static class ViewHolder {
        @Bind(R.id.tv_activityRecord_company)
        TextView tvActivityRecordCompany;
        @Bind(R.id.tv_activityRecord_CompletionRate)
        TextView tvActivityRecordCompletionRate;
        @Bind(R.id.tv_activityRecord_cardNumber)
        TextView tvActivityRecordCardNumber;
        @Bind(R.id.tv_activityRecord_cardtype)
        TextView tvActivityRecordCardtype;
        @Bind(R.id.tv_activityRecord_indentClose)
        TextView tvActivityRecordIndentClose;
        @Bind(R.id.bt_activityRecord_gotoPay)
        Button btActivityRecordGotoPay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder2 {
        @Bind(R.id.tv_activityRecord_company2)
        TextView tvActivityRecordCompany2;
        @Bind(R.id.tv_activityRecord_CompletionRate2)
        TextView tvActivityRecordCompletionRate2;
        @Bind(R.id.tv_activityRecord_cardNumber2)
        TextView tvActivityRecordCardNumber2;
        @Bind(R.id.tv_activityRecord_cardtype2)
        TextView tvActivityRecordCardtype2;
        @Bind(R.id.tv_activityRecord_payNumber)
        TextView tvActivityRecordPayNumber;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
