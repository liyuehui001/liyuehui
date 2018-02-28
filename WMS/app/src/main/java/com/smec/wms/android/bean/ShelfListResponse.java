package com.smec.wms.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/4/20.
 */
public class ShelfListResponse {
    private List<Shelf> StockDetail=new ArrayList<Shelf>();

    public void clear(){
        StockDetail.clear();;
    }

    public List<Shelf> getStockDetail() {
        return StockDetail;
    }

    public void setStockDetail(List<Shelf> stockDetail) {
        StockDetail = stockDetail;
    }

    public void addAll(ShelfListResponse data) {
        StockDetail.addAll(data.getStockDetail());
    }
}
