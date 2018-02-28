package com.version.smec.wms.module.Acceptance;

import android.content.Context;
import android.util.Log;

import com.google.gson.internal.ObjectConstructor;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceEnclosureModel;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceMatnrsModel;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceModule;
import com.version.smec.wms.module.Acceptance.bean.approval.AcceptanceResponseApprovalModule;
import com.version.smec.wms.module.PendingApproval.PendingApprovalContract;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceModelDto;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.GsonHelper;
import com.version.smec.wms.utils.NetworkState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.ACCEPTANCECHECK_CONFIRM;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.ACCEPTANCECHECK_FAILED;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.ACCEPTANCECHECK_SUCCESS;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.Accepttance_FAIL;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.Accepttance_SUCCESS;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.ACCEPTANCE_APPROVAL_FAIL;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.ACCEPTANCE_APPROVAL_SUCCESS;
import static com.version.smec.wms.module.Acceptance.AcceptanceActivity.ACCEPTANCE_APPROVAL_NOT_SUCCESS;

/**
 * Created by Administrator on 2017/8/20.
 */
public class AcceptancePresenter extends WmsBasePresenter implements AcceptanceContract {
    public AcceptancePresenter(Context context) {
        super(context);
    }

    @Override
    public void search(final WeakHashMap<String, String> map) {

        final String isOrder=map.get("isOrder");

        if(!NetworkState.networkConnected(WmsApplication.getContext())){
            if(isOrder !=null && isOrder.equals("T")){
                AcceptanceModelDto acceptanceModelDto=getDataFromDB(map.get("receiptNo"));
                if(acceptanceModelDto!=null && acceptanceModelDto.getData()!=null){
                    mRxbus.post(Accepttance_SUCCESS, acceptanceModelDto);
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
                AcceptanceModelDto acceptanceModelDto= GsonHelper.convertEntity(jsonStr,AcceptanceModelDto.class);
                if (acceptanceModelDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    if(isOrder !=null && isOrder.equals("T")){
                        saveDataToDB(acceptanceModelDto);
                    }
                    mRxbus.post(Accepttance_SUCCESS, acceptanceModelDto);
                }else{
                    mRxbus.post(Accepttance_FAIL,acceptanceModelDto);
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
                mRxbus.post(ACCEPTANCE_APPROVAL_FAIL, new Object());
            }

            @Override
            public void onNext(JSONObject jsonObject) {
//                jsonObject就是从volleymanager中的RxVolleyRequest方法中subscriber.onNext(postRequest(subscriber,url, map, headersMap));参数接受的参数
                String jsonStr = jsonObject.toString();
                HttpResults httpResults=GsonHelper.convertEntity(jsonStr,HttpResults.class);
                if(httpResults == null || httpResults.getRetCode() == null){
                    return;
                }
                if(httpResults.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(ACCEPTANCECHECK_SUCCESS,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.requestFailed)){
                    mRxbus.post(ACCEPTANCECHECK_FAILED,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.reqtestConfirm)){
                    mRxbus.post(ACCEPTANCECHECK_CONFIRM,httpResults);
                }
            }
        });

    }

    @Override
    public void approvalAcceptance(final Map<String, String> map) {

         VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_ACCEPTANCE_APPROVE
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRxbus.post(ACCEPTANCE_APPROVAL_FAIL, new Object());
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                AcceptanceResponseApprovalModule aramodule=
                        GsonHelper.convertEntity(jsonStr,AcceptanceResponseApprovalModule.class);
                if (aramodule.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    deleteData(map.get("receiptNo"));
                    mRxbus.post(ACCEPTANCE_APPROVAL_SUCCESS, aramodule);
                }else{
                    mRxbus.post(ACCEPTANCE_APPROVAL_NOT_SUCCESS,aramodule);
                }
            }
        });
    }

    /**
     * 保存数据至本地
     * @param acceptanceModelDto
     */
    public void saveDataToDB(AcceptanceModelDto acceptanceModelDto){
        if(acceptanceModelDto == null || acceptanceModelDto.getData() == null){
            return;
        }
        deleteData(acceptanceModelDto.getData().getReceiptNo());
        realmManager.saveOrUpdate(acceptanceModelDto.getData());
        realmManager.saveOrUpdateBatch(acceptanceModelDto.getData().getMatnrs());
        realmManager.saveOrUpdateBatch(acceptanceModelDto.getData().getAttachs());
    }

    /**
     * 删除领用单数据
     * @param receiptNo
     */
    public void deleteData(String receiptNo){
        realmManager.deleteByKey(AcceptanceModule.class,"receiptNo",receiptNo);
        realmManager.deleteByKey(AcceptanceMatnrsModel.class,"receiptNo",receiptNo);
        realmManager.deleteByKey(AcceptanceEnclosureModel.class,"receiptNo",receiptNo);
    }

    /**
     * 获取本地数据
     * @param receiptNo
     * @return
     */
    public AcceptanceModelDto getDataFromDB(String receiptNo){
        AcceptanceModelDto acceptanceModelDto=new AcceptanceModelDto();
        acceptanceModelDto.setCode(WmsApi.requestCode.requestSuccess);
        RealmObject realmObject=realmManager.findFirst(AcceptanceModule.class,"receiptNo",receiptNo);
        if(realmObject!=null){
            acceptanceModelDto.setData((AcceptanceModule)realmObject);
            ArrayList<AcceptanceMatnrsModel> matnrs;
            ArrayList<AcceptanceEnclosureModel> attachs;
            RealmResults<AcceptanceMatnrsModel> formGoodModules=(RealmResults<AcceptanceMatnrsModel>)realmManager.findAllByKey(AcceptanceMatnrsModel.class,"receiptNo",receiptNo);
            RealmResults<AcceptanceEnclosureModel> appendixRealmResults=(RealmResults<AcceptanceEnclosureModel>)realmManager.findAllByKey(AcceptanceEnclosureModel.class,"receiptNo",receiptNo);

            if(CommonUtils.notEmpty(formGoodModules)){
                matnrs=(ArrayList<AcceptanceMatnrsModel>)realmManager.getRealm().copyFromRealm(formGoodModules);
            }else {
                matnrs=new ArrayList<>();
            }

            if(CommonUtils.notEmpty(appendixRealmResults)){
                attachs=(ArrayList<AcceptanceEnclosureModel>)realmManager.getRealm().copyFromRealm(appendixRealmResults);
            }else {
                attachs=new ArrayList<>();
            }

            acceptanceModelDto.getData().setMatnrs(matnrs);
            acceptanceModelDto.getData().setAttachs(attachs);
        }
        return acceptanceModelDto;
    }
}
