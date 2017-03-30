package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.huijiayou.huijiayou.R;

import static com.ashokvarma.bottomnavigation.utils.Utils.dp2px;

/**
 * Created by lugg on 2017/3/9.
 */

public class MyLinearLayout extends LinearLayout {

    private Paint paint = new Paint();
    float width = 0;
    float height = 0;
    int textSize;

    String text;
    Context context;

    public MyLinearLayout(Context context) {
        super(context);
        if (isInEditMode()) { return; }
        this.context = context;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        textSize = dp2px(context,13);
        paint.setTextSize(textSize);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) { return; }
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyLinearLayout);
        text = a.getString(R.styleable.MyLinearLayout_textable);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        textSize = dp2px(context,13);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        canvas.drawColor(getResources().getColor(android.R.color.transparent));

        int startX = dp2px(context,6);
        int startY = dp2px(context,6);
        float textWidth = paint.measureText(text);
        float textStartX = ((width-textWidth)/2);
        float textEndX = width/2+textWidth/2;

        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        RectF rectF = new RectF(startX,startY,width-startX,height-startY);
        canvas.drawRoundRect(rectF,dp2px(context,5),dp2px(context,5),paint);

        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        canvas.drawCircle(textStartX-36,textSize/2,5,paint);

        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        canvas.drawCircle(textEndX+36,textSize/2,5,paint);

        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.bg_color));
        canvas.drawLine(textStartX-36+5,textSize/2,textEndX+36-5,textSize/2,paint);

        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        canvas.drawText(text,textStartX,textSize-3,paint);

//        setPadding(dp2px(context,15),dp2px(context,15),dp2px(context,15),dp2px(context,15));
        super.onDraw(canvas);
    }

}
