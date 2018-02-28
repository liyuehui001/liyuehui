package com.version.smec.wms.module.BorrowingForm.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 附件信息
 * Created by 小黑 on 2017/8/21.
 */
public class BorrowingFormAppendix extends RealmObject{

    @PrimaryKey
    private String attachId;
    private String receiptNo;
    private String attachName;
    private String creator;
    private String createTime;
    private String url;
    private String localPath;
    private String attachType;
    private String ddocName;
    private int code;

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    public String getDdocName() {
        return ddocName;
    }

    public void setDdocName(String ddocName) {
        this.ddocName = ddocName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLocalPath() {
        return localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
