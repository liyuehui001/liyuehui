package com.smec.wms.android.bean;

import android.app.Activity;
import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Created by apple on 16/9/26.
 */

public class ListRouterBean implements Serializable{
    @DrawableRes
    private int itemIcon ;

    private String itemString ;

    private Class routerActivity ;

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemString() {
        return itemString;
    }

    public void setItemString(String itemString) {
        this.itemString = itemString;
    }

    public Class getRouterActivity() {
        return routerActivity;
    }

    public void setRouterActivity(Class routerActivity) {
        this.routerActivity = routerActivity;
    }

    public ListRouterBean(int itemIcon, String itemString, Class routerActivity) {
        this.itemIcon = itemIcon;
        this.itemString = itemString;
        this.routerActivity = routerActivity;
    }

    public ListRouterBean(){

    }
}
