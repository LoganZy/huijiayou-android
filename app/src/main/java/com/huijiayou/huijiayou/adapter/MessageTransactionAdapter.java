package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.bean.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/26.
 */

public class MessageTransactionAdapter extends RecyclerView.Adapter<MessageTransactionAdapter.MessageTransactionAndActivityViewHolder> {

    ArrayList<Message> messageArrayList;
    Context context;
    View.OnClickListener onBtnClickListener;

    public MessageTransactionAdapter(ArrayList<Message> messageArrayList, Context context, View.OnClickListener onBtnClickListener) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.onBtnClickListener = onBtnClickListener;
    }

    @Override
    public MessageTransactionAndActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageTransactionAndActivityViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_message_transaction_activity_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MessageTransactionAndActivityViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Message message = messageArrayList.get(position);
        Date date = new Date(Long.parseLong(message.getCreated_at()+"000"));

        holder.tv_itemMessageTransactionActivityLayout_button.setText(message.getJump());
        holder.tv_itemMessageTransactionActivityLayout_content.setText(message.getContent());
        holder.tv_itemMessageTransactionActivityLayout_time.setText(simpleDateFormat1.format(date));
        holder.tv_itemMessageTransactionActivityLayout_title.setText(message.getTitle());
        holder.tv_itemMessageTransactionActivityLayout_button.setTag(position);
        holder.tv_itemMessageTransactionActivityLayout_button.setOnClickListener(onBtnClickListener);
        if ("0".equals(message.getRead_status())){
            holder.tv_itemMessageTransactionActivityLayout_content.setTextColor(context.getResources().getColor(R.color.textColor_51586A));
        }else{
            holder.tv_itemMessageTransactionActivityLayout_content.setTextColor(context.getResources().getColor(R.color.gray));
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class MessageTransactionAndActivityViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_itemMessageTransactionActivityLayout_time)
        TextView tv_itemMessageTransactionActivityLayout_time;

        @Bind(R.id.tv_itemMessageTransactionActivityLayout_title)
        TextView tv_itemMessageTransactionActivityLayout_title;

        @Bind(R.id.tv_itemMessageTransactionActivityLayout_content)
        TextView tv_itemMessageTransactionActivityLayout_content;

        @Bind(R.id.tv_itemMessageTransactionActivityLayout_button)
        TextView tv_itemMessageTransactionActivityLayout_button;

        public MessageTransactionAndActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
