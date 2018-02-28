package com.version.smec.wms.module.Acceptance.bean;

import com.version.smec.wms.bean.HttpResponse;

/**
 * Created by Administrator on 2017/8/20.
 */
public class AcceptanceModelDto extends HttpResponse {
    private AcceptanceModule data;

    public AcceptanceModule getData() {
        return data;
    }

    public void setData(AcceptanceModule data) {
        this.data = data;
    }
}
