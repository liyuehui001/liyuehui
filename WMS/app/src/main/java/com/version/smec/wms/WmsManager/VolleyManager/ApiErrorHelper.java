package com.version.smec.wms.WmsManager.VolleyManager;


import com.version.smec.wms.utils.ToastUtils;


import org.apache.http.HttpException;

import java.io.IOException;


/**
 * Created by xupeizuo on 2017/4/26.
 * 遵循单一职责原则SRP
 */

public final class ApiErrorHelper {


    /**
     * 分解错误信息
     * @param e
     */
    public static void handleError(Throwable e) {
        String msg="服务器异常,请稍后再试";
        if (e instanceof HttpException) {
            msg="服务暂不可用";
        } else if (e instanceof IOException) {
            msg="连接失败";
        } else if (e instanceof ApiException) {
            ApiException apiException=(ApiException) e;
            msg=apiException.getMessage();
        }
        showMsg(msg);
    }

    /**
     * 处理错误信息
     * @param msg
     */
    public static void showMsg(String msg){
        ToastUtils.showToast(msg);
    }

}
