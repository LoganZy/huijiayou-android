package com.huijiayou.huijiayou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageSystemViewHolder> {

    ArrayList<Message> messageArrayList;
    Context context;
    View.OnClickListener onItemClickListener;
    String type;
    public MessageAdapter(ArrayList<Message> messageArrayList, Context context, View.OnClickListener onItemClickListener) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public MessageAdapter(ArrayList<Message> messageArrayList, Context context, View.OnClickListener onItemClickListener, String type) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.type = type;
    }

//    @Override
//    public int getItemViewType(int position) {
//        Message message = messageArrayList.get(position);
//        if ("oil_register".equals(message.getMtype())){
//            return itemType_system;
//        }else if ("oil_buy_forthwith".equals(message.getMtype()) ||
//                "oil_buy_noforthwith".equals(message.getMtype()) ||
//                "oil_recharge_forthwith".equals(message.getMtype()) ||
//                "oil_recharge_noforthwith".equals(message.getMtype()) ||
//                "oil_packets".equals(message.getMtype()) ||
//                "oil_oildrop_friend".equals(message.getMtype())){
//            return itemType_transactionAndActivity;
//        }else{
//            return itemType_system;
//        }
//    }

    @Override
    public MessageSystemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageSystemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_system_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MessageSystemViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Message message = messageArrayList.get(position);
        Date date = new Date(Long.parseLong(message.getCreated_at()+"000"));
        holder.tv_itemMessageSystemLayout_content.setText(message.getContent());
        holder.tv_itemMessageSystemLayout_time.setText(simpleDateFormat.format(date));
        holder.tv_itemMessageSystemLayout_title.setText(message.getTitle());
        holder.rl_itemMessageSystemLayout_view.setTag(position);
        holder.rl_itemMessageSystemLayout_view.setOnClickListener(onItemClickListener);
        if ("0".equals(message.getRead_status())){
            holder.tv_itemMessageSystemLayout_content.setTextColor(context.getResources().getColor(R.color.textColor_51586A));
            if ("all".equals(type))
                holder.img_itemMessageSystemLayout_unRead.setVisibility(View.VISIBLE);
        }else{
            holder.tv_itemMessageSystemLayout_content.setTextColor(context.getResources().getColor(R.color.gray));
        }
    }


    @Override
    public int getItemCount() {
        return messageArrayList.size();
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

        @Bind(R.id.img_itemMessageSystemLayout_unRead)
        ImageView img_itemMessageSystemLayout_unRead;

        public MessageSystemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
