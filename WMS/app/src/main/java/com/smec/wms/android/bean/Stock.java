package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/3/10.
 */
public class Stock {
    private String MatnrName;
    private String Matnr;
    private String WarehouseNo;
    private String WarehouseName;
    private String StockQty;
    private String LockQty;

    private String price ;

    private String DownloadUrl ;

    public String getLockQty() {
        return LockQty;
    }

    public void setLockQty(String lockQty) {
        LockQty = lockQty;
    }

    public String getMatnr() {
        return Matnr;
    }

    public void setMatnr(String matnr) {
        Matnr = matnr;
    }

    public String getMatnrName() {
        return MatnrName;
    }

    public void setMatnrName(String matnrName) {
        MatnrName = matnrName;
    }

    public String getStockQty() {
        return StockQty;
    }

    public void setStockQty(String stockQty) {
        StockQty = stockQty;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    public String getWarehouseNo() {
        return WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.DownloadUrl = downloadUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
