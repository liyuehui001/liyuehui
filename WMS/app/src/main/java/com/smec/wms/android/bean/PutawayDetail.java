package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/19.
 */

import java.io.Serializable;

/**
 *  物料明细
 */
public class PutawayDetail implements Serializable{
    private String MatnrName;       //物料名称
    private String Matnr;           //物料号
    private String PkgUnit;         //单位
    private String OrderQty;        //装箱数量
    private String PutawayQty;      //箱已收数量
    private String SuggestShelfSpace;   //建议货架


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

    public String getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(String orderQty) {
        OrderQty = orderQty;
    }

    public String getPkgUnit() {
        return PkgUnit;
    }

    public void setPkgUnit(String pkgUnit) {
        PkgUnit = pkgUnit;
    }

    public String getPutawayQty() {
        return PutawayQty;
    }

    public void setPutawayQty(String putawayQty) {
        PutawayQty = putawayQty;
    }

    public String getSuggestShelfSpace() {
        return SuggestShelfSpace;
    }

    public void setSuggestShelfSpace(String suggestShelfSpace) {
        SuggestShelfSpace = suggestShelfSpace;
    }
}
