package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/19.
 */
public class BoxDetail extends SimpleBoxItem{
    private String inboundOrderNo;      //入库单
    private String matnrName;   //物料名称
    private String matnr;       //物料编号
    private String pkgUnit;     //单位
    private String pkgQty;      //
    private String shouldQty;   //箱已收量
    private String realQty;     //应收数量
    private String suggestShelfSpace;   //建议货架


    public String getInboundOrderNo() {
        return inboundOrderNo;
    }

    public void setInboundOrderNo(String inboundOrderNo) {
        this.inboundOrderNo = inboundOrderNo;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMatnrName() {
        return matnrName;
    }

    public void setMatnrName(String matnrName) {
        this.matnrName = matnrName;
    }

    public String getPkgQty() {
        return pkgQty;
    }

    public void setPkgQty(String pkgQty) {
        this.pkgQty = pkgQty;
    }

    public String getPkgUnit() {
        return pkgUnit;
    }

    public void setPkgUnit(String pkgUnit) {
        this.pkgUnit = pkgUnit;
    }

    public String getRealQty() {
        return realQty;
    }

    public void setRealQty(String realQty) {
        this.realQty = realQty;
    }

    public String getShouldQty() {
        return shouldQty;
    }

    public void setShouldQty(String shouldQty) {
        this.shouldQty = shouldQty;
    }

    public String getSuggestShelfSpace() {
        return suggestShelfSpace;
    }

    public void setSuggestShelfSpace(String suggestShelfSpace) {
        this.suggestShelfSpace = suggestShelfSpace;
    }
}
