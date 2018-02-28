package com.smec.wms.android.bean;

import java.io.Serializable;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PickDetail implements Serializable{
    private String MatnrName;
    private String Matnr;
    private String PkgUnit;
    private String ShouldQty;
    private String RealQty;
    private String ShelfSpaceNo;

    private String WaitingQty;      //

    public String getMatnr() {
        return Matnr;
    }

    public void setMatnr(String matnr) {
        Matnr = matnr;
    }

    public String getMatnrName() {
        return MatnrName;
    }

    public void setMatnrName(String matnrName) {
        MatnrName = matnrName;
    }

    public String getPkgUnit() {
        return PkgUnit;
    }

    public void setPkgUnit(String pkgUnit) {
        PkgUnit = pkgUnit;
    }

    public String getRealQty() {
        return RealQty;
    }

    public void setRealQty(String realQty) {
        RealQty = realQty;
    }

    public String getShouldQty() {
        return ShouldQty;
    }

    public void setShouldQty(String shouldQty) {
        ShouldQty = shouldQty;
    }

    public String getWaitingQty() {
        return WaitingQty;
    }

    public void setWaitingQty(String waitingQty) {
        WaitingQty = waitingQty;
    }

    public String getShelfSpaceNo() {
        return ShelfSpaceNo;
    }

    public void setShelfSpaceNo(String shelfSpaceNo) {
        ShelfSpaceNo = shelfSpaceNo;
    }
}
