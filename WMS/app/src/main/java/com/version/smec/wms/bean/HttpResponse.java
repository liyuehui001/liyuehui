package com.version.smec.wms.bean;

/**
 * Created by xupeizuo on 2017/8/15.
 */

public class HttpResponse {

    private String code;
    private String userMsg;

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
