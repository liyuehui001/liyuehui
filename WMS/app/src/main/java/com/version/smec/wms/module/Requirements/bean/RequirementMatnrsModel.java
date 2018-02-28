package com.version.smec.wms.module.Requirements.bean;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.smec.wms.BR;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by 小黑 on 2017/8/16.
 */
public class RequirementMatnrsModel extends BaseObservable{


    private String matnrId;
    private String matnr;
    private String matnrName;
    private String qty;//数量
    private String unit;
    private String lineStatus;
    private String hdQty;
    private String changedMatnr;
    private String description;
    private String remark;
    private String receiptNo;
    private String reason;
    private String pickingQty;//待拣货
    private String pickedQty;//已件货
    private String finishQty;


    public String getFinishQty() {
        return finishQty;
    }

    public void setFinishQty(String finishQty) {
        this.finishQty = finishQty;
    }

    public String getPickingQty() {
        return pickingQty;
    }

    public void setPickingQty(String pickingQty) {
        this.pickingQty = pickingQty;
    }

    public String getPickedQty() {
        return pickedQty;
    }

    public void setPickedQty(String pickedQty) {
        this.pickedQty = pickedQty;
    }

    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
        notifyPropertyChanged(BR.remark);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChangedMatnr() {
        return changedMatnr;
    }

    public void setChangedMatnr(String changedMatnr) {
        this.changedMatnr = changedMatnr;
    }


    @Bindable
    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
        notifyPropertyChanged(BR.matnr);
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
    @Bindable
    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
        notifyPropertyChanged(BR.matnrs);
    }

    public String getHdQty() {
        return hdQty;
    }

    public void setHdQty(String hdQty) {
        this.hdQty = hdQty;
    }
}
