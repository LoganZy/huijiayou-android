package com.wanglibao.huijiayou.widget;


/**
 * Created by heixinhai on 2017/3/13 0013.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

public class MyImageView extends ScrollView{

    private ImageView iv;
    private int intrinsicHeight;
    private int originalHeight;
    private ValueAnimator va;

    public MyImageView(Context context) {
        this(context,null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if(isTouchEvent){
            if(deltaY<0){
                int newHeight=iv.getLayoutParams().height+Math.abs(deltaY);
                if(newHeight>intrinsicHeight*2.0f){
                    newHeight= (int) (intrinsicHeight*2.0f);
                }
                //获取图片的高度
                iv.getLayoutParams().height=newHeight;
                //让修改过的布局参数生效,重写布局和绘制界面
                iv.requestLayout();

            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    public void setImageView(ImageView imageView) {
        this.iv = imageView;
        //获取图片的原生高度,右键-属性-详细信息
        //获取ImageView中的"相片"
        Drawable drawable = iv.getDrawable();
        //获取图片的原生高度
        intrinsicHeight = drawable.getIntrinsicHeight();
        //获取最初的布局参数的高度
        originalHeight = iv.getLayoutParams().height;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(va!=null){
                    if(va.isRunning()){
                        va.cancel();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                va = ValueAnimator.ofInt(iv.getLayoutParams().height, originalHeight);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //获取值动画在值改变过程中的某个瞬间的值
                        int tempHeight= (int) animation.getAnimatedValue();
                        iv.getLayoutParams().height=tempHeight;
                        iv.requestLayout();
                    }
                });
                va.setDuration(1000);
                va.start();
                break;
        }
        return super.onTouchEvent(ev);
    }

}
