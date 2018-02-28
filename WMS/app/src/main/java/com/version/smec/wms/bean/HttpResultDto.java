package com.version.smec.wms.bean;

import com.smec.wms.android.bean.UserProfile;

/**
 * Created by xupeizuo on 2017/9/11.
 */

public class HttpResultDto {

    private String code;
    private String userMsg;
    private UserProfile data;
    private String ErrMsg;

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public UserProfile getData() {
        return data;
    }

    public void setData(UserProfile data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }
}
