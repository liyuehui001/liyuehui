package com.version.smec.wms.module.BorrowingForm.bean;

import com.version.smec.wms.bean.HttpResponse;

/**
 * Created by 小黑 on 2017/8/21.
 */
public class BorrowingFormDto extends HttpResponse{

    private BorrowingFormModule data;

    public BorrowingFormModule getData() {
        return data;
    }

    public void setData(BorrowingFormModule data) {
        this.data = data;
    }
}
