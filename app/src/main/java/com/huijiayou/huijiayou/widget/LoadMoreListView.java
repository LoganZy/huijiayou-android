package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ListView;

import com.huijiayou.huijiayou.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener,PtrHandler {
    private View footerView;
    private int firstVisibleItem;
    private boolean isLoadData = false;
    private boolean isRefresh = false;
    private Context context;
    private UltraRefreshListener mUltraRefreshListener;

//    private MyPullUpListViewCallBack myPullUpListViewCallBack;
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
        initBottomView();
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
//            myPullUpListViewCallBack.scrollBottomState();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

        if (footerView != null) {
            //加载更多的判断
            if(totalItemCount>1&&!isLoadData&&totalItemCount==firstVisibleItem+visibleItemCount){
                isRefresh =false;
                isLoadData = true;
                addFooterView(footerView);
                mUltraRefreshListener.addMore();
            }
        }
    }



    //刷新完成的后调用此方法还原布局
    public void refreshComplete(){
        isLoadData = false;
        if(isRefresh){
            //获取其父控件，刷新
            ViewParent parent = getParent();
            if(parent instanceof PtrClassicFrameLayout){
                ((PtrClassicFrameLayout) parent).refreshComplete();
            }
        }else{
            removeFooterView(footerView);
        }
    }

//    public void setMyPullUpListViewCallBack(
//            MyPullUpListViewCallBack myPullUpListViewCallBack) {
//        this.myPullUpListViewCallBack = myPullUpListViewCallBack;
//    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !isLoadData&&checkContentCanBePulledDown(frame, content, header);
    }
    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);

    }

    public static boolean canChildScrollUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return view.canScrollVertically(-1);
        }
    }
    /**
     * 设置ListView的监听回调
     */
    public void setUltraRefreshListener(UltraRefreshListener mUltraRefreshListener) {
        this.mUltraRefreshListener = mUltraRefreshListener;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        isLoadData  =true;
        isRefresh =true;
        //下拉刷新的回调
        if(mUltraRefreshListener!=null){

            mUltraRefreshListener.onRefresh();
        }
    }


    public interface UltraRefreshListener {

        //下拉刷新
        void onRefresh();

        //上拉加载
        void addMore();
    }
}
