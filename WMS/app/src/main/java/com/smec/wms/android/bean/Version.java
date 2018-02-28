package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/3/6.
 */
public class Version {
    public final static String UPDATE_POLICY_FORCE="FORCE";
    public final static String UPDATE_POLICY_OPTION="OPTION";
    private String haveUpdate;      //是否有更新Y/N
    private String curVersion;      //当前版本
    private String updatePolicy;    //升级策略 FORCE 强制  OPTION可选
    private String apkURL;          //安装包下载地址

    public String getApkURL() {
        return apkURL;
    }

    public void setApkURL(String apkURL) {
        this.apkURL = apkURL;
    }

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    public String getHaveUpdate() {
        return haveUpdate;
    }

    public void setHaveUpdate(String haveUpdate) {
        this.haveUpdate = haveUpdate;
    }

    public String getUpdatePolicy() {
        return updatePolicy;
    }

    public void setUpdatePolicy(String updatePolicy) {
        this.updatePolicy = updatePolicy;
    }

    public boolean isHaveUpdate(){
        return "Y".equals(this.haveUpdate);
    }
}
