package com.huijiayou.huijiayou.utils;

import java.math.BigDecimal;

/**
 * Created by lugg on 2017/3/2.
 */

public class CommitUtils {

    public static double bigDecimal2(double value){
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(2,BigDecimal.ROUND_UP).doubleValue();
    }


}
