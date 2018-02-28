package com.version.smec.wms.module.Requirements.bean.approval;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xupeizuo on 2017/9/12.
 */

public class RequirementMatnrsModelForRealm extends RealmObject {

    @PrimaryKey
    private String matnrId;
    private String matnr;
    private String matnrName;
    private String qty;//数量
    private String unit;
    private String lineStatus;
    private String hdQty;
    private String changedMatnr;
    private String description;
    private String reamrk;
    private String receiptNo;

    public String getMatnrId() {
        return matnrId;
    }

    public void setMatnrId(String matnrId) {
        this.matnrId = matnrId;
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

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    public String getHdQty() {
        return hdQty;
    }

    public void setHdQty(String hdQty) {
        this.hdQty = hdQty;
    }

    public String getChangedMatnr() {
        return changedMatnr;
    }

    public void setChangedMatnr(String changedMatnr) {
        this.changedMatnr = changedMatnr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReamrk() {
        return reamrk;
    }

    public void setReamrk(String reamrk) {
        this.reamrk = reamrk;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}
