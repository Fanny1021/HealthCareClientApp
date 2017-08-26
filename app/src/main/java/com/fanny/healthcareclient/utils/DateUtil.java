package com.fanny.healthcareclient.utils;

/**
 * Created by Fanny on 17/7/27.
 */

public class DateUtil {

    public static String getWeek(int i){
        String[] WeekArr={"日","一","二","三","四","五","六"};
        return WeekArr[i];
    }
}
