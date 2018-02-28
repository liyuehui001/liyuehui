package com.version.smec.wms.module.BorrowingForm;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/21.
 */
public interface BorrowingFormContract {

    void getBorrowingFormData(WeakHashMap<String,String> map);
    void approvalBorrowForm(Map<String,String> map);
    /**
     * 审批校验
     * @param map
     */
    void approveCheck(Map<String,String> map);
}
