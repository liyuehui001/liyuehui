package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/19.
 */
public class CheckRDCFGSPutawayMatnrResponse extends ResponseData{
    private List<PutawayDetail> PutawayDetail;
    public List<PutawayDetail> getPutawayDetail() {
        return PutawayDetail;
    }

    public void setPutawayDetail(List<PutawayDetail> putawayDetail) {
        this.PutawayDetail = putawayDetail;
    }
}
