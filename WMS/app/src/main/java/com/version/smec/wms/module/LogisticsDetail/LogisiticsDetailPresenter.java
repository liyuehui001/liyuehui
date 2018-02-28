package com.version.smec.wms.module.LogisticsDetail;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormDto;
import com.version.smec.wms.module.LogisticsDetail.bean.LogisiticsDetailDto;
import com.version.smec.wms.utils.GsonHelper;

import org.json.JSONObject;

import java.util.WeakHashMap;

import static com.version.smec.wms.module.LogisticsDetail.LogisiticsDetailActivity.GETLOGISITICS_FAIL;
import static com.version.smec.wms.module.LogisticsDetail.LogisiticsDetailActivity.GETLOGISITICS_SUCCESS;

/**
 * Created by 小黑 on 2017/8/24.
 */
public class LogisiticsDetailPresenter extends WmsBasePresenter implements LogisiticsDetailContract {


    public LogisiticsDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getLogisicsDetail(WeakHashMap<String, String> map) {

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_LOGISTICS_DETAIL
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {

            @Override
            public void onNext(JSONObject jsonObject) {

                String jsonStr = jsonObject.toString();
                LogisiticsDetailDto logisiticsDetailDto =
                        GsonHelper.convertEntity(jsonStr, LogisiticsDetailDto.class);
                Log.e("sss_ssLogisiticsDetail",jsonStr);
                if (logisiticsDetailDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(GETLOGISITICS_SUCCESS, logisiticsDetailDto);
                }else{
                    mRxbus.post(GETLOGISITICS_FAIL, logisiticsDetailDto);
                }
            }
        });

    }
}
