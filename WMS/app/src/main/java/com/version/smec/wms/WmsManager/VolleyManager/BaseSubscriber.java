package com.version.smec.wms.WmsManager.VolleyManager;


import rx.Subscriber;

import static com.version.smec.wms.WmsManager.VolleyManager.ApiErrorHelper.handleError;

/**
 * Created by xupeizuo on 2017/4/26.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    public BaseSubscriber() {
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        handleError(e);
    }

}
