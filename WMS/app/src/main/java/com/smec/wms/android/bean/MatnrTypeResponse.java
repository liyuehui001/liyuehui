package com.smec.wms.android.bean;

import java.util.ArrayList;

/**
 * Created by apple on 16/9/22.
 */

public class MatnrTypeResponse {
    public String ErrMsg ;
    public ArrayList<Matnr> MatnrList;

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public ArrayList<Matnr> getMatnrList() {
        return MatnrList;
    }

    public void setMatnrList(ArrayList<Matnr> matnrList) {
        MatnrList = matnrList;
    }
}
