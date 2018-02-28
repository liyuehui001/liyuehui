package com.smec.wms.android.bean;

import java.io.Serializable;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PackBoxDetail implements Serializable{
    private String MatnrName;
    private String Matnr;
    private String PkgUnit;
    private String ShouldQty;
    private String RealQty;
    private String AllBoxQty;
    private String PlateFlag;
    private String BoxQty;

    public String getAllBoxQty() {
        return AllBoxQty;
    }

    public void setAllBoxQty(String allBoxQty) {
        AllBoxQty = allBoxQty;
    }

    public String getBoxQty() {
        return BoxQty;
    }

    public void setBoxQty(String boxQty) {
        BoxQty = boxQty;
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

    public String getPlateFlag() {
        return PlateFlag;
    }

    public void setPlateFlag(String plateFlag) {
        PlateFlag = plateFlag;
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
}
