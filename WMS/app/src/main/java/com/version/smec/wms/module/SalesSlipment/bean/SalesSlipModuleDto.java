package com.version.smec.wms.module.SalesSlipment.bean;

import com.version.smec.wms.bean.HttpResponse;

/**
 * Created by 小黑 on 2017/8/18.
 */
public class SalesSlipModuleDto extends HttpResponse {
    private SalesSlipModule data;

    public SalesSlipModule getData() {
        return data;
    }

    public void setData(SalesSlipModule data) {
        this.data = data;
    }
}
