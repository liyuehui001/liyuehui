package com.version.smec.wms.module.Acceptance.bean;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2017/8/20.
 */
public class AcceptanceModule extends RealmObject{
    @PrimaryKey
    private String receiptNo;
    private String receiveReason;
    private String receiptType;
    private String receiptId;
    private String station;
    private String receiveUser;
    private String receiveTime;
    private String receiveType;
    private String project;
    private String reqDate;
    private String contactPhone;
    private String receiveComp;
    private String reasonType;
    private String reasonDetail;
    private String contType;
    private String contNo;
    private String contractNo;
    private String eleNo;
    private String deliveryType;
    private String address;
    private String internalNo;
    private String internalNanme;
    private String costCenterNo;
    private String costCenterName;
    private String statusCode;
    private String statusMeaning;


    public String getStatusMeaning() {
        return statusMeaning;
    }

    public void setStatusMeaning(String statusMeaning) {
        this.statusMeaning = statusMeaning;
    }

    @Ignore
    private ArrayList<AcceptanceMatnrsModel> matnrs;//物料信息
    @Ignore
    private ArrayList<AcceptanceEnclosureModel>  attachs;  //附件信息


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

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getReceiveComp() {
        return receiveComp;
    }

    public void setReceiveComp(String receiveComp) {
        this.receiveComp = receiveComp;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getContNo() {
        return contNo;
    }

    public void setContNo(String contNo) {
        this.contNo = contNo;
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

    public String getInternalNo() {
        return internalNo;
    }

    public void setInternalNo(String internalNo) {
        this.internalNo = internalNo;
    }

    public String getInternalNanme() {
        return internalNanme;
    }

    public void setInternalNanme(String internalNanme) {
        this.internalNanme = internalNanme;
    }

    public String getCostCenterNo() {
        return costCenterNo;
    }

    public void setCostCenterNo(String costCenterNo) {
        this.costCenterNo = costCenterNo;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<AcceptanceMatnrsModel> getMatnrs() {
        return matnrs;
    }

    public void setMatnrs(ArrayList<AcceptanceMatnrsModel> matnrs) {
        this.matnrs = matnrs;
    }


    public ArrayList<AcceptanceEnclosureModel> getAttachs() {
        return attachs;
    }

    public void setAttachs(ArrayList<AcceptanceEnclosureModel> attachs) {
        this.attachs = attachs;
    }

    public String getReceiveReason() {
        return receiveReason;
    }

    public void setReceiveReason(String receiveReason) {
        this.receiveReason = receiveReason;
    }
}
