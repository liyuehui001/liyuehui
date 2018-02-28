package com.smec.wms.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/1/31.
 */
public class MatnrStockResponse {
    private List<MatnrStock> StockDetail=new ArrayList<>();

    public List<MatnrStock> getStockDetail() {
        return StockDetail;
    }

    public void setStockDetail(List<MatnrStock> stockDetail) {
        StockDetail = stockDetail;
    }

    public MatnrStock getMantrnStockByShelf(String shelf){
        if(StockDetail==null){
            return null;
        }
        for(MatnrStock matnr:StockDetail){
            if(shelf.equals(matnr.getShelfSpaceNo())){
                return matnr;
            }
        }
        return null;
    }

    public void addAll(MatnrStockResponse data){
        this.StockDetail.addAll(data.getStockDetail());
    }

    public void clear(){
        this.StockDetail.clear();
    }


}
