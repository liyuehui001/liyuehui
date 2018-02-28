package com.version.smec.wms.module.Acceptance;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/9/5.
 */
public interface AcceptanceContract {

    void approvalAcceptance(Map<String,String> map);
    void search(WeakHashMap<String,String> map);
    /**
     * 审批校验
     * @param map
     */
    void approveCheck(Map<String,String> map);
}
