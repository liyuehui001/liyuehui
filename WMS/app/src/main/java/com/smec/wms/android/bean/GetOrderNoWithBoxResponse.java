package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/18.
 */
public class GetOrderNoWithBoxResponse extends ResponseData{
    private String InboundOrderNo;

    public String getInboundOrderNo() {
        return InboundOrderNo;
    }

    public void setInboundOrderNo(String inboundOrderNo) {
        InboundOrderNo = inboundOrderNo;
    }
}
