package com.version.smec.wms.module.Requirements.bean;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 小黑 on 2017/8/15.
 */
public class RequirementDetailModel extends RealmObject{

    @PrimaryKey
    private String receiptNo;
    private String receiptType;
    private String receiptId;
    private String branch;
    private String creator;
    private String createTime;
    private String reqDate;
    private String sourceType;
    private String isUrgent;
    private String isToBranch;
    private String contractNo;
    private String eleNo;
    private String project;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private String statusCode;
    private String statusMeaning;

    @Ignore
    private ArrayList<RequirementMatnrsModel> matnrs;


    public String getStatusMeaning() {
        return statusMeaning;
    }

    public void setStatusMeaning(String statusMeaning) {
        this.statusMeaning = statusMeaning;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getIsToBranch() {
        return isToBranch;
    }

    public void setIsToBranch(String isToBranch) {
        this.isToBranch = isToBranch;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<RequirementMatnrsModel> getRequirementMatnrsModelArrayList() {
        return matnrs;
    }

    public void setRequirementMatnrsModelArrayList(ArrayList<RequirementMatnrsModel> matnrs) {
        this.matnrs = matnrs;
    }


}
