package com.version.smec.wms.module.SalesSlipment;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/18.
 */
public interface SalesSlipmentContract {

    void getSaleSlip(WeakHashMap<String,String> map);

    void refuseAgree(Map<String,String> map);

    /**
     * 审批校验
     * @param map
     */
    void approveCheck(Map<String,String> map);
}
