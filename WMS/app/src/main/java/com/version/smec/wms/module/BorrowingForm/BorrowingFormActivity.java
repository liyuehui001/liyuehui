package com.version.smec.wms.module.BorrowingForm;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.smec.wms.R;
import com.smec.wms.BR;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.databinding.ActivityBorrowFormBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.bean.RemoveBean;
import com.version.smec.wms.module.Acceptance.AcceptanceActivity;
import com.version.smec.wms.module.BorrowingForm.adapter.BorrowFormAttachAdapter;
import com.version.smec.wms.module.BorrowingForm.adapter.BorrowFormMatnrAdapter;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormAppendix;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormDto;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormGoodModule;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormModule;
import com.version.smec.wms.module.BorrowingForm.bean.approval.ApprovalBorrowRequestModule;
import com.version.smec.wms.module.BorrowingForm.bean.approval.ApprovalBorrowResponseModule;
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
 * Created by 小黑 on 2017/8/21.
 */
public class BorrowingFormActivity extends WmsBaseActivity<BorrowingFormPresenter> implements BorrowingFormContract{

    private ArrayList<BorrowingFormAppendix> listAppendix;//附件列表数据
    private ArrayList<BorrowingFormGoodModule> listGood;//
    private BorrowingFormModule borrowform;

    public static final String GETBORROWFORM_FAIL="GETBORROWFORM_FAIL";
    public static final String GETBORROWFORM_SUCCESS="GETBORROWFORM_SUCCESS";
    public static final String BORROWFORM_APPROVAL_FAIL="BORROWFORM_APPROVAL_FAIL";
    public static final String BORROWFORM_APPROVAL_SUCCESS="BORROWFORM_APPROVAL_SUCCESS";
    public static final String BORROWFORM_APPROVAL_NOT_SUCCESS = "BORROWFORM_APPROVAL_NOT_SUCCESS";

    public static final String BORROWFORMCHECK_SUCCESS="BORROWFORMCHECK_SUCCESS";
    public static final String BORROWFORMCHECK_FAILED="BORROWFORMCHECK_FAILED";
    public static final String BORROWFORMCHECK_CONFIRM="BORROWFORMCHECK_CONFIRM";

    private ActivityBorrowFormBinding bfactivity;
    private boolean todoTrueBillFalse;
    private String receiptType,receiptNo,operation,userRole,user;
    private String UrgentCode;
    private ProgressDialog progressDialog;
    private  Map<String,String> map=new HashMap<>();

    private BorrowFormMatnrAdapter afmAdapter;
    private BorrowFormAttachAdapter adaAdapter;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         bfactivity = DataBindingUtil.setContentView(this, R.layout.activity_borrow_form);
        _firstInitView();
        _initData();
    }

    private void _firstInitView() {

        userRole = AppUtil.getValue(this,WMS_USER_ROLE);
        user = AppUtil.getValue(BorrowingFormActivity.this,WMS_USER);
        Intent mintent = getIntent();
        todoTrueBillFalse = mintent.getBooleanExtra("todoTrueBillFalse",false);
        receiptType = mintent.getStringExtra("receiptType");
        receiptNo = mintent.getStringExtra("receiptNo");
        operation = mintent.getStringExtra("operation");
        UrgentCode = mintent.getStringExtra("UrgentCode");



        CommonUtils.styleBoldTextView(bfactivity.includeBorrowFormTop.tvJyDetail);
        CommonUtils.styleBoldTextView(bfactivity.matnrTitleInfo);
        CommonUtils.styleBoldTextView(bfactivity.attachTitleInfo);

        bfactivity.includeBorrowFormTop.ivBorrowFormImage.setVisibility(View.GONE);


        if (todoTrueBillFalse){
            bfactivity.cvTodoListDetailTitle.getTvTextTitle().setText("待办详情");
        }else{
            bfactivity.cvTodoListDetailTitle.getTvTextTitle().setText("单据详情");
        }


        _hide_before_request();


        bfactivity.recyclerViewMatrns.setVisibility(View.VISIBLE);
        bfactivity.btnAllSpreadPackupMatrns.setText("收起");


        bfactivity.recyclerViewAttach.setVisibility(View.VISIBLE);
        bfactivity.btnAllSpreadPackupAttach.setText("收起");

        bfactivity.includeBorrowFormTop.llShowHide.setVisibility(View.GONE);
        bfactivity.includeBorrowFormTop.tvSpreadPackup.setText("查看详情");
        bfactivity.includeBorrowFormTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);
        bfactivity.includeBorrowFormTop.llCtrlHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bfactivity.includeBorrowFormTop.llShowHide.getVisibility() == View.GONE){
                    bfactivity.includeBorrowFormTop.llShowHide.setVisibility(View.VISIBLE);
                    bfactivity.includeBorrowFormTop.tvSpreadPackup.setText("收起");
                    bfactivity.includeBorrowFormTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_packup);
                }else{
                    bfactivity.includeBorrowFormTop.llShowHide.setVisibility(View.GONE);
                    bfactivity.includeBorrowFormTop.tvSpreadPackup.setText("查看详情");
                    bfactivity.includeBorrowFormTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);
                }
            }
        });



        bfactivity.cvTodoListDetailTitle.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {
                Intent mintent = new Intent(BorrowingFormActivity.this,LogsActivity.class);
                mintent.putExtra("receiptNo",receiptNo);
                mintent.putExtra("receiptType",WmsApi.billForm.borrowForm);
                startActivity(mintent);
            }
        });


    }

    private void _hide_before_request() {
        bfactivity.rlBottomRefuseAgree.setVisibility(View.GONE);
        bfactivity.svMainContent.setVisibility(View.GONE);
    }

    private void _show_after_request(){
        bfactivity.rlBottomRefuseAgree.setVisibility(View.VISIBLE);
        bfactivity.svMainContent.setVisibility(View.VISIBLE);
        if (todoTrueBillFalse){

            bfactivity.includeBorrowFormTop
                    .llBorrowState.setVisibility(View.GONE);
            if ( userRole.contains(WmsApi.RoleCode.WMS_BRANCH_MGR)){
                if (UrgentCode.equals("Y")){
                    bfactivity.llBottomBtnTwoApproval.setVisibility(View.VISIBLE);
                    bfactivity.llBottomBtnTwo.setVisibility(View.GONE);
                }else{
                    bfactivity.llBottomBtnTwo.setVisibility(View.VISIBLE);
                    bfactivity.llBottomBtnTwoApproval.setVisibility(View.GONE);
                }
            }else if (userRole.contains(WmsApi.RoleCode.MNT_DEPT_APPROVER)){
                bfactivity.llBottomBtnTwo.setVisibility(View.VISIBLE);
                bfactivity.llBottomBtnTwoApproval.setVisibility(View.GONE);
            }else{
                ToastUtil.show(this,"此用户不应该有此权限查看借用单",Toast.LENGTH_LONG);
            }
        }else{
            bfactivity.llBottomBtnTwo.setVisibility(View.GONE);
            bfactivity.llBottomBtnTwoApproval.setVisibility(View.GONE);
            bfactivity.rlBottomRefuseAgree.setVisibility(View.GONE);


            bfactivity.includeBorrowFormTop
                    .llBorrowState.setVisibility(View.VISIBLE);
        }
    }

    private void _initData() {
        listAppendix = new ArrayList<>();
        listGood = new ArrayList<>();
        borrowform = new BorrowingFormModule();
        WeakHashMap<String,String> map = new WeakHashMap<>();

        this.getBorrowingFormData(map);
    }

    @Override
    public void getBorrowingFormData(WeakHashMap<String, String> map) {
        map.put("receiptType",WmsApi.billForm.borrowForm);
        map.put("receiptNo",receiptNo);
        map.put("isOrder",todoTrueBillFalse ? "T" : "F");
        mPresenter.getBorrowingFormData(map);
    }

    @Override
    public void approvalBorrowForm(Map<String, String> map) {
        mPresenter.approvalBorrowForm(map);
    }

    @Override
    public void approveCheck(Map<String, String> map) {
        mPresenter.approveCheck(map);
    }

    @Override
    public BorrowingFormPresenter getPresenter() {
        return new BorrowingFormPresenter(this);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETBORROWFORM_SUCCESS)
            }
    )
    public void getSuccess(BorrowingFormDto response){
        borrowform = response.getData();

        if ( CommonUtils.notEmpty(borrowform.getMatnrs()) ){
            for (int i = 0; i < borrowform.getMatnrs().size(); i++) {
                listGood.add(borrowform.getMatnrs().get(i));
            }

        }
        if (CommonUtils.notEmpty(borrowform.getAttachs())){

            for (int i = 0; i < borrowform.getAttachs().size(); i++) {
                listAppendix.add(borrowform.getAttachs().get(i));
            }
        }

        _initView();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETBORROWFORM_FAIL)
            }
    )
    public void getBorrowFormFail(Object object){
        CommonUtils.hideProgressDialog(progressDialog);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BORROWFORM_APPROVAL_SUCCESS)
            }
    )
    public void getApprovalSuccess(final ApprovalBorrowResponseModule response){
        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtils.showToast(response.getRetMsg());
        bfactivity.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRxBus.post(WmsApi.removeReceiptNo,new RemoveBean(receiptNo));
            }
        },500);
        bfactivity.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                BorrowingFormActivity.this.finish();
            }
        },700);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BORROWFORM_APPROVAL_FAIL)
            }
    )
    public void getapprovalFail(Object object){
        CommonUtils.hideProgressDialog(progressDialog);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BORROWFORM_APPROVAL_NOT_SUCCESS)
            }
    )
    public void getapprovalNotSuccess(ApprovalBorrowResponseModule response){
        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtil.show(BorrowingFormActivity.this,response.getRetMsg(),Toast.LENGTH_SHORT);
    }


    private void _initView() {

        _show_after_request();

        bfactivity.setVariable(BR.borrowing_form_info,borrowform);
        bfactivity.includeBorrowFormTop.ivBorrowFormImage.setVisibility(View.VISIBLE);
        final FullyLinearLayoutManager managerMatnrs = new FullyLinearLayoutManager(this);
        afmAdapter = new BorrowFormMatnrAdapter(this,listGood,todoTrueBillFalse);
        adaAdapter = new BorrowFormAttachAdapter(this,listAppendix,todoTrueBillFalse);

        bfactivity.recyclerViewMatrns.setLayoutManager(managerMatnrs);
        bfactivity.recyclerViewMatrns.setNestedScrollingEnabled(false);
        bfactivity.recyclerViewMatrns.setAdapter(afmAdapter);

        bfactivity.recyclerViewAttach.setLayoutManager(new LinearLayoutManager(this));
        bfactivity.recyclerViewAttach.setNestedScrollingEnabled(false);
        bfactivity.recyclerViewAttach.setAdapter(adaAdapter);



        final boolean[] isfalgMatnrs = {true};
        bfactivity.btnAllSpreadPackupMatrns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isfalgMatnrs[0]){
                    bfactivity.recyclerViewMatrns.setVisibility(View.GONE);
                    bfactivity.btnAllSpreadPackupMatrns.setText("展开");
                    isfalgMatnrs[0] = false;
                }else{
                    bfactivity.recyclerViewMatrns.setVisibility(View.VISIBLE);
                    bfactivity.btnAllSpreadPackupMatrns.setText("收起");
                    isfalgMatnrs[0] = true;
                }

            }
        });

        final boolean[] isflagAttach = {true};
        bfactivity.btnAllSpreadPackupAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isflagAttach[0]){
                    bfactivity.btnAllSpreadPackupAttach.setText("展开");
                    bfactivity.recyclerViewAttach.setVisibility(View.GONE);
                    isflagAttach[0] = false;
                }else{
                    bfactivity.btnAllSpreadPackupAttach.setText("收起");
                    bfactivity.recyclerViewAttach.setVisibility(View.VISIBLE);
                    isflagAttach[0] = true;
                }
            }
        });

        _judgeHavingData();

        bfactivity.includeBorrowFormTop.llCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = borrowform.getContactPhone();
                if (!TextUtils.isEmpty(phone)){
                    CommonUtils.callPhone(phone,BorrowingFormActivity.this);
                }else{
                    ToastUtil.show(BorrowingFormActivity.this,WmsApi.tip.noRecordPhone,Toast.LENGTH_SHORT);
                }
            }
        });



        bfactivity.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _submitApproval("","");
                progressDialog = CommonUtils.showProgessDialog(progressDialog,BorrowingFormActivity.this,"正在校验...");
                approveCheck(map);
            }
        });

        bfactivity.tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(BorrowingFormActivity.this);
                builder.setTitle("驳回意见");
                builder.seteditTextHint("必填（最多200字）");
                builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String comment = "";

                        if (builder.getEditText().getText()!=null &&
                                !builder.getEditText().getText().toString().trim().equals("") ){
                            comment = builder.getEditText().getText().toString().trim();

                            dialog.dismiss();
                            _submitApproval(comment,WmsApi.approveCode.REJECTED);
                            progressDialog = CommonUtils.showProgessDialog(progressDialog,BorrowingFormActivity.this,"正在提交...");
                            approvalBorrowForm(map);

                        }else{
                            ToastUtil.show(BorrowingFormActivity.this,"驳回意见为必填项，不能为空",Toast.LENGTH_LONG);
                        }


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                CommonUtils.getWidthHeight(builder.create(),BorrowingFormActivity.this).show();
            }
        });


        bfactivity.tvAgreeApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _submitApproval("","");
                progressDialog = CommonUtils.showProgessDialog(progressDialog,BorrowingFormActivity.this,"正在校验...");
                approveCheck(map);
            }
        });

        bfactivity.tvRefuseApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(BorrowingFormActivity.this);
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
                        if (builder.getEditText()!=null &&
                                !builder.getEditText().getText().toString().trim().equals("")){
                            comment = builder.getEditText().getText().toString().trim();

                            dialog.dismiss();
                            _submitApproval(comment,WmsApi.approveCode.REJECTED);
                            progressDialog = CommonUtils.showProgessDialog(progressDialog,BorrowingFormActivity.this,"正在提交...");
                            approvalBorrowForm(map);

                        }else{
                            ToastUtil.show(BorrowingFormActivity.this,"驳回理由为必填项，不能为空",Toast.LENGTH_LONG);
                        }
                    }
                });
                CommonUtils.getWidthHeight(builder.create(),BorrowingFormActivity.this).show();
            }
        });
    }

    private void _judgeHavingData() {

        if (!CommonUtils.notEmpty(listAppendix)) {
            bfactivity.btnAllSpreadPackupAttach.setText(WmsApi.tip.NowNotAvailable);
            bfactivity.btnAllSpreadPackupAttach.setClickable(false);
            bfactivity.btnAllSpreadPackupAttach.setBackgroundResource(R.drawable.bg_corner_gray);
        }
        if (!CommonUtils.notEmpty(listGood)) {
            bfactivity.btnAllSpreadPackupAttach.setText(WmsApi.tip.NowNotAvailable);
            bfactivity.btnAllSpreadPackupAttach.setClickable(false);
            bfactivity.btnAllSpreadPackupAttach.setBackgroundResource(R.drawable.bg_corner_gray);
        }
    }
    /**
     * 校验失败，fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BORROWFORMCHECK_FAILED)
            }
    )
    public void checgFailed(HttpResults response){
        CommonUtils.hideProgressDialog(progressDialog);
        if(response !=null && response.getRetMsg()!=null){
            ToastUtils.showToast(response.getRetMsg());
        }
    }

    /**
     * 校验通知，fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BORROWFORMCHECK_CONFIRM)
            }
    )
    public void checgConfim(final HttpResults response){
        CommonUtils.hideProgressDialog(progressDialog);
        bfactivity.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(customDialog == null){
                    customDialog=new CustomDialog(BorrowingFormActivity.this,R.style.project_back)
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
                    @Tag(BORROWFORMCHECK_SUCCESS)
            }
    )
    public void getCheckSuccess(HttpResults response){
        CommonUtils.hideProgressDialog(progressDialog);
        bfactivity.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        },500);
    }

    public void showDialog(){
        final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(BorrowingFormActivity.this);
        builder.setTitle("审批意见");
        builder.seteditTextHint("非必填（最多200字）");
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //判断用户身份?总经理还是维保站
                //不用判断身份，因为operation是由上一级传来的，

                String comment = "";
                if (builder.getEditText().getText().toString()!=null){
                    comment = builder.getEditText().getText().toString().trim();
                }
                dialog.dismiss();
                _submitApproval(comment,WmsApi.approveCode.APPROVED);
                progressDialog = CommonUtils.showProgessDialog(progressDialog,BorrowingFormActivity.this,"正在提交...");
                approvalBorrowForm(map);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        CommonUtils.getWidthHeight(builder.create(),BorrowingFormActivity.this).show();
    }

    private void  _submitApproval(String comment,String outCome){
        ApprovalBorrowRequestModule abrModule = new ApprovalBorrowRequestModule();
        abrModule.setOutcome(outCome);
        abrModule.setReceiptNo(borrowform.getReceiptNo());
        abrModule.setOperation(operation);
        abrModule.setComment(comment);
        abrModule.setUser(user);
        map = CommonUtils.parseData(CommonUtils.ObjectToJson(abrModule));
        map.put("receiptType",WmsApi.billForm.borrowForm);
    }
}
