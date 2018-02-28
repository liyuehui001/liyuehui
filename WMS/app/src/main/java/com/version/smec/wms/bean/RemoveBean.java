package com.version.smec.wms.bean;

import java.io.Serializable;

/**
 * Created by xupeizuo on 2017/9/15.
 */

public class RemoveBean implements Serializable {

    private String receiptNo;

    public RemoveBean(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}
