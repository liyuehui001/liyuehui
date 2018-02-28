package com.version.smec.wms.WmsManager.VolleyManager;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.version.smec.wms.api.WmsConstant;
import com.version.smec.wms.utils.ToastUtils;

/**
 * Created by xupeizuo on 2017/8/16.
 */

public abstract class VolleyErrorListener implements Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("LOGIN-ERROR", error.getMessage(), error);
        error.printStackTrace();
    }
}
