package com.version.smec.wms.module.PendingApproval.bean;

/**
 * Created by Administrator on 2017/8/21.
 */
public class DiaSearchModel {
    private String contractNo;
    private String eleNo;
    private String matnr;
    private String matnrName;
    private String contactPerson;
    private int currentItem;


    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
