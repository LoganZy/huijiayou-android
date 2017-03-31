package com.huijiayou.huijiayou.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by lugg on 2017/3/2.
 */

public class CommitUtils {

    public static double bigDecimal2(double value, int digit){
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(digit,BigDecimal.ROUND_UP).doubleValue();
    }

    public static double decimal1(double vlaue){
        return Double.parseDouble(new DecimalFormat("0.0").format(vlaue));
    }

    public static double decimal2(double vlaue){
        return Double.parseDouble(new DecimalFormat("0.00").format(vlaue));
    }
}
