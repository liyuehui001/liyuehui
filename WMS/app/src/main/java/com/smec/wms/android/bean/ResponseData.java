package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/15.
 */
public class ResponseData {
    protected String code=Status.OK;

    protected String ErrMsg;    //错误信息
    public static final ResponseData SERVER_JSON_PARSE_ERROR=new ResponseData(Status.ERROR,Status.JSON_PARSE_ERROR);
    public ResponseData(){

    }
    public ResponseData(String code,String errMsg) {
        ErrMsg = errMsg;
        this.code=code;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
