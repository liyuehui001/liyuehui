package com.version.smec.wms.module.SalesSlipment.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by 小黑 on 2017/8/17.
 */
public class SalesSlipGoodsModule extends RealmObject{

    @PrimaryKey
    private String matnrId;
    private String matnr;
    private String receiptNo;
    private String matnrName;
    private String qty;
    private String unit;
    private String branchQty;
    private String hdQty;
    private String standPrice;
    private String description;


    public String getMatnrId() {
        return matnrId;
    }

    public void setMatnrId(String matnrId) {
        this.matnrId = matnrId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBranchQty() {
        return branchQty;
    }

    public void setBranchQty(String branchQty) {
        this.branchQty = branchQty;
    }

    public String getHdQty() {
        return hdQty;
    }

    public void setHdQty(String hdQty) {
        this.hdQty = hdQty;
    }

    public String getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(String standPrice) {
        this.standPrice = standPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
