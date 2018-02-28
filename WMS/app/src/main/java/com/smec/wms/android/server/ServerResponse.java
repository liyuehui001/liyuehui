package com.smec.wms.android.server;

import java.util.Map;

/**
 * Created by Adobe on 2016/1/16.
 */
public class ServerResponse {
    private String iface;   //标识是哪个接口返回的数据
    private Object data;    //业务数据

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    private Map<String,String> requestParams;   //请求数据参数
    public  ServerResponse(){

    }

    public ServerResponse(String iface){
        this.iface=iface;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getIface() {
        return iface;
    }

    public void setIface(String iface) {
        this.iface = iface;
    }
}
