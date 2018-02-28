package com.version.smec.wms.utils;

/**
 * Created by 小黑 on 2017/8/18.
 */
public class DataBingUseUtil {
    public static boolean isStrUrgent(String str_is_urgent){
        if (str_is_urgent!=null && !str_is_urgent.equals("")){
            if (str_is_urgent.equals("true")){
                return true;
            }

        }
        return false;
    }
}
