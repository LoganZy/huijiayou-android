package com.wanglibao.huijiayou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可放在 ScrollView中 全部展开显示
 * Created by lugg on 2016/4/12.
 */
public class SVListView extends ListView {

    public SVListView(Context context) {
        super(context);
    }

    public SVListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SVListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
