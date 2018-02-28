package com.version.smec.wms.module.BorrowingForm.bean;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 小黑 on 2017/8/21.
 */
public class BorrowingFormModule extends RealmObject{
    @PrimaryKey
    private String receiptNo;
    private String receiptType;
    private String receiptId;
    private String station;
    private String borrowUser;
    private String borrowTime;
    private String sourceType;
    private String project;
    private String backDate;
    private String contactPhone;
    private String borrowComp;
    private String contractNo;
    private String eleNo;
    private String deliveryType;
    private String address;
    private String borrowReason;
    private String statusCode;
    private String statusMeaning;
    private String isUrgent;

    public String getStatusMeaning() {
        return statusMeaning;
    }

    public void setStatusMeaning(String statusMeaning) {
        this.statusMeaning = statusMeaning;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    @Ignore
    private ArrayList<BorrowingFormGoodModule> matnrs;
    @Ignore
    private ArrayList<BorrowingFormAppendix> attachs;


    public ArrayList<BorrowingFormAppendix> getAttachs() {
        return attachs;
    }

    public void setAttachs(ArrayList<BorrowingFormAppendix> attachs) {
        this.attachs = attachs;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getBorrowUser() {
        return borrowUser;
    }

    public void setBorrowUser(String borrowUser) {
        this.borrowUser = borrowUser;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getBorrowComp() {
        return borrowComp;
    }

    public void setBorrowComp(String borrowComp) {
        this.borrowComp = borrowComp;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getEleNo() {
        return eleNo;
    }

    public void setEleNo(String eleNo) {
        this.eleNo = eleNo;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBorrowReason() {
        return borrowReason;
    }

    public void setBorrowReason(String borrowReason) {
        this.borrowReason = borrowReason;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<BorrowingFormGoodModule> getMatnrs() {
        return matnrs;
    }

    public void setMatnrs(ArrayList<BorrowingFormGoodModule> matnrs) {
        this.matnrs = matnrs;
    }
}
