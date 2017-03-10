package com.wanglibao.huijiayou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.bean.Record;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/8 0008.
 */
public class RecordAdapter extends BaseAdapter {
    private final List<Record> list;
    private final Context context;

    public RecordAdapter(Context context, List<Record> recordList) {
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
                case 2:
                    convertView = View.inflate(context, R.layout.item2_activity_record_message, null);
                    holder2 = new ViewHolder2(convertView);
            }
        }else{
            switch (type){
                case 1:
                    holder = (ViewHolder) convertView.getTag(1);
                case 2:
                    holder2 = (ViewHolder2) convertView.getTag(2);

            }



        }

        return convertView;
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
