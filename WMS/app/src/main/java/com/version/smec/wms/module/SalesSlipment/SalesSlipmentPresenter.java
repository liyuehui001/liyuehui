package com.version.smec.wms.module.SalesSlipment;

import android.content.Context;

import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipGoodsModule;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipModule;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipModuleDto;
import com.version.smec.wms.module.SalesSlipment.bean.approval.ApprovalResponseSaleModule;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.GsonHelper;
import com.version.smec.wms.utils.NetworkState;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.GETSALESLIP_FAIL;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.GETSALESLIP_SUCCESS;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.SALESCHECK_CONFIRM;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.SALESCHECK_FAILED;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.SALESCHECK_SUCCESS;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.SALES_APPROVE_FAIL;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.SALES_APPROVE_SUCCESS;
import static com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity.SALES_APPROVE_NOT_SUCCESS;

/**
 * Created by 小黑 on 2017/8/18.
 */
public class SalesSlipmentPresenter extends WmsBasePresenter implements SalesSlipmentContract {
    private Context mcontext;
    public SalesSlipmentPresenter(Context context) {
        super(context);
        this.mcontext = context;
    }

    @Override
    public void getSaleSlip(WeakHashMap<String, String> map) {

        final String isOrder=map.get("isOrder");

        if(!NetworkState.networkConnected(WmsApplication.getContext())){
            if(isOrder !=null && isOrder.equals("T")){
                SalesSlipModuleDto salesSlipModuleDto=getDataFromDB(map.get("receiptNo"));
                if(salesSlipModuleDto != null && salesSlipModuleDto.getData()!=null){
                    mRxbus.post(GETSALESLIP_SUCCESS, salesSlipModuleDto);
                    return;
                }
            }
        }

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_PROJECT_DETAILS
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                SalesSlipModuleDto salesSlipModuleDto =
                        GsonHelper.convertEntity(jsonStr, SalesSlipModuleDto.class);
                if (salesSlipModuleDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    if(isOrder !=null && isOrder.equals("T")){
                        saveDataToDB(salesSlipModuleDto);
                    }
                    mRxbus.post(GETSALESLIP_SUCCESS, salesSlipModuleDto);
                }else{
                    mRxbus.post(GETSALESLIP_FAIL, salesSlipModuleDto);
                }
            }
        });

    }

    @Override
    public void refuseAgree(final Map<String, String> map) {//GET_SALES_APPROVE


        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_SALES_APPROVE
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRxbus.post(SALES_APPROVE_FAIL,new Object());
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                ApprovalResponseSaleModule arsModule =
                        GsonHelper.convertEntity(jsonStr, ApprovalResponseSaleModule.class);
                if (arsModule.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    deleteDataFromDB(map.get("receiptNo"));
                    mRxbus.post(SALES_APPROVE_SUCCESS, arsModule);
                }else{
                    mRxbus.post(SALES_APPROVE_NOT_SUCCESS, arsModule);
                }
            }
        });
    }

    @Override
    public void approveCheck(Map<String, String> map) {

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.APPROVE_CHECK
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRxbus.post(SALESCHECK_FAILED, new Object());
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                HttpResults httpResults=GsonHelper.convertEntity(jsonStr,HttpResults.class);
                if(httpResults == null || httpResults.getRetCode() == null){
                    return;
                }
                if(httpResults.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(SALESCHECK_SUCCESS,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.requestFailed)){
                    mRxbus.post(SALESCHECK_FAILED,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.reqtestConfirm)){
                    mRxbus.post(SALESCHECK_CONFIRM,httpResults);
                }
            }
        });
    }

    /**
     * 保存数据至本地
     * @param salesSlipModuleDto
     */
    public void saveDataToDB(SalesSlipModuleDto salesSlipModuleDto){
        if(salesSlipModuleDto == null || salesSlipModuleDto.getData() == null){
            return;
        }
        deleteDataFromDB(salesSlipModuleDto.getData().getReceiptNo());//删除本地数据
        realmManager.saveOrUpdate(salesSlipModuleDto.getData());
        realmManager.saveOrUpdateBatch(salesSlipModuleDto.getData().getMatnrs());
    }

    /**
     * 删除销售单数据
     * @param receiptNo
     */
    public void deleteDataFromDB(String receiptNo){
        realmManager.deleteByKey(SalesSlipModule.class,"receiptNo",receiptNo);//删除借用单数据
        realmManager.deleteByKey(SalesSlipGoodsModule.class,"receiptNo",receiptNo);//删除物料数据
    }

    /**
     * 获取本地缓存
     * @param receiptNo
     * @return
     */
    public SalesSlipModuleDto getDataFromDB(String receiptNo){
        SalesSlipModuleDto salesSlipModuleDto=new SalesSlipModuleDto();
        salesSlipModuleDto.setCode(WmsApi.requestCode.requestSuccess);
        //获取销售单 单据数据
        RealmObject realmObject=realmManager.findFirst(SalesSlipModule.class,"receiptNo",receiptNo);
        if(realmObject!=null){
            salesSlipModuleDto.setData((SalesSlipModule)realmObject);
            ArrayList<SalesSlipGoodsModule> matnrs;
            RealmResults<SalesSlipGoodsModule> realmResults=(RealmResults<SalesSlipGoodsModule>)realmManager.findAllByKey(SalesSlipGoodsModule.class,"receiptNo",receiptNo);

            if(CommonUtils.notEmpty(realmResults)){
                matnrs=(ArrayList<SalesSlipGoodsModule>)realmManager.getRealm().copyFromRealm(realmResults);
            }else {
                matnrs=new ArrayList<>();
            }

            salesSlipModuleDto.getData().setMatnrs(matnrs);
        }
        return salesSlipModuleDto;
    }
}
