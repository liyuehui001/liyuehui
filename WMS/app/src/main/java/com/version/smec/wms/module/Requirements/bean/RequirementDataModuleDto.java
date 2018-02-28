package com.version.smec.wms.module.Requirements.bean;

import com.version.smec.wms.bean.HttpResponse;

/**
 * Created by 小黑 on 2017/8/17.
 */
public class RequirementDataModuleDto extends HttpResponse {
    private RequirementDetailModel data;

    public RequirementDetailModel getData() {
        return data;
    }

    public void setData(RequirementDetailModel data) {
        this.data = data;
    }
}
