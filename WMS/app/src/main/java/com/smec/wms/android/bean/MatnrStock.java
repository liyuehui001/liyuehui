package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/31.
 */
public class MatnrStock {
    private String WarehouseNo;
    private String ShelfSpaceNo;
    private String Matnr;
    private String MatnrName;
    private String PkgUnit;
    private String StockQty;
    private String LockQty;

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

    public String getPkgUnit() {
        return PkgUnit;
    }

    public void setPkgUnit(String pkgUnit) {
        PkgUnit = pkgUnit;
    }

    public String getShelfSpaceNo() {
        return ShelfSpaceNo;
    }

    public void setShelfSpaceNo(String shelfSpaceNo) {
        ShelfSpaceNo = shelfSpaceNo;
    }

    public String getStockQty() {
        return StockQty;
    }

    public void setStockQty(String stockQty) {
        StockQty = stockQty;
    }

    public String getWarehouseNo() {
        return WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }
}
