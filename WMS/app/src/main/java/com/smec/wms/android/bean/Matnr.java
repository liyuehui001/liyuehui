package com.smec.wms.android.bean;

import java.io.Serializable;

/**
 * Created by Adobe on 2016/3/11.
 */
public class Matnr implements Serializable{
    private String MatnrName;
    private String Matnr;
    private String PkgUnit;
    private String PkgQty;
    private String price;
    private String Remark;
    private String ChangeMsg;
    private String DownloadUrl ;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getPkgQty() {
        return PkgQty;
    }

    public void setPkgQty(String pkgQty) {
        PkgQty = pkgQty;
    }

    public String getPkgUnit() {
        return PkgUnit;
    }

    public void setPkgUnit(String pkgUnit) {
        PkgUnit = pkgUnit;
    }

    public String getChangeMsg() {
        return ChangeMsg;
    }

    public void setChangeMsg(String changeMsg) {
        ChangeMsg = changeMsg;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }
}
