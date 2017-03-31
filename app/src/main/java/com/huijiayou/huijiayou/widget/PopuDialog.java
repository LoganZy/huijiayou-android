package com.huijiayou.huijiayou.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;

/**
 * Created by hexinhai 2017/3/14 0014.
 */

public class PopuDialog extends Dialog {
    private TextView titleTv;
    private TextView messageTv;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    public PopuDialog(@NonNull Context context) {
        super(context);
    }

    public PopuDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.MyDialog);
    }

    protected PopuDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oil_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();


    }


    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {

        titleTv = (TextView) findViewById(R.id.title);
        messageTv = (TextView) findViewById(R.id.tv_activity_oil_num);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

}


