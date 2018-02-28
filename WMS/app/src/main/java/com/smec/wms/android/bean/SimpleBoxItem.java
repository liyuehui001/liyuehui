package com.smec.wms.android.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Adobe on 2016/1/15.
 */
public class SimpleBoxItem {
    public String BoxNo;
    public String status=Status.NEW;
    public String sacnTime; //箱号扫描时间


    public String getSacnTime() {
        return sacnTime;
    }

    public void setSacnTime(String sacnTime) {
        this.sacnTime = sacnTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBoxNo() {
        return BoxNo;
    }

    public void setBoxNo(String boxNo) {
        BoxNo = boxNo;
    }

}
