package com.version.smec.wms.module.LogisticsList.bean;

import com.version.smec.wms.bean.HttpResponse;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/23.
 */
public class LogisticsDto extends HttpResponse{
    private ArrayList<LogisticsModule> data;

    public ArrayList<LogisticsModule> getData() {
        return data;
    }

    public void setData(ArrayList<LogisticsModule> data) {
        this.data = data;
    }
}
