package com.version.smec.wms.module.logs;

import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/28.
 */
public interface LogsContract {
    public void getLogs(WeakHashMap<String,String> map);
}
