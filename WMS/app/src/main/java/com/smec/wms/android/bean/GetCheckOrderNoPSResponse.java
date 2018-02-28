package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/21.
 */
public class GetCheckOrderNoPSResponse extends ResponseData {
    private List<CheckDetailItem> CheckDetail;

    public List<CheckDetailItem> getCheckDetail() {
        return CheckDetail;
    }

    public void setCheckDetail(List<CheckDetailItem> checkDetail) {
        CheckDetail = checkDetail;
    }


}
