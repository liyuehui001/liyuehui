package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/1/15.
 */
public class StockInDetailsResponse extends ResponseData{
    private List<SimpleBoxItem> BoxNoList;

    public List<SimpleBoxItem> getBoxNoList() {
        return BoxNoList;
    }

    public void setBoxNoList(List<SimpleBoxItem> boxNoList) {
        BoxNoList = boxNoList;
    }

}
