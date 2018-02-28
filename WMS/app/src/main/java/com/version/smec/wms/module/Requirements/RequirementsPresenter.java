package com.version.smec.wms.module.Requirements;

import android.content.Context;
import android.util.Log;

import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;

import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.module.Requirements.bean.RequirementDataModuleDto;
import com.version.smec.wms.module.Requirements.bean.RequirementDetailModel;
import com.version.smec.wms.module.Requirements.bean.RequirementMatnrsModel;
import com.version.smec.wms.module.Requirements.bean.approval.ApprovalResponseModule;
import com.version.smec.wms.module.Requirements.bean.approval.RequirementMatnrsModelForRealm;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.GsonHelper;
import com.version.smec.wms.utils.NetworkState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.WeakHashMap;

import static com.version.smec.wms.module.Requirements.RequirementsActivity.GETREQUIREMENT_FAIL;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.GETREQUIREMENT_SUCCESS;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.REQUIREMENTAPPROVE_FAIL;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.REQUIREMENTAPPROVE_SUCCESS;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.REQUIREMENTAPPROVE_NOT_SUCCESS;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.REQUIREMENTCHECK_CONFIRM;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.REQUIREMENTCHECK_FAILED;
import static com.version.smec.wms.module.Requirements.RequirementsActivity.REQUIREMENTCHECK_SUCCESS;

import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by xupeizuo on 2017/8/15.
 */

public class RequirementsPresenter extends WmsBasePresenter implements RequirementsContract{

    public RequirementsPresenter(Context context) {
        super(context);
    }

    @Override
    public void requirementDetail(WeakHashMap<String, String> map) {

        final String isOrder=map.get("isOrder");
        if(!NetworkState.networkConnected(WmsApplication.getContext())){
            if(isOrder !=null && isOrder.equals("T")){
                RequirementDetailModel requirementDetailModel=getDataFromDB(map.get("receiptNo"));
                if(requirementDetailModel!=null){
                    RequirementDataModuleDto requirementDataModuleDto=new RequirementDataModuleDto();
                    requirementDataModuleDto.setData(requirementDetailModel);
                    requirementDataModuleDto.setCode(WmsApi.requestCode.requestSuccess);
                    mRxbus.post(GETREQUIREMENT_SUCCESS, requirementDataModuleDto);
                    return;
                }
            }
        }
        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_PROJECT_DETAILS
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRxbus.post(GETREQUIREMENT_FAIL,new RequirementDataModuleDto());
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                RequirementDataModuleDto requirementDataModuleDto =
                        GsonHelper.convertEntity(jsonStr, RequirementDataModuleDto.class);

                if (requirementDataModuleDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    if(isOrder !=null && isOrder.equals("T")){
                        saveDataToDB(requirementDataModuleDto);//保存数据至本地
                    }
                    mRxbus.post(GETREQUIREMENT_SUCCESS, requirementDataModuleDto);
                }else{
                    mRxbus.post(GETREQUIREMENT_FAIL,requirementDataModuleDto);
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
            }
            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                HttpResults httpResults=GsonHelper.convertEntity(jsonStr,HttpResults.class);
                if(httpResults == null || httpResults.getRetCode() == null){
                    return;
                }
                if(httpResults.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(REQUIREMENTCHECK_SUCCESS,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.requestFailed)){
                    mRxbus.post(REQUIREMENTCHECK_FAILED,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.reqtestConfirm)){
                    mRxbus.post(REQUIREMENTCHECK_CONFIRM,httpResults);
                }
            }
        });
    }



    @Override
    public void approveRequrement(final Map<String, String> map) {

        for (Map.Entry<String,String> entry : map.entrySet()){
            Log.e(entry.getKey(),entry.getValue());
        }

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_REQUIREMENT_APPROVE
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRxbus.post(REQUIREMENTAPPROVE_FAIL,new Object());
            }
            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                ApprovalResponseModule requirementApproveModule =
                        GsonHelper.convertEntity(jsonStr, ApprovalResponseModule.class);
                if(requirementApproveModule.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                   deleteObject(map.get("receiptNo"));
                }
                if (requirementApproveModule.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    mRxbus.post(REQUIREMENTAPPROVE_SUCCESS, requirementApproveModule);
                }else{
                    mRxbus.post(REQUIREMENTAPPROVE_NOT_SUCCESS, requirementApproveModule);
                }

            }
        });

    }

    /**
     * 保存数据至db
     * @param requirementDataModuleDto
     * @return
     */
    public boolean saveDataToDB(RequirementDataModuleDto requirementDataModuleDto){
        if(requirementDataModuleDto == null || requirementDataModuleDto.getData() == null){
            return false;
        }
        //先删除原先receiptNo的单据
        deleteObject(requirementDataModuleDto.getData().getReceiptNo());
        realmManager.saveOrUpdate(requirementDataModuleDto.getData());
        if(CommonUtils.notEmpty(requirementDataModuleDto.getData().getRequirementMatnrsModelArrayList())){
            for(int i=0;i<requirementDataModuleDto.getData().getRequirementMatnrsModelArrayList().size();i++){
                RequirementMatnrsModel matnrsModel=requirementDataModuleDto.getData().getRequirementMatnrsModelArrayList().get(i);
                RequirementMatnrsModelForRealm forRealm=new RequirementMatnrsModelForRealm();
                forRealm.setMatnrId(matnrsModel.getMatnrId());
                forRealm.setChangedMatnr(matnrsModel.getChangedMatnr());
                forRealm.setDescription(matnrsModel.getDescription());
                forRealm.setHdQty(matnrsModel.getHdQty());
                forRealm.setLineStatus(matnrsModel.getLineStatus());
                forRealm.setMatnr(matnrsModel.getMatnr());
                forRealm.setMatnrName(matnrsModel.getMatnrName());
                forRealm.setQty(matnrsModel.getQty());
                forRealm.setReamrk(matnrsModel.getRemark());
                forRealm.setUnit(matnrsModel.getUnit());
                forRealm.setReceiptNo(matnrsModel.getReceiptNo());
                realmManager.saveOrUpdate(forRealm);
            }
        }
        return true;
    }

    /**
     * 删除需求单
     * @return
     */
    public void deleteObject(String receiptNo){
        realmManager.deleteByKey(RequirementDetailModel.class,"receiptNo",receiptNo);//删除单据数据
        realmManager.deleteByKey(RequirementMatnrsModelForRealm.class,"receiptNo",receiptNo);//删除物料数据
    }

    /**
     * 获取数据
     * @param receiptNo
     * @return
     */
    public RequirementDetailModel getDataFromDB(String receiptNo){

        RequirementDetailModel data=(RequirementDetailModel)realmManager.findFirst(RequirementDetailModel.class,"receiptNo",receiptNo);
        if(data == null){
            return null;
        }

        RealmResults<RequirementMatnrsModelForRealm> results=realmManager.findRealmModel(RequirementMatnrsModelForRealm.class,"receiptNo",receiptNo);
        if(!CommonUtils.notEmpty(results)){
            data.setRequirementMatnrsModelArrayList(new ArrayList<RequirementMatnrsModel>());
            return data;
        }
        ArrayList<RequirementMatnrsModelForRealm> requirementMatnrsModels=(ArrayList<RequirementMatnrsModelForRealm>)realmManager.getRealm().copyFromRealm(results);
        ArrayList<RequirementMatnrsModel> arrayList=new ArrayList<>();
        for(int i=0;i<requirementMatnrsModels.size();i++){
            RequirementMatnrsModelForRealm matnrsModel=requirementMatnrsModels.get(i);
            RequirementMatnrsModel forRealm=new RequirementMatnrsModel();
            forRealm.setMatnrId(matnrsModel.getMatnrId());
            forRealm.setChangedMatnr(matnrsModel.getChangedMatnr());
            forRealm.setDescription(matnrsModel.getDescription());
            forRealm.setHdQty(matnrsModel.getHdQty());
            forRealm.setLineStatus(matnrsModel.getLineStatus());
            forRealm.setMatnr(matnrsModel.getMatnr());
            forRealm.setMatnrName(matnrsModel.getMatnrName());
            forRealm.setQty(matnrsModel.getQty());
            forRealm.setRemark(matnrsModel.getReamrk());
            forRealm.setUnit(matnrsModel.getUnit());
            forRealm.setReceiptNo(matnrsModel.getReceiptNo());
            arrayList.add(forRealm);
        }
        data.setRequirementMatnrsModelArrayList(arrayList);

        return data;
    }
}
