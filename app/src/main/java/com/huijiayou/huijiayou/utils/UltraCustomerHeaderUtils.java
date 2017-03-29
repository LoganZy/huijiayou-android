package com.huijiayou.huijiayou.utils;

import android.content.Context;

import com.huijiayou.huijiayou.R;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by hexinhai on 2017/3/29 0029.
 */

public class UltraCustomerHeaderUtils {
    public static void setUltraCustomerHeader(PtrClassicFrameLayout mPtrFrame, Context context){

        mPtrFrame.setLastUpdateTimeRelateObject(context);
        // the following are default settings
        mPtrFrame.setResistanceHeader(1.7f); // 您还可以单独设置脚,头
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(1000);  // 您还可以单独设置脚,头
        // default is false
        mPtrFrame.setPullToRefresh(false);

        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //以下为自定义header需要
        StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, LocalDisplay.dp2px(20), 0, LocalDisplay.dp2px(20));
        header.setTextColor(context.getResources().getColor(R.color.orange_FF7320));
        header.initWithString("ZSG");
        mPtrFrame.setDurationToCloseHeader(1500);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setBackgroundColor(context.getResources().getColor(R.color.orange_FF7320));
    }


}
