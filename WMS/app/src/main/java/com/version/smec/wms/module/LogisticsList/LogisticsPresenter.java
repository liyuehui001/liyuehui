package com.version.smec.wms.module.LogisticsList;

import android.content.Context;
import android.util.Log;

import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.module.LogisticsList.bean.LogisticsDto;
import com.version.smec.wms.module.Requirements.bean.RequirementDataModuleDto;
import com.version.smec.wms.utils.GsonHelper;

import org.json.JSONObject;

import java.util.WeakHashMap;

import static com.version.smec.wms.module.LogisticsList.LogisticsActivity.GETLOGISTICS_FAIL;

import static com.version.smec.wms.module.LogisticsList.LogisticsActivity.GETLOGISTICS_SUCCESS;

/**
 * Created by 小黑 on 2017/8/23.
 */
public class LogisticsPresenter extends WmsBasePresenter implements LogisticsContract {

    public LogisticsPresenter(Context context) {
        super(context);
    }

    @Override
    public void getLogisticsList(WeakHashMap<String, String> map) {

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_LOGISTICS_LIST
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                LogisticsDto logisticsDto =
                        GsonHelper.convertEntity(jsonStr, LogisticsDto.class);

                Log.e("sss",jsonStr);

                if (logisticsDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(GETLOGISTICS_SUCCESS, logisticsDto);
                }else{
                    mRxbus.post(GETLOGISTICS_FAIL,logisticsDto);
                }
            }
        });

    }
}
