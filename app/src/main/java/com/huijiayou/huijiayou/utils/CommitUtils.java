package com.huijiayou.huijiayou.utils;

import java.math.BigDecimal;

/**
 * Created by lugg on 2017/3/2.
 */

public class CommitUtils {

    public static double bigDecimal2(double value, int digit){
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(digit,BigDecimal.ROUND_UP).doubleValue();
    }

}
