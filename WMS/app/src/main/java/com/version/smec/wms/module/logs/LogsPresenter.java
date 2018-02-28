package com.version.smec.wms.module.logs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.smec.wms.android.server.IFace;
import com.smec.wms.android.util.ToastUtil;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipModuleDto;
import com.version.smec.wms.module.logs.bean.LogsDto;
import com.version.smec.wms.utils.GsonHelper;

import org.json.JSONObject;

import java.util.WeakHashMap;

import static com.version.smec.wms.module.logs.LogsActivity.GETLOGS_FAIL;
import static com.version.smec.wms.module.logs.LogsActivity.GETLOGS_SUCCESS;

/**
 * Created by 小黑 on 2017/8/28.
 */
public class LogsPresenter extends WmsBasePresenter implements LogsContract{
    private Context mcontext;
    public LogsPresenter(Context context) {
        super(context);
        this.mcontext = context;
    }

    @Override
    public void getLogs(WeakHashMap<String, String> map) {
        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_LOGS_LIST
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                LogsDto logsDto =
                        GsonHelper.convertEntity(jsonStr, LogsDto.class);

                Log.e("sss_logsDto",jsonStr);

                if (logsDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(GETLOGS_SUCCESS, logsDto);
                }else{
                    mRxbus.post(GETLOGS_FAIL,logsDto);
                }
            }
        });
    }
}
