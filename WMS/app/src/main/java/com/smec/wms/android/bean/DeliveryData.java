package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/7/15.
 */
public class DeliveryData extends ResponseData{
    private String OutboundOrderNo; //出库单
    private String DeliveryAddress; //发运地址
    private String QuotedPrice;     //报价

    public String getQuotedPrice() {
        return QuotedPrice;
    }

    public void setQuotedPrice(String quotedPrice) {
        QuotedPrice = quotedPrice;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getOutboundOrderNo() {
        return OutboundOrderNo;
    }

    public void setOutboundOrderNo(String outboundOrderNo) {
        OutboundOrderNo = outboundOrderNo;
    }
}
