package com.version.smec.wms.utils;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smec.wms.android.application.WmsApplication;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.widget.CustomePopupWindow;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xupeizuo on 2017/7/3.
 */

public class CommonUtils {


    /**
     * textview 汉字加粗字体
     * @param tv
     */
    public static void styleBoldTextView(TextView tv){
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }


    /**
     * 根据文件png  返回code
     * @param type
     * @return
     */
    public static int getCodeByFileType(String type){
        if(type == null || type.equals("")){
            return SmecDownloadManager.FILE_TYPE_ERROR;
        }
        if (type.equalsIgnoreCase("pdf")){
            return SmecDownloadManager.FILE_TYPE_PDF;
        }else if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpg")){
            return SmecDownloadManager.FILE_TYPE_IMAGE;
        }
        return SmecDownloadManager.FILE_TYPE_ERROR;
    }

    /**
     * 判断是否有文件存在
     * @param path
     * @return
     */
    public static boolean isExistFile(String path){
        try{
            File file = new File(path);
            if (file.exists()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断字符串为空？
     * @param str
     * @return
     */
    public static boolean notEmptyStr(String str){
        return str!=null && !str.isEmpty();
    }


    public static boolean notEmpty(List<?> T){
        return T!=null && !T.isEmpty();
    }

    /**
     * 奖json转换为Map
     * @param data
     * @return
     */
    public static Map<String, String> parseData(String data){
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, String> map = g.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
        return map;
    }

    /**
     * 将对象转化为json
     * @param o
     * @return
     */
    public static String ObjectToJson(Object o){
        return new Gson().toJson(o);
    }

    public static ProgressDialog showProgessDialog(ProgressDialog progressDialog,Context context,String message){

        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(context, "", message, false, false);
        } else {
            progressDialog.setMessage(message);
        }

        return progressDialog;
    }

    /**
     * 隐藏ProgressDialog
     * @param progressDialog
     */
    public static  void hideProgressDialog(ProgressDialog progressDialog){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    /**
     *
     * 设置弹出框的大小
     * @param dialog
     * @param activity
     * @return
     */
    public static Dialog getWidthHeight(Dialog dialog, Activity activity){
        Window window = dialog.getWindow();
        // 获得代表当前window属性的对象
        WindowManager.LayoutParams params = window.getAttributes();
        Point point = new Point();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 将window的宽高信息保存在point中
        // display.getSize(point);
        // 将设置后的大小赋值给window的宽高
        params.width = (int) (displayMetrics.widthPixels * 0.8);
//        params.height = (int) (displayMetrics.heightPixels * 0.4);
        window.setAttributes(params);

        return dialog;
    }


    /**
     * 打电话
     */
    public static void callPhone(String phone,Context mcontext) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(intent);
    }





    /**
     * 获取UUID
     * @return
     */
    public static String getUUID() {

        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return str+","+temp;
    }

    public static boolean isForeground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {

                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return false;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 隐藏键盘
     * @param view
     */
    public static void hideImmManager(View view) {
        InputMethodManager imm = (InputMethodManager) WmsApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
