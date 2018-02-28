package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PackBoxDetailResponse extends ResponseData{
    private List<PackBoxDetail> BoxDetail;

    public List<PackBoxDetail> getBoxDetail() {
        return BoxDetail;
    }

    public void setBoxDetail(List<PackBoxDetail> boxDetail) {
        BoxDetail = boxDetail;
    }
}
