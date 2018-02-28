package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/19.
 */
public class CheckRDCFGSPutawayBoxNoRespnose extends ResponseData{
    List<BoxDetail> boxDetail;

    public List<BoxDetail> getBoxDetail() {
        return boxDetail;
    }

    public void setBoxDetail(List<BoxDetail> boxDetail) {
        this.boxDetail = boxDetail;
    }
}
