package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/28.
 */
public class UserProfile {
    private String DsiplayName;
    private String warehouseNo;
    private String isCDCFlag;
    private String uid;
    private String token;
    private String userRole;
    private String userName;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getNetwork() {
        return network;
    }

    public String getNetworkDesc() {
        if("0".equals(network)){
            return "外网环境";
        }else if("1".equals(network)){
            return "内网环境";
        }
        return "测试环境";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    private String network;

    public String getDsiplayName() {
        return DsiplayName;
    }

    public void setDsiplayName(String dsiplayName) {
        DsiplayName = dsiplayName;
    }

    public String getIsCDCFlag() {
        return isCDCFlag;
    }

    public void setIsCDCFlag(String isCDCFlag) {
        this.isCDCFlag = isCDCFlag;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
