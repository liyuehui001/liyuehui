package com.version.smec.wms.module.LogisticsDetail.bean;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/25.
 */
public class LogisitiicsDetailModule {

    private String outboundOrderNo;
    private String boxNo;
    private String logisticsComp;
    private String logisticsNo;
    private String entrustUnit;
    private String shippingWay;


    private ArrayList<LogisitiicsDetailMatnrModule> matnr;

    public ArrayList<LogisitiicsDetailMatnrModule> getMatnr() {
        return matnr;
    }

    public void setMatnr(ArrayList<LogisitiicsDetailMatnrModule> matnr) {
        this.matnr = matnr;
    }

    public String getLogisticsComp() {
        return logisticsComp;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public void setLogisticsComp(String logisticsComp) {
        this.logisticsComp = logisticsComp;
    }

    public String getEntrustUnit() {
        return entrustUnit;
    }

    public void setEntrustUnit(String entrustUnit) {
        this.entrustUnit = entrustUnit;
    }

    public String getOutboundOrderNo() {
        return outboundOrderNo;
    }

    public void setOutboundOrderNo(String outboundOrderNo) {
        this.outboundOrderNo = outboundOrderNo;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getShippingWay() {
        return shippingWay;
    }

    public void setShippingWay(String shippingWay) {
        this.shippingWay = shippingWay;
    }


}
