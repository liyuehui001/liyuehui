package com.version.smec.wms.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xupeizuo on 2017/7/25.
 */

public class SmecUser extends RealmObject {

    @PrimaryKey
    private String userId;
    private String userName;
    private String passWord;
    private String userRole;
    private String token;
    private boolean autoLogin ;
    private boolean rememberPassword ;
    private boolean rememberAccount;
    private boolean lastLogin ;

    public boolean isRememberAccount() {
        return rememberAccount;
    }

    public void setRememberAccount(boolean rememberAccount) {
        this.rememberAccount = rememberAccount;
    }

    public boolean isLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(boolean lastLogin) {
        this.lastLogin = lastLogin;
    }

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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public boolean isRememberPassword() {
        return rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        this.rememberPassword = rememberPassword;
    }
}
