package com.smec.wms.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/21.
 */

public class ElevatorTypeResponse {
    public String ErrMsg ;
    public ArrayList<ElevatorType> EleTypeList ;

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public ArrayList<ElevatorType> getEleTypeList() {
        return EleTypeList;
    }

    public void setEleTypeList(ArrayList<ElevatorType> eleTypeList) {
        EleTypeList = eleTypeList;
    }
}
