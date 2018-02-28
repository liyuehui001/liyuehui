package com.version.smec.wms.module.PendingApproval.bean;

/**
 * Created by xupeizuo on 2017/8/15.
 */

public class QueryModule {

    private String queryModule;
    private String queryMode;
    private String condition;
    private String contractNo;
    private String eleNo;
    private String matnr;
    private String matnrName;
    private String contactPerson;

    public String getQueryModule() {
        return queryModule;
    }

    public void setQueryModule(String queryModule) {
        this.queryModule = queryModule;
    }

    public String getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
