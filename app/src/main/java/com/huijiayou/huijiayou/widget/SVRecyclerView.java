package com.huijiayou.huijiayou.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 可放在 ScrollView中 全部展开显示
 * Created by lugg on 2016/4/12.
 */
public class SVRecyclerView extends RecyclerView {

    public SVRecyclerView(Context context) {
        super(context);
    }

    public SVRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SVRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
