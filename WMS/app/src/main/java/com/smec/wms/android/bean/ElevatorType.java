package com.smec.wms.android.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 16/9/21.
 */

public class ElevatorType implements Serializable{
    private String EleType ;
    private String EleTypeMeaning ;

    public String getEleType() {
        return EleType;
    }

    public void setEleType(String eleType) {
        EleType = eleType;
    }

    public String getEleTypeMeaning() {
        return EleTypeMeaning;
    }

    public void setEleTypeMeaning(String eleTypeMeaning) {
        EleTypeMeaning = eleTypeMeaning;
    }
}
