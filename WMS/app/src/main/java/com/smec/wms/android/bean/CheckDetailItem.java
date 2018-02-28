package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/21.
 */
public class CheckDetailItem {
    private String CheckQty;
    private String Matnr;
    private String PkgUnit;
    private String MatnrName;

    public String getCheckQty() {
        return CheckQty;
    }

    public void setCheckQty(String checkQty) {
        CheckQty = checkQty;
    }

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
}
