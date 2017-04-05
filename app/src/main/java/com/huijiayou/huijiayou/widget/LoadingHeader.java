package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.indicator.PtrTensionIndicator;

public class LoadingHeader extends FrameLayout implements PtrUIHandler {
    private View view;
    private ImageView tvLoading;
    private AnimationDrawable animation;
    private PtrTensionIndicator mPtrTensionIndicator;
    PtrFrameLayout mPtrFrameLayout;
    public LoadingHeader(@NonNull Context context) {
        super(context);
        initMyview();
    }

    public LoadingHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initMyview();
    }

    public LoadingHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyview();
    }

    private void initMyview() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.headview, this, false);
        addView(view);
        tvLoading = (ImageView) findViewById(R.id.tv_headview_loading);
        animation = (AnimationDrawable) tvLoading.getDrawable();

    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        //重置
        tvLoading.setVisibility(View.GONE);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        //准备刷新
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        //开始刷新 显示刷新进度跟文本
        tvLoading.setVisibility(View.VISIBLE);
        animation.start();
        invalidate();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
        //刷新完成  设置文本 设置进度隐藏
        tvLoading.setVisibility(View.GONE);
        animation.stop();

        invalidate();
    }
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        invalidate();
        if (currentPos < mOffsetToRefresh) {
            //未到达刷新线
            if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {

                animation.stop();
            }
        } else if (currentPos > mOffsetToRefresh) {
            //到达或超过刷新线
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                animation.start();
            }
        }
    }

}
