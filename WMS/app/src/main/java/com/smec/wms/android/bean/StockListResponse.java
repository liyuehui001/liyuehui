package com.smec.wms.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/3/10.
 */
public class StockListResponse {
    private List<Stock> stocks = new ArrayList<Stock>();

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void clear() {
        stocks.clear();
    }

    public void addAll(StockListResponse data) {
        stocks.addAll(data.getStocks());
    }
}
