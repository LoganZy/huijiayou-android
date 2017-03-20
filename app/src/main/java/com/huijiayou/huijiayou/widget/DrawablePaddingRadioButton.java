package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

/**
 * Created by lugg on 2017/3/4.
 */

public class DrawablePaddingRadioButton extends RadioButton {
    public DrawablePaddingRadioButton(Context context) {
        super(context);
    }

    public DrawablePaddingRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            //获取右边图片
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
                setGravity(Gravity.CENTER_VERTICAL);
            }
            Drawable drawableTop = drawables[1];
            if (drawableTop != null) {
                float textHeight = getPaint().getTextSize();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawableTop.getIntrinsicHeight();
                float bodyHeight = textHeight + drawableHeight + drawablePadding;
                canvas.translate(0, (getHeight()- bodyHeight) / 2);
                setGravity(Gravity.CENTER_HORIZONTAL);
            }
            Drawable drawableRight = drawables[2];
            if (drawableRight != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableRight.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate(-(getWidth() - bodyWidth) / 2, 0);
                setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            }
            Drawable drawableBottom = drawables[3];
            if (drawableBottom != null) {
                float textHeight = getPaint().getTextSize();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawableBottom.getIntrinsicHeight();
                float bodyHeight = textHeight + drawableHeight + drawablePadding;
                canvas.translate(0, -(getHeight()- bodyHeight) / 2);
                setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
            }

        }
        super.onDraw(canvas);
    }
}
