package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/30.
 */
public class BindBoxDetailResponse extends ResponseData{

    List<BindBoxDetail>BoxStandardDetail;

    public List<BindBoxDetail> getBoxStandardDetail() {
        return BoxStandardDetail;
    }

    public void setBoxStandardDetail(List<BindBoxDetail> boxStandardDetail) {
        BoxStandardDetail = boxStandardDetail;
    }
}
