package com.smec.wms.android.bean;

import java.util.List;

/**
 * Created by Adobe on 2016/2/18.
 */
public class UnbindBoxResponse extends ResponseData{
    private List<UnbindBox> BoxNoList;

    public List<UnbindBox> getBoxNoList() {
        return BoxNoList;
    }

    public void setBoxNoList(List<UnbindBox> boxNoList) {
        BoxNoList = boxNoList;
    }
}
