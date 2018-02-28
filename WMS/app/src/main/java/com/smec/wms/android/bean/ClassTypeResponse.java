package com.smec.wms.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/22.
 */

public class ClassTypeResponse {
    public String ErrMsg ;
    private ArrayList<ClassType> ClassList ;

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public ArrayList<ClassType> getClassList() {
        return ClassList;
    }

    public void setClassList(ArrayList<ClassType> classList) {
        ClassList = classList;
    }
}
