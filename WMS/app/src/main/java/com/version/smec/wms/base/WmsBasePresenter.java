package com.version.smec.wms.base;

import android.content.Context;
import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.SmecRxBus;
import com.smec.wms.android.util.AppUtil;
import com.version.smec.wms.WmsManager.WmsRealmManager.RealmManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

import static com.smec.wms.android.activity.BaseActivity.WMS_TOKEN;
/**
 * Created by xupeizuo on 2017/8/15.
 */

public class WmsBasePresenter implements Serializable {

    protected Context mContext;
    protected Bus mRxbus;
    protected Map<String, String> headersMap;
    protected RealmManager realmManager;

    public WmsBasePresenter(Context context){
        this.mContext=context;
        mRxbus= SmecRxBus.get();
        mRxbus.register(this);
        realmManager=RealmManager.getInstance();
        initHeader();
    }

    protected void initHeader(){
        headersMap = new HashMap<>();
        headersMap.put("DF_KEY", AppUtil.getValue(mContext, WMS_TOKEN));
    }



}
