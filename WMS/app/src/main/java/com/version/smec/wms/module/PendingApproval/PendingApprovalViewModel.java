package com.version.smec.wms.module.PendingApproval;


/**
 * Created by Administrator on 2017/8/15.
 */
public class PendingApprovalViewModel {

    public int currentItem;

    public String searchKeyWord;


    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }
}
