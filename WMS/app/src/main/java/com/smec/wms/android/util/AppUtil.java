package com.smec.wms.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/15.
 */
public class AppUtil {

    public static String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(("yyyy-MM-dd HH:mm:ss"));
        return fmt.format(date);
    }

    public static Integer str2int(String str) {
        Integer result = null;
        try {
            result = Integer.parseInt(str);
        } catch (Exception ex) {
            Log.e("str2int0", "parse error");
        }
        return result;
    }

    public static boolean strNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String stringPlus(String src,String value){
        try{
            Integer intSrc=Integer.parseInt(src);
            Integer intValue=Integer.parseInt(value);
            return String.valueOf(intSrc + intValue);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return src;
    }

    public static void openIMM(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideIMM(Context context,View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void putValue(Context context, Map<String, String> data) {
        SharedPreferences sp = context.getSharedPreferences("WMS_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (String key : data.keySet()) {
            String value = data.get(key);
            editor.putString(key, value);
        }
        editor.commit();


    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("WMS_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static  String getValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("WMS_DATA", Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

}
