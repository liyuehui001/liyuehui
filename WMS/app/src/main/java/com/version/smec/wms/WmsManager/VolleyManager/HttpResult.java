package com.version.smec.wms.WmsManager.VolleyManager;

import com.version.smec.wms.bean.SmecUser;

/**
 * Created by xupeizuo on 2017/7/3.
 */

public class HttpResult {

    private String code;
    private SmecUser data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SmecUser getData() {
        return data;
    }

    public void setData(SmecUser data) {
        this.data = data;
    }
}
