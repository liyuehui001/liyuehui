package com.version.smec.wms.module.LogisticsDetail.bean;

import com.version.smec.wms.bean.HttpResponse;

/**
 * Created by 小黑 on 2017/8/25.
 */
public class LogisiticsDetailDto extends HttpResponse {

    private LogisitiicsDetailModule data;

    public LogisitiicsDetailModule getData() {
        return data;
    }

    public void setData(LogisitiicsDetailModule data) {
        this.data = data;
    }
}
