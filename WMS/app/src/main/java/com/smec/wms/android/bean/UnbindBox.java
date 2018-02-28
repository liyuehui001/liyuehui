package com.smec.wms.android.bean;

import java.io.Serializable;

/**
 * Created by Adobe on 2016/2/18.
 */
public class UnbindBox implements Serializable{
    private String BoxNo;

    public String getBoxNo() {
        return BoxNo;
    }

    public void setBoxNo(String boxNo) {
        BoxNo = boxNo;
    }
}
