package com.version.smec.wms.module.Requirements;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.databinding.ActivityRequirementsBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.api.WmsConstant;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.bean.RemoveBean;
import com.version.smec.wms.module.LogisticsList.LogisticsActivity;
import com.version.smec.wms.module.Requirements.RequirementsAdapter.RequirementsAdapter;
import com.version.smec.wms.module.Requirements.bean.RequirementDataModuleDto;
import com.version.smec.wms.module.Requirements.bean.RequirementDetailModel;
import com.version.smec.wms.module.Requirements.bean.RequirementMatnrsModel;
import com.version.smec.wms.module.Requirements.bean.approval.ApprovalResponseModule;
import com.version.smec.wms.module.Requirements.bean.approval.MatnrModule;
import com.version.smec.wms.module.logs.LogsActivity;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.FullyLinearLayoutManager;
import com.version.smec.wms.utils.ToastUtils;
import com.version.smec.wms.widget.CustomDialog;
import com.version.smec.wms.widget.CustomePopupWindow;
import com.version.smec.wms.widget.TopBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by xupeizuo on 2017/8/14.
 */

public class RequirementsActivity extends WmsBaseActivity<RequirementsPresenter> implements RequirementsContract {


    public static final String GETREQUIREMENT_FAIL="SEARCH_FAIL";
    public static final String GETREQUIREMENT_SUCCESS="SEARCH_SUCCESS";
    public static final String REQUIREMENTAPPROVE_SUCCESS="REQUIREMENTAPPROVE_SUCCESS";
    public static final String REQUIREMENTAPPROVE_FAIL="REQUIREMENTAPPROVE_FAIL";
    public static final String REQUIREMENTAPPROVE_NOT_SUCCESS="REQUIREMENTAPPROVE_NOT_SUCCESS";
    public static final String REQUIREMENTCHECK_SUCCESS="REQUIREMENTCHECK_SUCCESS";
    public static final String REQUIREMENTCHECK_FAILED="REQUIREMENTCHECK_FAILED";
    public static final String REQUIREMENTCHECK_CONFIRM="REQUIREMENTCHECK_CONFIRM";

    private ActivityRequirementsBinding activityRequirementsBinding;
    private ArrayList<RequirementMatnrsModel> arrayList;
    private RequirementsAdapter adapter;
    private RequirementDetailModel requirementDetailModel;
    private ProgressDialog pd;

    private boolean todoTrueBillFalse;
    private final boolean[] is_spread = {true};

    private String userRole;
    private String receiptNo,receiptType;
    private String operation;
    private String isUrgent;
    private HashMap<String ,String> map = new HashMap<String, String>();
    private CustomDialog customDialog;

    private boolean isDispatch=false;//是否是急件 false --非急件，true --急件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequirementsBinding=DataBindingUtil
                .setContentView(this, R.layout.activity_requirements);
        pd = WmsConstant.getProgressDialog();
        _firstinitView();
        _initArraylist();

    }

    private void _firstinitView() {
        Intent intent = getIntent();
        userRole = AppUtil.getValue(this,WMS_USER_ROLE);
        todoTrueBillFalse = intent.getBooleanExtra("todoTrueBillFalse",false);
        receiptNo = intent.getStringExtra("receiptNo");
        receiptType = intent.getStringExtra("receiptType");
        operation = intent.getStringExtra("operation");
        isUrgent = intent.getStringExtra("isUrgent");


        TextPaint tp =  activityRequirementsBinding.includeRequirementsTop.tvXuqiuDetail.getPaint();
        tp.setFakeBoldText(true);

        CommonUtils.styleBoldTextView(activityRequirementsBinding.includeRequirementsTop.tvXuqiuDetail);
        CommonUtils.styleBoldTextView(activityRequirementsBinding.matnrTitleInfo);




        _setTopBar();

        _init_hide_before_request();

        activityRequirementsBinding.recyclerView.setVisibility(View.VISIBLE);
        activityRequirementsBinding.btnAllSpreadPackup.setText("收起");

        activityRequirementsBinding.cvTodoListDetailTitle.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                RequirementsActivity.this.finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {
                Intent mintent = new Intent(RequirementsActivity.this,LogsActivity.class);
                mintent.putExtra("receiptNo",receiptNo);
                mintent.putExtra("receiptType",WmsApi.billForm.requireForm);
                startActivity(mintent);
            }
        });


        activityRequirementsBinding.includeRequirementsTop.llHideShow.setVisibility(View.GONE);
        activityRequirementsBinding.includeRequirementsTop.tvSpreadPackup.setText("查看详情");
        activityRequirementsBinding.includeRequirementsTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);
        activityRequirementsBinding.includeRequirementsTop.llCtrlHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityRequirementsBinding.includeRequirementsTop.llHideShow.getVisibility() == View.GONE){
                    activityRequirementsBinding.includeRequirementsTop.llHideShow.setVisibility(View.VISIBLE);
                    activityRequirementsBinding.includeRequirementsTop.tvSpreadPackup.setText("收起");
                    activityRequirementsBinding.includeRequirementsTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_packup);
                }else{
                    activityRequirementsBinding.includeRequirementsTop.llHideShow.setVisibility(View.GONE);
                    activityRequirementsBinding.includeRequirementsTop.tvSpreadPackup.setText("查看详情");
                    activityRequirementsBinding.includeRequirementsTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);

                }
            }
        });
    }

    private void _setTopBar() {
        if (todoTrueBillFalse){
            activityRequirementsBinding
                    .cvTodoListDetailTitle
                    .getTvTextTitle().setText("待办详情");
            activityRequirementsBinding.includeRequirementsTop
                    .llOrderState.setVisibility(View.GONE);

        }else{
            activityRequirementsBinding
                    .cvTodoListDetailTitle
                    .getTvTextTitle().setText("单据详情");

            activityRequirementsBinding.includeRequirementsTop
                    .llOrderState.setVisibility(View.VISIBLE);
        }
    }

    private void _init_hide_before_request() {

        activityRequirementsBinding
                .includeRequirementsTop.ivIsurgent.setVisibility(View.GONE);//隐藏是否是急件图标


        activityRequirementsBinding.llBottomBtnTwo.setVisibility(View.GONE);
        activityRequirementsBinding.llConfirm.setVisibility(View.GONE);
        activityRequirementsBinding.llCheckMatnrsLiu.setVisibility(View.GONE);
        activityRequirementsBinding.svMainContent.setVisibility(View.GONE);
//        pd = CommonUtils.showProgessDialog(pd,RequirementsActivity.this,"正在请求...");

    }

    private void _activity_button_show_after_request(String isUrgent) {
//        CommonUtils.hideProgressDialog(pd);
        if (todoTrueBillFalse){
            if (isUrgent.equals("Y")){//需求单急件

                if (userRole.contains(WmsApi.RoleCode.WMSMaintenanceClerk)){//总部备件管理员
                    activityRequirementsBinding.llCheckMatnrsLiu.setVisibility(View.GONE);
                    activityRequirementsBinding.llBottomBtnTwo.setVisibility(View.VISIBLE);
                    activityRequirementsBinding.llConfirm.setVisibility(View.GONE);
                }else if (userRole.contains(WmsApi.RoleCode.WMS_BRANCH_MGR)){//总经理
                    activityRequirementsBinding.llCheckMatnrsLiu.setVisibility(View.GONE);
                    activityRequirementsBinding.llBottomBtnTwo.setVisibility(View.GONE);
                    activityRequirementsBinding.llConfirm.setVisibility(View.VISIBLE);
                }
            }else{

                activityRequirementsBinding.llCheckMatnrsLiu.setVisibility(View.GONE);
                activityRequirementsBinding.llBottomBtnTwo.setVisibility(View.VISIBLE);
                activityRequirementsBinding.llConfirm.setVisibility(View.GONE);
            }
        }else{
            activityRequirementsBinding.llBottomBtnTwo.setVisibility(View.GONE);
            activityRequirementsBinding.llConfirm.setVisibility(View.GONE);
            activityRequirementsBinding.llCheckMatnrsLiu.setVisibility(View.GONE);
        }

        activityRequirementsBinding.svMainContent.setVisibility(View.VISIBLE);

    }

    private void _initArraylist() {
        arrayList =new ArrayList<>();
        requirementDetailModel=new RequirementDetailModel();
        activityRequirementsBinding.setVariable(BR.requirement_detail,requirementDetailModel);
        WeakHashMap<String, String> params = new WeakHashMap<>();
        requirementDetail(params);

    }

    @Override
    public RequirementsPresenter getPresenter() {
        return new RequirementsPresenter(this);
    }

    private void _initView(){
        //判断是--- 待办详情  还是---单据详情

        _activity_button_show_after_request(isUrgent);

        if (!todoTrueBillFalse
                && CommonUtils.notEmptyStr(requirementDetailModel.getStatusCode())
                &&requirementDetailModel.getStatusCode().equals(WmsApi.docStatus.closeStatus)){//单据详情，并且状态为CLOSED
            activityRequirementsBinding.llCheckMatnrsLiu.setVisibility(View.VISIBLE);
        }
        activityRequirementsBinding.setVariable(BR.requirement_detail,requirementDetailModel);
        activityRequirementsBinding
                .includeRequirementsTop.ivIsurgent.setVisibility(View.VISIBLE);

        //设置是否发往公司
        if (CommonUtils.notEmptyStr(requirementDetailModel.getIsToBranch()) && requirementDetailModel.getIsToBranch().equals("Y")){

            activityRequirementsBinding.includeRequirementsTop.tvIsTogoCompany.setText("是");
        }else{

            activityRequirementsBinding.includeRequirementsTop.tvIsTogoCompany.setText("否");
        }
        adapter=new RequirementsAdapter(this,todoTrueBillFalse,userRole,requirementDetailModel.getIsUrgent());
        adapter.setArrayList(arrayList);
        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
        activityRequirementsBinding.recyclerView.setLayoutManager(mLayoutManager);
        activityRequirementsBinding.recyclerView.setNestedScrollingEnabled(false);
        activityRequirementsBinding.recyclerView.setAdapter(adapter);

        activityRequirementsBinding.llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                getParmsMap(WmsApi.approveCode.APPROVED,"");
                pd = CommonUtils.showProgessDialog(pd,RequirementsActivity.this,"正在校验...");
                mPresenter.approveRequrement(map);
            }
        });

        activityRequirementsBinding.btnAllSpreadPackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_spread[0]){
                    activityRequirementsBinding.recyclerView.setVisibility(View.GONE);
                    activityRequirementsBinding.btnAllSpreadPackup.setText("展开");
                    is_spread[0] = false;
                }else{
                    activityRequirementsBinding.recyclerView.setVisibility(View.VISIBLE);
                    activityRequirementsBinding.btnAllSpreadPackup.setText("收起");
                    is_spread[0] = true;
                }
            }
        });

        _judgeHavingData();
        activityRequirementsBinding.llCheckMatnrsLiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent = new Intent(RequirementsActivity.this, LogisticsActivity.class);
                mintent.putExtra("receiptNo",requirementDetailModel.getReceiptNo());
                startActivity(mintent);
            }
        });

        //打电话
        activityRequirementsBinding.includeRequirementsTop.llCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = requirementDetailModel.getContactPhone();
                if (!TextUtils.isEmpty(phone)){
                    CommonUtils.callPhone(phone,RequirementsActivity.this);
                }else{
                    ToastUtil.show(RequirementsActivity.this,WmsApi.tip.noRecordPhone,Toast.LENGTH_SHORT);
                }
            }
        });

        activityRequirementsBinding.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.clear();
                getParmsMap("","");
                approveCheck(map);
                pd = CommonUtils.showProgessDialog(pd,RequirementsActivity.this,"正在校验...");
            }
        });

        activityRequirementsBinding.tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(RequirementsActivity.this);
                builder.setTitle("驳回意见");
                builder.seteditTextHint("必填（最多200字）");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //驳回，
                        if (builder.getEditText().getText().toString().trim()==null
                                || builder.getEditText().getText().toString().trim().equals("")){
                            ToastUtil.show(RequirementsActivity.this,"驳回理由不能为空",Toast.LENGTH_LONG);
                        }else{
                            map.clear();
                            getParmsMap(WmsApi.approveCode.REJECTED,builder.getEditText().getText().toString().trim());
                            approveRequrement(map);
                            dialog.dismiss();//提交
                            pd = CommonUtils.showProgessDialog(pd,RequirementsActivity.this,"正在提交...");
                        }
                    }
                });
                CommonUtils.getWidthHeight(builder.create(),RequirementsActivity.this).show();
            }
        });

    }

    private void _judgeHavingData() {
        if (!CommonUtils.notEmpty(arrayList)){//物料
            activityRequirementsBinding.btnAllSpreadPackup.setText(WmsApi.tip.NowNotAvailable);
            activityRequirementsBinding.btnAllSpreadPackup.setClickable(false);
            activityRequirementsBinding.btnAllSpreadPackup.setBackgroundResource(R.drawable.bg_corner_gray);
        }
    }

    @Override
    public void requirementDetail(WeakHashMap<String, String> map) {
        map.put("receiptType",WmsApi.billForm.requireForm);
        map.put("receiptNo",receiptNo);
        map.put("isOrder",todoTrueBillFalse ? "T" : "F");
        mPresenter.requirementDetail(map);
    }

    @Override
    public void approveCheck(Map<String, String> map) {
        mPresenter.approveCheck(map);
    }

    @Override
    public void approveRequrement(Map<String, String> map) {
        mPresenter.approveRequrement(map);
    }

    private void getParmsMap(String operator,String comment){
        ArrayList<MatnrModule> cammList = new ArrayList<>();
        ArrayList<RequirementMatnrsModel> rmmList = adapter.getArrayList();
        HashMap<Integer,String> radiomap = adapter.getMap();
        for (int i = 0; i < arrayList.size(); i++) {
            MatnrModule cammodule= new MatnrModule();
            if (rmmList.get(i).getRemark()==null){
                cammodule.setRemark("");
            }else{
                cammodule.setRemark(rmmList.get(i).getRemark().trim());
            }
            cammodule.setMatnr(rmmList.get(i).getMatnr());
            cammodule.setStatus(radiomap.get(i));
            cammList.add(cammodule);
        }
        String strjson = CommonUtils.ObjectToJson(cammList);
        map.put("receiptType",WmsApi.billForm.requireForm);
        map.put("receiptNo",requirementDetailModel.getReceiptNo());
        map.put("user",AppUtil.getValue(RequirementsActivity.this,WMS_USER));
        map.put("operation",operation);
        map.put("outcome",operator);
        map.put("comment",comment);
        map.put("matnrs",strjson);
    }


    /**
     * 接收需求单详情成功,success
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETREQUIREMENT_SUCCESS)
            }
    )
    public void searchSuccess(RequirementDataModuleDto response){
        requirementDetailModel = response.getData();
        if (CommonUtils.notEmpty(response.getData().getRequirementMatnrsModelArrayList())){
            for (int i = 0; i < response.getData().getRequirementMatnrsModelArrayList().size(); i++) {
                arrayList.add(response.getData().getRequirementMatnrsModelArrayList().get(i));
            }
        }
        _initView();
        activityRequirementsBinding.invalidateAll();
    }


    /**
     * 接收需求单详情失败，fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETREQUIREMENT_FAIL)
            }
    )
    public void getlistFail(RequirementDataModuleDto response){
        ToastUtil.show(RequirementsActivity.this,response.getUserMsg(),Toast.LENGTH_SHORT);
    }


    /**
     * 校验失败，fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(REQUIREMENTCHECK_FAILED)
            }
    )
    public void checgFailed(HttpResults response){
        CommonUtils.hideProgressDialog(pd);
        if(response !=null && response.getRetMsg()!=null){
            ToastUtils.showToast(response.getRetMsg());
        }
    }

    /**
     * 校验通知，
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(REQUIREMENTCHECK_CONFIRM)
            }
    )
    public void checgConfim(final HttpResults response){
        CommonUtils.hideProgressDialog(pd);
        activityRequirementsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(customDialog == null){
                    customDialog=new CustomDialog(RequirementsActivity.this,R.style.project_back)
                            .setContent(response.getRetMsg()).buttonlistener(new CustomDialog.ButtonListener() {
                                @Override
                                public void cancellistener() {
                                    customDialog.dismiss();
                                }

                                @Override
                                public void makeSurelistener() {
                                    customDialog.dismiss();
                                    showDialog();
                                }
                            });
                }
                customDialog.show();
                }
            },500);
    }



    /**
     * 校验成功
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(REQUIREMENTCHECK_SUCCESS)
            }
    )
    public void getCheckSuccess(HttpResults response){

        CommonUtils.hideProgressDialog(pd);
        activityRequirementsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        },500);
    }

    private void showDialog(){

        final CustomePopupWindow.Builder customePopupWindow = new CustomePopupWindow.Builder(RequirementsActivity.this);
        customePopupWindow.setTitle("审批意见");
        customePopupWindow.seteditTextHint("非必填（最多200字）");
        customePopupWindow.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        customePopupWindow.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String comment = "";
                if (customePopupWindow.getEditText().getText().toString().trim()==null
                        || (customePopupWindow.getEditText().getText().toString().trim().equals(""))
                        ){
                    comment = "";
                }else{
                    comment = customePopupWindow.getEditText().getText().toString().trim();
                }
                map.clear();
                getParmsMap(WmsApi.approveCode.APPROVED,comment);
                approveRequrement(map);
                dialog.dismiss();//提交
                pd = CommonUtils.showProgessDialog(pd,RequirementsActivity.this,"正在提交...");
            }
        });
        CommonUtils.getWidthHeight(customePopupWindow.create(),RequirementsActivity.this).show();
    }




    /**
     * 提交信息成功
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(REQUIREMENTAPPROVE_SUCCESS)
            }
    )
    public void approveSuccess(final ApprovalResponseModule response){

        CommonUtils.hideProgressDialog(pd);
        ToastUtils.showToast(response.getRetMsg());

        activityRequirementsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRxBus.post(WmsApi.removeRequireNo,new RemoveBean(receiptNo));

            }
        },500);
        activityRequirementsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },700);

    }


    /**
     * 提交数据失败，网络连接错误
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                @Tag(REQUIREMENTAPPROVE_FAIL)
            }
    )
    public void approveFail(Object response){
        CommonUtils.hideProgressDialog(pd);
    }


    /**
     * 接收数据失败,fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(REQUIREMENTAPPROVE_NOT_SUCCESS)
            }
    )
    public void approveNotSuccess(final ApprovalResponseModule response){
        activityRequirementsBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.hideProgressDialog(pd);
                ToastUtil.show(RequirementsActivity.this,response.getRetMsg(),Toast.LENGTH_SHORT);
            }
        },500);
    }

    @Override
    public void onBackPressed() {
        CommonUtils.hideProgressDialog(pd);
        super.onBackPressed();
    }
}
