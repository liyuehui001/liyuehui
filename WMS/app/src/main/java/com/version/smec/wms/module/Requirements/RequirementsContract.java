package com.version.smec.wms.module.Requirements;

import java.util.WeakHashMap;
import java.util.Map;

/**
 * Created by xupeizuo on 2017/8/15.
 */

public interface RequirementsContract {

    void requirementDetail(WeakHashMap<String,String> map);

    /**
     * 审批校验
     * @param map
     */
    void approveCheck(Map<String,String> map);


    void approveRequrement(Map<String,String> map);
}
