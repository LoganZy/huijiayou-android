package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.bean.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/3/22.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Message> messageArrayList;
    Context context;
    View.OnClickListener onBtnClickListener;
    View.OnClickListener onItemClickListener;

    private int itemType_system = 1;
    private int itemType_transactionAndActivity = 2;


    public MessageAdapter(ArrayList<Message> messageArrayList, Context context, View.OnClickListener onBtnClickListener, View.OnClickListener onItemClickListener) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.onBtnClickListener = onBtnClickListener;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if ("oil_register".equals(message.getMtype())){
            return itemType_system;
        }else if ("oil_buy_forthwith".equals(message.getMtype()) ||
                "oil_buy_noforthwith".equals(message.getMtype()) ||
                "oil_recharge_forthwith".equals(message.getMtype()) ||
                "oil_recharge_noforthwith".equals(message.getMtype()) ||
                "oil_packets".equals(message.getMtype()) ||
                "oil_oildrop_friend".equals(message.getMtype())){
            return itemType_transactionAndActivity;
        }else{
            return itemType_system;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == itemType_system){
            return new MessageSystemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_system_layout,parent,false));
        }else if (viewType == itemType_transactionAndActivity){
            return new MessageTransactionAndActivityViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_message_transaction_activity_layout,parent,false));
        }else {
            return new MessageSystemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_system_layout,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Message message = messageArrayList.get(position);
        Date date = new Date(Long.parseLong(message.getCreated_at()+"000"));
        if (holder instanceof  MessageTransactionAndActivityViewHolder){
            MessageTransactionAndActivityViewHolder messageTransactionAndActivityViewHolder = (MessageTransactionAndActivityViewHolder) holder;
            messageTransactionAndActivityViewHolder.tv_itemMessageTransactionActivityLayout_button.setText(message.getJump());
            messageTransactionAndActivityViewHolder.tv_itemMessageTransactionActivityLayout_content.setText(message.getContent());
            messageTransactionAndActivityViewHolder.tv_itemMessageTransactionActivityLayout_time.setText(simpleDateFormat1.format(date));
            messageTransactionAndActivityViewHolder.tv_itemMessageTransactionActivityLayout_title.setText(message.getTitle());
            messageTransactionAndActivityViewHolder.tv_itemMessageTransactionActivityLayout_button.setTag(position);
            messageTransactionAndActivityViewHolder.tv_itemMessageTransactionActivityLayout_button.setOnClickListener(onBtnClickListener);
        }else{
            MessageSystemViewHolder messageSystemViewHolder = (MessageSystemViewHolder) holder;
            messageSystemViewHolder.tv_itemMessageSystemLayout_content.setText(message.getContent());
            messageSystemViewHolder.tv_itemMessageSystemLayout_time.setText(simpleDateFormat.format(date));
            messageSystemViewHolder.tv_itemMessageSystemLayout_title.setText(message.getTitle());
            messageSystemViewHolder.rl_itemMessageSystemLayout_view.setTag(position);
            messageSystemViewHolder.rl_itemMessageSystemLayout_view.setOnClickListener(onItemClickListener);
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

    class MessageSystemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_itemMessageSystemLayout_title)
        TextView tv_itemMessageSystemLayout_title;

        @Bind(R.id.tv_itemMessageSystemLayout_time)
        TextView tv_itemMessageSystemLayout_time;

        @Bind(R.id.tv_itemMessageSystemLayout_content)
        TextView tv_itemMessageSystemLayout_content;

        @Bind(R.id.rl_itemMessageSystemLayout_view)
        RelativeLayout rl_itemMessageSystemLayout_view;

        public MessageSystemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
