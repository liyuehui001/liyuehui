package com.version.smec.wms.module.PendingApproval.bean;

import com.version.smec.wms.bean.HttpResponse;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2017/8/17.
 */

public class RequirementModelDto extends HttpResponse {

    private ArrayList<RequirementModel> data;

    public ArrayList<RequirementModel> getData() {
        return data;
    }

    public void setData(ArrayList<RequirementModel> data) {
        this.data = data;
    }
}
