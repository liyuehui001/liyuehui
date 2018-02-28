package com.version.smec.wms.module.Acceptance;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.flyco.dialog.listener.OnBtnLeftClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.smec.wms.R;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.databinding.ActivityPendingAcceptanceBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.api.WmsConstant;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.bean.RemoveBean;
import com.version.smec.wms.module.Acceptance.adapter.AcceptanceAdapter;
import com.version.smec.wms.module.Acceptance.adapter.EnclosureAdapter;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceEnclosureModel;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceMatnrsModel;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceModule;
import com.version.smec.wms.module.Acceptance.bean.approval.AcceptanceRequestApprovalModule;
import com.version.smec.wms.module.Acceptance.bean.approval.AcceptanceResponseApprovalModule;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceModelDto;
import com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity;
import com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentInside;
import com.version.smec.wms.module.Requirements.RequirementsActivity;
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

public class AcceptanceActivity extends WmsBaseActivity<AcceptancePresenter> implements AcceptanceContract {

    private ActivityPendingAcceptanceBinding activityPendingAcceptanceBinding;
    public static final String Accepttance_SUCCESS="SEARCH_SUCCESS";
    public static final String Accepttance_FAIL="Accepttance_FAIL";
    public static final String ACCEPTANCE_APPROVAL_SUCCESS="ACCEPTANCE_APPROVAL_SUCCESS";
    public static final String ACCEPTANCE_APPROVAL_NOT_SUCCESS="ACCEPTANCE_APPROVAL_NOT_SUCCESS";
    public static final String ACCEPTANCE_APPROVAL_FAIL="ACCEPTANCE_APPROVAL_FAIL";

    public static final String ACCEPTANCECHECK_SUCCESS="ACCEPTANCECHECK_SUCCESS";
    public static final String ACCEPTANCECHECK_FAILED="ACCEPTANCECHECK_FAILED";
    public static final String ACCEPTANCECHECK_CONFIRM="ACCEPTANCECHECK_CONFIRM";

    private AcceptanceAdapter acceptanceAdapter;
    private EnclosureAdapter enclosureAdapter;
    private AcceptanceModule acceptanceModule=new AcceptanceModule();
    private ArrayList<AcceptanceMatnrsModel> arrayList=new ArrayList<>();
    private ArrayList<AcceptanceEnclosureModel> enclosureArrayList=new ArrayList<>();
    private String urgentCode="";
    private String receiptType,receiptNo;
    private boolean todo_true_order_false = false;
    private String operation,userRole,user;
    private ProgressDialog progressDialog;
    private CustomDialog customDialog;
    private Map<String,String> map=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPendingAcceptanceBinding = DataBindingUtil
                .setContentView(this,R.layout.activity_pending_acceptance);
        setTitle();
    }

    @Override
    public AcceptancePresenter getPresenter() {
        return new AcceptancePresenter(this);
    }

    public void setTitle(){
        Intent intent=getIntent();
        todo_true_order_false = intent.getBooleanExtra("todoTrueBillFalse",false);
        receiptType = intent.getStringExtra("receiptType");
        receiptNo = intent.getStringExtra("receiptNo");
        urgentCode=intent.getStringExtra("UrgentCode");
        operation = intent.getStringExtra("operation");
        userRole = AppUtil.getValue(this,WMS_USER_ROLE);
        user = AppUtil.getValue(this,WMS_USER);


        CommonUtils.styleBoldTextView(activityPendingAcceptanceBinding.acceptanceInclude.lyTvTitle);
        CommonUtils.styleBoldTextView(activityPendingAcceptanceBinding.matnrTitleInfo);
        CommonUtils.styleBoldTextView(activityPendingAcceptanceBinding.attachTitleInfo);

        activityPendingAcceptanceBinding.acceptanceInclude.llHideShow.setVisibility(View.GONE);
        activityPendingAcceptanceBinding.acceptanceInclude.tvSpreadPackup.setText("查看详情");
        activityPendingAcceptanceBinding.acceptanceInclude.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);

        activityPendingAcceptanceBinding.acceptanceInclude.llCtrlHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityPendingAcceptanceBinding.acceptanceInclude.llHideShow.getVisibility() == View.GONE){
                    activityPendingAcceptanceBinding.acceptanceInclude.llHideShow.setVisibility(View.VISIBLE);
                    activityPendingAcceptanceBinding.acceptanceInclude.tvSpreadPackup.setText("收起");
                    activityPendingAcceptanceBinding.acceptanceInclude.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_packup);

                }else{
                    activityPendingAcceptanceBinding.acceptanceInclude.llHideShow.setVisibility(View.GONE);
                    activityPendingAcceptanceBinding.acceptanceInclude.tvSpreadPackup.setText("查看详情");
                    activityPendingAcceptanceBinding.acceptanceInclude.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);

                }
            }
        });


        if (todo_true_order_false){
            activityPendingAcceptanceBinding.cvTodoListDetailTitle.getTvTextTitle().setText("待办详情");
        }else{
            activityPendingAcceptanceBinding.cvTodoListDetailTitle.getTvTextTitle().setText("单据详情");
        }
        _hide_defore_request();

        activityPendingAcceptanceBinding.cvTodoListDetailTitle.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                AcceptanceActivity.this.finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {
                Intent mintent = new Intent(AcceptanceActivity.this,LogsActivity.class);
                mintent.putExtra("receiptNo",receiptNo);
                mintent.putExtra("receiptType",WmsApi.billForm.acceptForm);
                startActivity(mintent);
            }
        });

        search(new WeakHashMap<String, String>());

    }

    private void _hide_defore_request(){
        activityPendingAcceptanceBinding.rlBottomRefuseAgree.setVisibility(View.GONE);
        activityPendingAcceptanceBinding.scro.setVisibility(View.GONE);
    }

    private void _show_after_request(){

        activityPendingAcceptanceBinding.rlBottomRefuseAgree.setVisibility(View.VISIBLE);
        activityPendingAcceptanceBinding.scro.setVisibility(View.VISIBLE);
        if (todo_true_order_false){
            if (!TextUtils.isEmpty(urgentCode)){
                if ( userRole.contains(WmsApi.RoleCode.WMS_BRANCH_MGR)){
                    if (urgentCode.equals("Y")){

                        activityPendingAcceptanceBinding.llBottomBackSubmitApproval.setVisibility(View.VISIBLE);
                        activityPendingAcceptanceBinding.llBottomBackSubmit.setVisibility(View.GONE);
                    }else{

                        activityPendingAcceptanceBinding.llBottomBackSubmitApproval.setVisibility(View.GONE);
                        activityPendingAcceptanceBinding.llBottomBackSubmit.setVisibility(View.VISIBLE);
                    }
                }else if (userRole.contains(WmsApi.RoleCode.MNT_DEPT_APPROVER)){

                    activityPendingAcceptanceBinding.llBottomBackSubmitApproval.setVisibility(View.GONE);
                    activityPendingAcceptanceBinding.llBottomBackSubmit.setVisibility(View.VISIBLE);
                }else{
                    ToastUtil.show(this,"此用户不应该有此权限查看借用单",Toast.LENGTH_LONG);
                    activityPendingAcceptanceBinding.llBottomBackSubmitApproval.setVisibility(View.GONE);
                    activityPendingAcceptanceBinding.llBottomBackSubmit.setVisibility(View.GONE);
                }
            }
        }else{
            activityPendingAcceptanceBinding.llBottomBackSubmitApproval.setVisibility(View.GONE);
            activityPendingAcceptanceBinding.llBottomBackSubmit.setVisibility(View.GONE);
            activityPendingAcceptanceBinding.rlBottomRefuseAgree.setVisibility(View.GONE);
        }
    }


    private void _initView(){
        _show_after_request();

        if(!TextUtils.isEmpty(urgentCode)){
            activityPendingAcceptanceBinding.acceptanceInclude.imgAgent.setVisibility(View.VISIBLE);
            if(urgentCode.equals("Y")){
                activityPendingAcceptanceBinding.acceptanceInclude.imgAgent.setImageResource(R.drawable.wms_requirement_agent);
            }else {
                activityPendingAcceptanceBinding.acceptanceInclude.imgAgent.setImageResource(R.drawable.wms_requirement_unagent);
            }
        }else {
            activityPendingAcceptanceBinding.acceptanceInclude.imgAgent.setVisibility(View.GONE);
        }


        //物料信息
        acceptanceAdapter = new AcceptanceAdapter(AcceptanceActivity.this,todo_true_order_false);
        acceptanceAdapter.setList(arrayList);
        activityPendingAcceptanceBinding.acceptanceRecyclerView.setLayoutManager(new FullyLinearLayoutManager(AcceptanceActivity.this));
        activityPendingAcceptanceBinding.acceptanceRecyclerView.setNestedScrollingEnabled(false);
        activityPendingAcceptanceBinding.acceptanceRecyclerView.setAdapter(acceptanceAdapter);

        //附件信息
        enclosureAdapter=new EnclosureAdapter(AcceptanceActivity.this,todo_true_order_false);
        enclosureAdapter.setList(enclosureArrayList);
        activityPendingAcceptanceBinding.enclosureRecyclerView.setLayoutManager(new FullyLinearLayoutManager(AcceptanceActivity.this));
        activityPendingAcceptanceBinding.enclosureRecyclerView.setNestedScrollingEnabled(false);
        activityPendingAcceptanceBinding.enclosureRecyclerView.setAdapter(enclosureAdapter);


        activityPendingAcceptanceBinding.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _submitApproval("","");
                progressDialog = CommonUtils.showProgessDialog(progressDialog,AcceptanceActivity.this,"正在校验...");
                approveCheck(map);
            }
        });

        activityPendingAcceptanceBinding.tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(AcceptanceActivity.this);
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

                        String comment = "";

                        if (builder.getEditText().getText()!=null &&
                                !builder.getEditText().getText().toString().trim().equals("") ){
                            comment = builder.getEditText().getText().toString().trim();

                            dialog.dismiss();//提交
                            progressDialog = CommonUtils.showProgessDialog(progressDialog,AcceptanceActivity.this,"正在提交...");
                            _submitApproval(comment,WmsApi.approveCode.REJECTED);
                            approvalAcceptance(map);
                        }else{
                            ToastUtil.show(AcceptanceActivity.this,"驳回意见为必填项，不能为空",Toast.LENGTH_LONG);
                        }

                    }
                });

                CommonUtils.getWidthHeight(builder.create(),AcceptanceActivity.this).show();
            }
        });



        activityPendingAcceptanceBinding.tvAgreeApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _submitApproval("","");
                progressDialog = CommonUtils.showProgessDialog(progressDialog,AcceptanceActivity.this,"正在校验...");
                approveCheck(map);
            }
        });

        activityPendingAcceptanceBinding.acceptanceInclude.llCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = acceptanceModule.getContactPhone();
                if (!TextUtils.isEmpty(phone)){
                    CommonUtils.callPhone(phone,AcceptanceActivity.this);
                }else{
                    ToastUtil.show(AcceptanceActivity.this,WmsApi.tip.noRecordPhone,Toast.LENGTH_SHORT);
                }
            }
        });


        activityPendingAcceptanceBinding.tvRefuseApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(AcceptanceActivity.this);
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
                        String comment = "";

                        if (builder.getEditText().getText()!=null &&
                                !builder.getEditText().getText().toString().trim().equals("") ){
                            comment = builder.getEditText().getText().toString().trim();

                            dialog.dismiss();//提交
                            _submitApproval(comment,WmsApi.approveCode.REJECTED);
                            approvalAcceptance(map);
                            progressDialog = CommonUtils.showProgessDialog(progressDialog,AcceptanceActivity.this,"正在提交...");

                        }else{
                            ToastUtil.show(AcceptanceActivity.this,"驳回意见为必填项，不能为空",Toast.LENGTH_LONG);
                        }
                    }
                });
                CommonUtils.getWidthHeight(builder.create(),AcceptanceActivity.this).show();
            }
        });

        if(!todo_true_order_false){
            activityPendingAcceptanceBinding.acceptanceInclude.llStatusCode.setVisibility(View.VISIBLE);
        }

        activityPendingAcceptanceBinding.btnAllSpreadPackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityPendingAcceptanceBinding.btnAllSpreadPackup.getText().equals("收起")){
                    activityPendingAcceptanceBinding.btnAllSpreadPackup.setText("展开");
                    activityPendingAcceptanceBinding.acceptanceRecyclerView.setVisibility(View.GONE);
                }else {
                    activityPendingAcceptanceBinding.btnAllSpreadPackup.setText("收起");
                    activityPendingAcceptanceBinding.acceptanceRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        activityPendingAcceptanceBinding.btnEnclosureShouqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityPendingAcceptanceBinding.btnEnclosureShouqi.getText().equals("收起")){
                    activityPendingAcceptanceBinding.btnEnclosureShouqi.setText("展开");
                    activityPendingAcceptanceBinding.enclosureRecyclerView.setVisibility(View.GONE);
                }else {
                    activityPendingAcceptanceBinding.btnEnclosureShouqi.setText("收起");
                    activityPendingAcceptanceBinding.enclosureRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        _judgeHavingData();

    }

    private void _judgeHavingData() {

        if ( !CommonUtils.notEmpty(enclosureArrayList) ){//附件
            activityPendingAcceptanceBinding.btnEnclosureShouqi.setText(WmsApi.tip.NowNotAvailable);
            activityPendingAcceptanceBinding.btnEnclosureShouqi.setClickable(false);
            activityPendingAcceptanceBinding.btnEnclosureShouqi.setBackgroundResource(R.drawable.bg_corner_gray);
        }
        if (!CommonUtils.notEmpty(arrayList)){//物料
            activityPendingAcceptanceBinding.btnAllSpreadPackup.setText(WmsApi.tip.NowNotAvailable);
            activityPendingAcceptanceBinding.btnAllSpreadPackup.setClickable(false);
            activityPendingAcceptanceBinding.btnAllSpreadPackup.setBackgroundResource(R.drawable.bg_corner_gray);
        }
    }

    public void showDialog(){

        final CustomePopupWindow.Builder customePopupWindow = new CustomePopupWindow.Builder(AcceptanceActivity.this);
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

                String comment="";
                if (customePopupWindow.getEditText()!=null &&
                        !customePopupWindow.getEditText().getText().toString().trim().equals("")){
                    comment = customePopupWindow.getEditText().getText().toString().trim();
                }
                dialog.dismiss();//提交
                _submitApproval(comment,WmsApi.approveCode.APPROVED);
                progressDialog = CommonUtils.showProgessDialog(progressDialog,AcceptanceActivity.this,"正在提交...");
                approvalAcceptance(map);
            }
        });
        CommonUtils.getWidthHeight(customePopupWindow.create(),AcceptanceActivity.this).show();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AcceptanceActivity.Accepttance_SUCCESS)
            }
    )
    public void searchSuccess(final AcceptanceModelDto response){
        acceptanceModule=response.getData();
        arrayList=acceptanceModule.getMatnrs();
        enclosureArrayList=acceptanceModule.getAttachs();
        _initView();
        activityPendingAcceptanceBinding.setAcceptance(acceptanceModule);
        acceptanceAdapter.setList(response.getData().getMatnrs());
        enclosureAdapter.setList(response.getData().getAttachs());
        setTextview();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AcceptanceActivity.Accepttance_FAIL)
            }
    )
    public void searchFail(AcceptanceModelDto response){
        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtils.showToast(response.getUserMsg());
    }

    public void setTextview(){

        if(!TextUtils.isEmpty(acceptanceModule.getReceiveType())) {
            if (acceptanceModule.getReceiveType().equals("内部订单领用")) {
                activityPendingAcceptanceBinding.acceptanceInclude.tvReceiptNumber.setText("内部订单号:");
                activityPendingAcceptanceBinding.acceptanceInclude.tvReceiptName.setText("内部订单名称:");
            } else if (acceptanceModule.getReceiveType().equals("出口备件")) {
                activityPendingAcceptanceBinding.acceptanceInclude.llChangeNumber.setVisibility(View.GONE);
                activityPendingAcceptanceBinding.acceptanceInclude.llChangeName.setVisibility(View.GONE);
            } else if (acceptanceModule.getReceiveType().equals("成本中心领用")) {
                activityPendingAcceptanceBinding.acceptanceInclude.tvReceiptNumber.setText("成本中心号:");
                activityPendingAcceptanceBinding.acceptanceInclude.tvReceiptName.setText("成本中心名称:");
            }
        }
    }



    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AcceptanceActivity.ACCEPTANCE_APPROVAL_SUCCESS)
            }
    )
    public void getapprovalSuccess(final AcceptanceResponseApprovalModule response){

        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtils.showToast(response.getRetMsg());
        activityPendingAcceptanceBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRxBus.post(WmsApi.removeReceiptNo,new RemoveBean(receiptNo));
            }
        },500);

        activityPendingAcceptanceBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },700);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AcceptanceActivity.ACCEPTANCE_APPROVAL_FAIL)
            }
    )
    public void getapprovalFail(Object object){
        CommonUtils.hideProgressDialog(progressDialog);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AcceptanceActivity.ACCEPTANCE_APPROVAL_FAIL)
            }
    )
    public void getapprovalNotSuccess(AcceptanceResponseApprovalModule response){
        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtil.show(AcceptanceActivity.this,response.getRetMsg(),Toast.LENGTH_SHORT);
    }


    /**
     * 校验失败，fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(ACCEPTANCECHECK_FAILED)
            }
    )
    public void checgFailed(HttpResults response){
        CommonUtils.hideProgressDialog(progressDialog);
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
                    @Tag(ACCEPTANCECHECK_CONFIRM)
            }
    )
    public void checgConfim(final HttpResults response){
        CommonUtils.hideProgressDialog(progressDialog);
        activityPendingAcceptanceBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(customDialog == null){
                    customDialog=new CustomDialog(AcceptanceActivity.this,R.style.project_back)
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
                    @Tag(ACCEPTANCECHECK_SUCCESS)
            }
    )
    public void getCheckSuccess(HttpResults response){

        CommonUtils.hideProgressDialog(progressDialog);
        activityPendingAcceptanceBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        },500);
    }

    @Override
    public void approvalAcceptance(Map<String, String> map) {
        mPresenter.approvalAcceptance(map);
    }

    @Override
    public void search(WeakHashMap<String, String> map) {
        CommonUtils.hideImmManager(activityPendingAcceptanceBinding.getRoot());
        map.put("receiptType",WmsApi.billForm.acceptForm);//查询模块,总部订单代办列表
        map.put("receiptNo",receiptNo);//查询模式
        map.put("isOrder",todo_true_order_false ? "T" : "F");
        mPresenter.search(map);
    }

    @Override
    public void approveCheck(Map<String, String> map) {
        mPresenter.approveCheck(map);
    }

    private void  _submitApproval(String comment,String isApproval){
        AcceptanceRequestApprovalModule aramodule = new AcceptanceRequestApprovalModule();
        aramodule.setUser(user);
        aramodule.setComment(comment);
        aramodule.setOperation(operation);
        aramodule.setOutcome(isApproval);
        aramodule.setReceiptNo(acceptanceModule.getReceiptNo());
        map = CommonUtils.parseData(CommonUtils.ObjectToJson(aramodule));
        map.put("receiptType",WmsApi.billForm.acceptForm);//查询模块,总部订单代办列表
    }
}
