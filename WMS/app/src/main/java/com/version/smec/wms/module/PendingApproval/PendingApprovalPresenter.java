package com.version.smec.wms.module.PendingApproval;

import android.content.Context;

import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.server.IFace;
import com.version.smec.wms.WmsManager.VolleyManager.BaseSubscriber;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.base.WmsBasePresenter;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModel;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModelDto;
import com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentInside;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.GsonHelper;
import com.version.smec.wms.utils.NetworkState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.WeakHashMap;

import io.realm.RealmResults;

import static com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentHeadquarters.SEARCH_FAIL;
import static com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentHeadquarters.SEARCH_SUCCESS;


/**
 * Created by xupeizuo on 2017/8/15.
 */
public class PendingApprovalPresenter extends WmsBasePresenter implements PendingApprovalContract{

    public PendingApprovalPresenter(Context context) {
        super(context);
    }

    @Override
    public void search(final WeakHashMap<String, String> map) {

        VolleyManager.schedulerThread(VolleyManager.RxVolleyRequest(IFace.GET_PROJECT_LIST
                ,map,headersMap)).subscribe(new BaseSubscriber<JSONObject>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                String module = map.get("queryModule");
                if(module.equals("headTaskList") || module.equals("headOrderList")){//分公司总部
                    mRxbus.post(SEARCH_FAIL,new RequirementModelDto());
                }else {//分公司内部
                    mRxbus.post(PendingFragmentInside.BRANCH_SEARCH_FAIL,new RequirementModelDto());
                }
            }

            @Override
            public void onNext(JSONObject jsonObject) {
                String jsonStr = jsonObject.toString();
                RequirementModelDto requirementModelDto  =
                        GsonHelper.convertEntity(jsonStr, RequirementModelDto.class);
                String module = map.get("queryModule");
                if (requirementModelDto.getCode().equals("SUCCESS")){
                    if(module.equals("headTaskList") || module.equals("headOrderList")){//分公司总部
                        mRxbus.post(SEARCH_SUCCESS, requirementModelDto);
                    }else {//分公司内部
                        mRxbus.post(PendingFragmentInside.BRANCH_SEARCH_SUCCESS, requirementModelDto);
                    }
                }else {
                    if(module.equals("headTaskList") || module.equals("headOrderList")){//分公司总部
                        mRxbus.post(SEARCH_FAIL,requirementModelDto);
                    }else {//分公司内部
                        mRxbus.post(PendingFragmentInside.BRANCH_SEARCH_FAIL,requirementModelDto);
                    }
                }
            }
        });
    }

    /**
     * 获取当前列表数据在某种情况下为空则不保存数据
     * 否则保存20条 删掉之前的数据
     * @param data
     * @return
     */
    @Override
    public boolean saveDataToDB(int currentFragment,ArrayList<RequirementModel> data) {
        if(CommonUtils.notEmpty(data)){
            if(currentFragment == 0){
                realmManager.deleteByKey(RequirementModel.class,"receiptType","WMS_XQ");
            }else {
                realmManager.deleteByKey(RequirementModel.class,"receiptType","WMS_JY");
                realmManager.deleteByKey(RequirementModel.class,"receiptType","WMS_LY");
                realmManager.deleteByKey(RequirementModel.class,"receiptType","WMS_XS");
            }
            if(data.size()>20){
                for(int i =0;i<20;i++){
                    realmManager.saveOrUpdate(data.get(i));
                }
            } else {
                realmManager.saveOrUpdateBatch(data);
            }
            return true;
        }else {
            return false;
        }
    }

    /**
     * 读取本地数据
     * @param currentFragment
     * @return
     */
    @Override
    public ArrayList<RequirementModel> getDataFromDB(int currentFragment) {
        ArrayList<RequirementModel> arrayList=new ArrayList<>();
        if(currentFragment == 0){
            RealmResults<RequirementModel> results=(RealmResults<RequirementModel>)realmManager.findAllByKey(RequirementModel.class,"receiptType","WMS_XQ");
            if(!CommonUtils.notEmpty(results)){
                return arrayList;
            }
            ArrayList<RequirementModel> xq=(ArrayList<RequirementModel>)realmManager.getRealm().copyFromRealm(results);
            arrayList.addAll(xq);
        }else {

            RealmResults<RequirementModel> jy=(RealmResults<RequirementModel>)realmManager.findAllByKey(RequirementModel.class,"receiptType","WMS_JY");
            RealmResults<RequirementModel> ly=(RealmResults<RequirementModel>)realmManager.findAllByKey(RequirementModel.class,"receiptType","WMS_LY");
            RealmResults<RequirementModel> xs=(RealmResults<RequirementModel>)realmManager.findAllByKey(RequirementModel.class,"receiptType","WMS_XS");

            if(CommonUtils.notEmpty(jy)){
                ArrayList<RequirementModel> JY=(ArrayList<RequirementModel>)realmManager.getRealm().copyFromRealm(jy);
                arrayList.addAll(JY);
            }
            if(CommonUtils.notEmpty(ly)){
                ArrayList<RequirementModel> LY=(ArrayList<RequirementModel>)realmManager.getRealm().copyFromRealm(ly);
                arrayList.addAll(LY);
            }
            if(CommonUtils.notEmpty(xs)){
                ArrayList<RequirementModel> XS=(ArrayList<RequirementModel>)realmManager.getRealm().copyFromRealm(xs);
                arrayList.addAll(XS);
            }
        }
        return arrayList;
    }
}
