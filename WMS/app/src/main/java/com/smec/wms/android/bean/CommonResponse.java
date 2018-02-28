package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/19.
 */

/**
 * 通用回复，用户部分接口返回一个字段情况
 */
public class CommonResponse extends ResponseData {
    private String IsNoBox;     //RDC上架_验证单号 CheckRDCFGSPutawayOrderNoPS
    private String IsCompletedFlag; //是否已完成标志。多个接口用到
    private String InboundOrderNo;  //RDC_入库 根据箱号返回入库单
    private String ShippingAddress; //装箱中的目的地
    private String price;   //物料价格

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsCompletedFlag() {
        return IsCompletedFlag;
    }

    public void setIsCompletedFlag(String isCompletedFlag) {
        IsCompletedFlag = isCompletedFlag;
    }

    public String getIsNoBox() {
        return IsNoBox;
    }

    public void setIsNoBox(String isNoBox) {
        IsNoBox = isNoBox;
    }

    public String getInboundOrderNo() {
        return InboundOrderNo;
    }

    public void setInboundOrderNo(String inboundOrderNo) {
        InboundOrderNo = inboundOrderNo;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public boolean isCompleted() {
        return "true".equals(this.getIsCompletedFlag());
    }
}
