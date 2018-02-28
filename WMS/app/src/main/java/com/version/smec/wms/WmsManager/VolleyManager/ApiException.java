package com.version.smec.wms.WmsManager.VolleyManager;

/**
 * Created by xupeizuo on 2017/4/26.
 */

public class ApiException extends RuntimeException {

    private String errorCode;

    public ApiException(String code, String msg){
        super(msg);
        errorCode=code;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
