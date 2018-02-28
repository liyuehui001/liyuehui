package com.smec.wms.android.bean;

import java.io.Serializable;

/**
 * Created by apple on 16/9/21.
 */

public class ClassType implements Serializable {
    private String ClassType ;
    private String ClassMeaning ;

    public String getClassType() {
        return ClassType;
    }

    public void setClassType(String classType) {
        ClassType = classType;
    }

    public String getClassMeaning() {
        return ClassMeaning;
    }

    public void setClassMeaning(String classMeaning) {
        ClassMeaning = classMeaning;
    }
}

