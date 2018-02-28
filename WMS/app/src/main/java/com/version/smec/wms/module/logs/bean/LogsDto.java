package com.version.smec.wms.module.logs.bean;

import com.version.smec.wms.bean.HttpResponse;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/28.
 */
public class LogsDto extends HttpResponse {
    private ArrayList<LogsModule> data;

    public ArrayList<LogsModule> getData() {
        return data;
    }

    public void setData(ArrayList<LogsModule> data) {
        this.data = data;
    }
}
