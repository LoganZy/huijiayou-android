package com.huijiayou.huijiayou.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lugg on 2017/3/23.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
