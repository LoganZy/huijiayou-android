package com.wanglibao.huijiayou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

import com.wanglibao.huijiayou.R;

import static com.ashokvarma.bottomnavigation.utils.Utils.dp2px;

/**
 * Created by lugg on 2017/3/1.
 */

public class MyEditText extends EditText {

    private Paint paint = new Paint();
    float width = 0;
    float height = 0;

    String text = "元/月";
    Context context;

    //在java中new 调用
    public MyEditText(Context context) {
        super(context);
        if (isInEditMode()) { return; }
        this.context = context;
        initPaint();
    }

    //在xml中生命 调用
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) { return; }
        this.context = context;
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        canvas.drawColor(getResources().getColor(android.R.color.white));

        int startX = dp2px(context,6);
        int startY = dp2px(context,6);
        int textSize = dp2px(context,12);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        float textStartX = width-dp2px(context,20)-textWidth;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        RectF rectF = new RectF(startX,startY,width-startX,height-startY);
        canvas.drawRoundRect(rectF,dp2px(context,5),dp2px(context,5),paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        canvas.drawCircle(textStartX-10,height-(textSize/2),5,paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        canvas.drawCircle(textStartX+textWidth+10,height-(textSize/2),5,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvas.drawLine(textStartX-10+5,height-(textSize/2),textStartX+textWidth+10-5,height-(textSize/2),paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        canvas.drawText(text,textStartX,height-5,paint);
        super.onDraw(canvas);
    }

    private void initPaint(){
        paint.setColor(getResources().getColor(R.color.my_edittext_frame_color));
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
    }


}
