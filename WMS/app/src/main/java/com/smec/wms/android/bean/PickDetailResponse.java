package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PickDetailResponse extends ResponseData{
    private List<PickDetail> PickDetail;

    public List<com.smec.wms.android.bean.PickDetail> getPickDetail() {
        return PickDetail;
    }

    public void setPickDetail(List<com.smec.wms.android.bean.PickDetail> pickDetail) {
        PickDetail = pickDetail;
    }
}
