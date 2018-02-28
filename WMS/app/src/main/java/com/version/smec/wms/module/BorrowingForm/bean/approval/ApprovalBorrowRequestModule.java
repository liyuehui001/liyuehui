package com.version.smec.wms.module.BorrowingForm.bean.approval;

/**
 * Created by 小黑 on 2017/9/5.
 */
public class ApprovalBorrowRequestModule {
    private String receiptNo;

    private String user;

    private String operation;

    private String outcome;

    private String comment;

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
