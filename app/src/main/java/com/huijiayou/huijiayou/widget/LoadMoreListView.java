package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.huijiayou.huijiayou.R;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private View footerView;
    private int firstVisibleItem;
    private Context context;
    private MyPullUpListViewCallBack myPullUpListViewCallBack;
    public LoadMoreListView(Context context) {
        super(context);
        this.context = context;
        initview();
    }



    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initview();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initview();
    }
    private void initview() {
        // 为ListView设置滑动监听
        setOnScrollListener(this);
        // 去掉底部分割线
        setFooterDividersEnabled(false);


    }

    /**
     * 初始化话底部页面
     */
    public void initBottomView() {
        if (footerView == null) {
            footerView = LayoutInflater.from(context).inflate(
                    R.layout.headview, null);
        }
        addFooterView(footerView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && firstVisibleItem != 0) {
            myPullUpListViewCallBack.scrollBottomState();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

        if (footerView != null) {
            //判断可视Item是否能在当前页面完全显示
            if (visibleItemCount == totalItemCount) {
                // removeFooterView(footerView);
                footerView.setVisibility(View.GONE);//隐藏底部布局
            } else {
                // addFooterView(footerView);
                footerView.setVisibility(View.VISIBLE);//显示底部布局
            }
        }
    }

    public void setMyPullUpListViewCallBack(
            MyPullUpListViewCallBack myPullUpListViewCallBack) {
        this.myPullUpListViewCallBack = myPullUpListViewCallBack;
    }


}
