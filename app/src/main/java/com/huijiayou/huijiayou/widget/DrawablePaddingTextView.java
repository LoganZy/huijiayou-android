package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lugg on 2017/3/4.
 */

public class DrawablePaddingTextView extends TextView {
    public DrawablePaddingTextView(Context context) {
        super(context);
    }

    public DrawablePaddingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            //获取右边图片
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                int textWidth = (int) getPaint().measureText(getText().toString());
                int drawableWidth  = drawableLeft.getIntrinsicWidth();
//                drawableLeft.setBounds(0,0,drawableLeft.getMinimumWidth(),drawableLeft.getMinimumHeight());
                setCompoundDrawablePadding((getWidth()-textWidth-drawableWidth)/2);
                //
            }
        }
        super.onDraw(canvas);
    }
}
