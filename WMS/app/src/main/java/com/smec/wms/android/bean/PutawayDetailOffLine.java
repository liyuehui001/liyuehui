package com.smec.wms.android.bean;

/**
 * Created by Adobe on 2016/1/28.
 */
public class PutawayDetailOffLine {
    private String InboundOrderNo;  //入库单
    private String BoxNo;           //箱号
    private String MatnrName;       //物料名称
    private String Matnr;           //物料号
    private String PkgUnit;         //单位
    private String PkgQty;          //装箱数量
    private String ShouldQty;       //箱已收数量
    private String SuggestShelfSpace;   //建议货架
    private String RealQty;
    private String amount="0";  //提交数量

    private String status=Status.NEW;          //物料状态 Status.NEW 未上架 Status.COMPLETE 已上架

    public String getBoxNo() {
        return BoxNo;
    }

    public void setBoxNo(String boxNo) {
        BoxNo = boxNo;
    }

    public String getInboundOrderNo() {
        return InboundOrderNo;
    }

    public void setInboundOrderNo(String inboundOrderNo) {
        InboundOrderNo = inboundOrderNo;
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

    public String getPkgQty() {
        return PkgQty;
    }

    public void setPkgQty(String pkgQty) {
        PkgQty = pkgQty;
    }

    public String getPkgUnit() {
        return PkgUnit;
    }

    public void setPkgUnit(String pkgUnit) {
        PkgUnit = pkgUnit;
    }

    public String getRealQty() {
        return RealQty;
    }

    public void setRealQty(String realQty) {
        RealQty = realQty;
    }

    public String getShouldQty() {
        return ShouldQty;
    }

    public void setShouldQty(String shouldQty) {
        ShouldQty = shouldQty;
    }

    public String getSuggestShelfSpace() {
        return SuggestShelfSpace;
    }

    public void setSuggestShelfSpace(String suggestShelfSpace) {
        SuggestShelfSpace = suggestShelfSpace;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
