package com.smec.wms.android.bean;

import com.smec.wms.android.util.AppUtil;

import java.util.List;

/**
 * Created by Adobe on 2016/1/16.
 */
public class StockInOrderResponse extends ResponseData {
    private List<SimpleBoxItem> BoxNoList;
    private String SupplierInfo;        //供应商
    private String AllBoxCount;         //总收箱量
    private String ReceivedCount;       //已收箱量

    public String getAllBoxCount() {
        return AllBoxCount;
    }

    public void setAllBoxCount(String allBoxCount) {
        AllBoxCount = allBoxCount;
    }


    public String getReceivedCount() {
        return ReceivedCount;
    }

    public int getReceivedCountInt(){
        int value=0;
        try{
            value=Integer.parseInt(this.ReceivedCount);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return value;
    }

    public void setReceivedCount(String receivedCount) {
        ReceivedCount = receivedCount;
    }

    public List<SimpleBoxItem> getBoxNoList() {
        return BoxNoList;
    }

    public void setBoxNoList(List<SimpleBoxItem> boxNoList) {
        BoxNoList = boxNoList;
    }


    public String getSupplierInfo() {
        return SupplierInfo;
    }

    public void setSupplierInfo(String supplierInfo) {
        SupplierInfo = supplierInfo;
    }

}
