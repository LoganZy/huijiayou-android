package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    View.OnClickListener onItemClickListener;

    public MessageTransactionAdapter(ArrayList<Message> messageArrayList, Context context, View.OnClickListener onBtnClickListener, View.OnClickListener onItemClickListener) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.onBtnClickListener = onBtnClickListener;
        this.onItemClickListener = onItemClickListener;
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

        if ("0".equals(message.getJump_type())){ //纯文本
            holder.tv_itemMessageTransactionActivityLayout_button.setVisibility(View.GONE);
        }else if ("1".equals(message.getJump_type())){ //去加油
            holder.tv_itemMessageTransactionActivityLayout_button.setText("去加油");
        }else if ("2".equals(message.getJump_type())){ // 查看订单列表
            holder.tv_itemMessageTransactionActivityLayout_button.setText("查看订单");
        }else if ("3".equals(message.getJump_type())){//查看订单详情
            holder.tv_itemMessageTransactionActivityLayout_button.setText("查看详情");
        }else if ("4".equals(message.getJump_type())){ //邀请好友
            holder.tv_itemMessageTransactionActivityLayout_button.setText("邀请好友");
        }
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

        holder.ll_itemMessageTransactionActivityLayout_content.setTag(position);
        holder.ll_itemMessageTransactionActivityLayout_content.setOnClickListener(onItemClickListener);
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

        @Bind(R.id.ll_itemMessageTransactionActivityLayout_content)
        LinearLayout ll_itemMessageTransactionActivityLayout_content;

        public MessageTransactionAndActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
