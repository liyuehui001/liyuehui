package com.smec.wms.android.bean;

import android.util.Log;

import com.smec.wms.android.util.AppUtil;

/**
 * Created by Adobe on 2016/7/15.
 */
public class CpzxItem extends ResponseData {
    private String className;   //品目
    private String foreFather;
    private String GCode;
    private String LCode;
    private String packingNo;   //装箱号
    private String quantity;    //数量
    private String remark;      //备注
    private String title;       //名称
    private String gwgno;       //图号
    private String idnrk;       //物料号


    private boolean visible = true;    //是否可见（过滤）

    public String getGwgno() {
        return gwgno;
    }

    public void setGwgno(String gwgno) {
        this.gwgno = gwgno;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getForeFather() {
        return foreFather;
    }

    public void setForeFather(String foreFather) {
        this.foreFather = foreFather;
    }

    public String getGCode() {
        return GCode;
    }

    public void setGCode(String GCode) {
        this.GCode = GCode;
    }

    public String getLCode() {
        return LCode;
    }

    public void setLCode(String LCode) {
        this.LCode = LCode;
    }

    public String getPackingNo() {
        return packingNo;
    }

    public void setPackingNo(String packingNo) {
        this.packingNo = packingNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getIdnrk() {
        return idnrk;
    }

    public void setIdnrk(String idnrk) {
        this.idnrk = idnrk;
    }


    public boolean filter(CpzxItem filter) {
        return check(className, filter.getClassName()) &&
                check(idnrk, filter.getIdnrk()) &&
                check(title, filter.getTitle()) &&
                check(gwgno, filter.getGwgno()) &&
                check(quantity, filter.getQuantity()) && check(packingNo, filter.getPackingNo());
    }

    private boolean check(String str1, String str2) {
        if (AppUtil.strNull(str2)) {
            return true;
        }
        //      String log=String.format("str1:%s str2:%s",str1,str2);
        str1 = str1 == null ? "" : str1;
        str1 = str1.toUpperCase();
        str2 = str2.toUpperCase();
        boolean result = str1.contains(str2);
        //       log+=result;
//        Log.e("********",log);
        return result;
    }
}
