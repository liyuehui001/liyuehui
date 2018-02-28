package com.version.smec.wms.module.BorrowingForm;

import android.content.Context;

import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormAppendix;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormDto;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormGoodModule;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormModule;
import com.version.smec.wms.module.BorrowingForm.bean.approval.ApprovalBorrowResponseModule;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.GsonHelper;
import com.version.smec.wms.utils.NetworkState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.BORROWFORMCHECK_CONFIRM;
import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.BORROWFORMCHECK_SUCCESS;
import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.GETBORROWFORM_FAIL;
import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.GETBORROWFORM_SUCCESS;
import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.BORROWFORM_APPROVAL_NOT_SUCCESS;
import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.BORROWFORM_APPROVAL_FAIL;
import static com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity.BORROWFORM_APPROVAL_SUCCESS;

/**
 * Created by 小黑 on 2017/8/21.
 */
public class BorrowingFormPresenter extends WmsBasePresenter implements BorrowingFormContract {
    public BorrowingFormPresenter(Context context) {
        super(context);
    }

    @Override
    public void getBorrowingFormData(WeakHashMap<String, String> map) {

        final String isOrder=map.get("isOrder");

        if(!NetworkState.networkConnected(WmsApplication.getContext())){
            if(isOrder !=null && isOrder.equals("T")){
                BorrowingFormDto borrowingFormDto=getDataFromDB(map.get("receiptNo"));
                if(borrowingFormDto !=null && borrowingFormDto.getData()!=null){
                    mRxbus.post(GETBORROWFORM_SUCCESS, borrowingFormDto);
                    return;
                }
            }
        }

        VolleyManager.schedulerThread(
                VolleyManager.RxVolleyRequest(IFace.GET_PROJECT_DETAILS,map,headersMap))
                .subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                BorrowingFormDto borrowingFormDto =
                        GsonHelper.convertEntity(jsonStr, BorrowingFormDto.class);
                if (borrowingFormDto.getCode().equals(WmsApi.requestCode.requestSuccess)){
                    if(isOrder !=null && isOrder.equals("T")){
                        saveDataToDB(borrowingFormDto);
                    }
                    mRxbus.post(GETBORROWFORM_SUCCESS, borrowingFormDto);
                }else{
                    mRxbus.post(GETBORROWFORM_FAIL,borrowingFormDto);
                }
            }
        });

    }

    @Override
    public void approvalBorrowForm(final Map<String, String> map) {

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_BORROW_APPROVE
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRxbus.post(BORROWFORM_APPROVAL_FAIL,new Object());
            }
            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                ApprovalBorrowResponseModule borrowingFormDto =
                        GsonHelper.convertEntity(jsonStr, ApprovalBorrowResponseModule.class);
                if (borrowingFormDto.getRetCode().equals(WmsApi.requestCode.requestSuccess)){
                    deleteDataFromDB(map.get("receiptNo"));
                    mRxbus.post(BORROWFORM_APPROVAL_SUCCESS, borrowingFormDto);
                }else{
                    mRxbus.post(BORROWFORM_APPROVAL_NOT_SUCCESS, borrowingFormDto);
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
                    mRxbus.post(BORROWFORMCHECK_SUCCESS,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.requestFailed)){
                    mRxbus.post(BORROWFORM_APPROVAL_FAIL,httpResults);
                }
                else if(httpResults.getRetCode().equals(WmsApi.requestCode.reqtestConfirm)){
                    mRxbus.post(BORROWFORMCHECK_CONFIRM,httpResults);
                }
            }
        });
    }

    /**
     * 保存数据至本地
     * @param borrowingFormDto
     */
    public void saveDataToDB(BorrowingFormDto borrowingFormDto){
        if(borrowingFormDto == null || borrowingFormDto.getData() == null){
            return;
        }
        deleteDataFromDB(borrowingFormDto.getData().getReceiptNo());//删除本地数据
        realmManager.saveOrUpdate(borrowingFormDto.getData());
        realmManager.saveOrUpdateBatch(borrowingFormDto.getData().getMatnrs());
        realmManager.saveOrUpdateBatch(borrowingFormDto.getData().getAttachs());
    }

    /**
     * 删除借用单数据
     * @param receiptNo
     */
    public void deleteDataFromDB(String receiptNo){
        realmManager.deleteByKey(BorrowingFormModule.class,"receiptNo",receiptNo);//删除借用单数据
        realmManager.deleteByKey(BorrowingFormGoodModule.class,"receiptNo",receiptNo);//删除物料数据
        realmManager.deleteByKey(BorrowingFormAppendix.class,"receiptNo",receiptNo);//删除附件信息
    }

    /**
     * 获取本地缓存
     * @param receiptNo
     * @return
     */
    public BorrowingFormDto getDataFromDB(String receiptNo){
        BorrowingFormDto borrowingFormDto=new BorrowingFormDto();
        borrowingFormDto.setCode(WmsApi.requestCode.requestSuccess);
        //获取借用单 单据数据
        RealmObject realmObject=realmManager.findFirst(BorrowingFormModule.class,"receiptNo",receiptNo);
        if(realmObject!=null){
            borrowingFormDto.setData((BorrowingFormModule)realmObject);
            ArrayList<BorrowingFormGoodModule> matnrs;
            ArrayList<BorrowingFormAppendix> attachs;
            RealmResults<BorrowingFormGoodModule> formGoodModules=(RealmResults<BorrowingFormGoodModule>)realmManager.findAllByKey(BorrowingFormGoodModule.class,"receiptNo",receiptNo);
            RealmResults<BorrowingFormAppendix> appendixRealmResults=(RealmResults<BorrowingFormAppendix>)realmManager.findAllByKey(BorrowingFormAppendix.class,"receiptNo",receiptNo);

            if(CommonUtils.notEmpty(formGoodModules)){
                matnrs=(ArrayList<BorrowingFormGoodModule>)realmManager.getRealm().copyFromRealm(formGoodModules);
            }else {
                matnrs=new ArrayList<>();
            }

            if(CommonUtils.notEmpty(appendixRealmResults)){
                attachs=(ArrayList<BorrowingFormAppendix>)realmManager.getRealm().copyFromRealm(appendixRealmResults);
            }else {
                attachs=new ArrayList<>();
            }

            borrowingFormDto.getData().setMatnrs(matnrs);
            borrowingFormDto.getData().setAttachs(attachs);
        }
        return borrowingFormDto;
    }

}
