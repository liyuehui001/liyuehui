package com.smec.wms.android.bean;

import com.smec.wms.android.util.AppUtil;

import java.util.List;

/**
 * Created by Adobe on 2016/1/28.
 */
public class PutawayDetailOfflineResponse extends ResponseData {
    private String ShelfSpaceNo;
    private String BoxFlag;
    private List<PutawayDetailOffLine> InboundDetail;

    public String getBoxFlag() {
        return BoxFlag;
    }

    public void setBoxFlag(String boxFlag) {
        BoxFlag = boxFlag;
    }

    public List<PutawayDetailOffLine> getInboundDetail() {
        return InboundDetail;
    }

    public void setInboundDetail(List<PutawayDetailOffLine> inboundDetail) {
        InboundDetail = inboundDetail;
    }

    public String getShelfSpaceNo() {
        return ShelfSpaceNo;
    }

    public void setShelfSpaceNo(String shelfSpaceNo) {
        ShelfSpaceNo = shelfSpaceNo;
    }

    public boolean isNoBox() {
        return "false".equals(this.BoxFlag);
    }

    /**
     * 箱号验证
     *
     * @param box
     * @return
     */
    public boolean validBox(String box) {
        for (PutawayDetailOffLine item : InboundDetail) {
            if (box.equals(item.getBoxNo())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取物料详细信息
     *
     * @param box
     * @param matnr
     * @return
     */
    public PutawayDetailOffLine getDetail(String box, String matnr) {
        for (PutawayDetailOffLine item : InboundDetail) {
            if (item.getMatnr().equals(matnr)) {
                if (AppUtil.strNull(item.getBoxNo())) {
                    //无箱子，物料号一样结束
                    return item;
                } else if (item.getBoxNo().equals(box)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 物料上架
     *
     * @param box
     * @param matnr
     */
    public void matnrPutway(String box, String matnr) {
        for (PutawayDetailOffLine item : InboundDetail) {
            if (item.getMatnr().equals(matnr)) {
                if (AppUtil.strNull(item.getBoxNo())) {
                    //无箱子，物料号一样结束
                    item.setStatus(Status.COMPLETE);
                    return;
                } else if (item.getBoxNo().equals(box)) {
                    item.setStatus(Status.COMPLETE);
                    return;
                }
            }
        }
    }

    /**
     * 是否入库单已经上架完毕
     *
     * @return
     */
    public boolean isCompleted() {
        for (PutawayDetailOffLine item : InboundDetail) {
            if (!Status.COMPLETE.equals(item.getStatus())) {
                return false;
            }
        }
        return true;
    }

    public void setPutawayAmount(String box, String matnr, String amount) {
        for (PutawayDetailOffLine item : InboundDetail) {
            if (item.getMatnr().equals(matnr)) {
                if (AppUtil.strNull(item.getBoxNo()) || item.getBoxNo().equals(box)) {
                    //无箱子，物料号一样结束
                    item.setAmount(AppUtil.stringPlus(item.getAmount(), amount));
                    item.setRealQty(AppUtil.stringPlus(item.getRealQty(), amount));
                    if (item.getShouldQty().equals(item.getRealQty())) {
                        item.setStatus(Status.COMPLETE);
                    }
                    return;
                }
            }
        }
    }
}
