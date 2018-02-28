package com.smec.wms.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/3/11.
 */
public class MatnrListResponse {
    private List<Matnr> matnrs=new ArrayList<>();

    public List<Matnr> getMatnrs() {
        return matnrs;
    }

    public void setMatnrs(List<Matnr> matnrs) {
        this.matnrs = matnrs;
    }

    public void clear(){
        matnrs.clear();
    }

    public void addAll(MatnrListResponse data){
        matnrs.addAll(data.getMatnrs());
    }
}
