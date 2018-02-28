package com.version.smec.wms.module.SalesSlipment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.smec.wms.databinding.ActivitySalesSlipBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBaseActivity;

import com.version.smec.wms.bean.HttpResults;
import com.version.smec.wms.bean.RemoveBean;
import com.version.smec.wms.module.SalesSlipment.adapter.SalesSlipmentAdapter;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipGoodsModule;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipModule;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipModuleDto;
import com.version.smec.wms.module.SalesSlipment.bean.approval.ApprovalRequestSaleModule;
import com.version.smec.wms.module.SalesSlipment.bean.approval.ApprovalResponseSaleModule;
import com.version.smec.wms.module.logs.LogsActivity;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.FullyLinearLayoutManager;
import com.version.smec.wms.utils.ToastUtils;
import com.version.smec.wms.widget.CustomDialog;
import com.version.smec.wms.widget.CustomePopupWindow;
import com.version.smec.wms.widget.TopBarLayout;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/18.
 */
public class SalesSlipmentActivity extends WmsBaseActivity<SalesSlipmentPresenter> implements SalesSlipmentContract {

    public static final String GETSALESLIP_FAIL="SEARCH_FAIL";
    public static final String GETSALESLIP_SUCCESS="SEARCH_SUCCESS";
    public static final String SALES_APPROVE_FAIL="SALES_APPROVE_FAIL";
    public static final String SALES_APPROVE_SUCCESS="SALES_APPROVE_SUCCESS";
    public static final String SALES_APPROVE_NOT_SUCCESS = "SALES_APPROVE_NOT_SUCCESS";


    public static final String SALESCHECK_SUCCESS="SALESCHECK_SUCCESS";
    public static final String SALESCHECK_FAILED="SALESCHECK_FAILED";
    public static final String SALESCHECK_CONFIRM="SALESCHECK_CONFIRM";

    private ActivitySalesSlipBinding assbind;
    private ArrayList<SalesSlipGoodsModule> ssgmLists;
    private SalesSlipModule ssModule;
    private boolean todoTrueBillFalse;
    private final boolean[] flag_visible = {true};
    private String receiptNo,receiptType;
    private String operation;
    private ProgressDialog progressDialog;
    private  Map<String,String> map;
    private CustomDialog customDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assbind =  DataBindingUtil.setContentView(this, R.layout.activity_sales_slip);
        _firstinitView();
        _initData();
    }

    private void _judgeHavingData() {
        if (!CommonUtils.notEmpty(ssgmLists)){//物料
            assbind.btnAllSpreadPackUpSalesSlip.setText(WmsApi.tip.NowNotAvailable);
            assbind.btnAllSpreadPackUpSalesSlip.setClickable(false);
            assbind.btnAllSpreadPackUpSalesSlip.setBackgroundResource(R.drawable.bg_corner_gray);
        }

    }

    private void _firstinitView() {

        Intent mintent = getIntent();
        todoTrueBillFalse = mintent.getBooleanExtra("todoTrueBillFalse",false);
        receiptNo = mintent.getStringExtra("receiptNo");
        receiptType = mintent.getStringExtra("receiptType");
        operation = mintent.getStringExtra("operation");


        CommonUtils.styleBoldTextView(assbind.includeSalesSlipTop.tvSaleInfo);
        CommonUtils.styleBoldTextView(assbind.saleMatnrTitleInfo);

        if (todoTrueBillFalse){
            assbind.tblCustomeTopToolbar.getTvTextTitle().setText("待办详情");


        }else{
            assbind.tblCustomeTopToolbar
                    .getTvTextTitle().setText("单据详情");

        }
        _hide_defore_request();

        assbind.rcvSalesGoods.setVisibility(View.VISIBLE);
        assbind.btnAllSpreadPackUpSalesSlip.setText("收起");

        assbind.tblCustomeTopToolbar.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                SalesSlipmentActivity.this.finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {
                Intent mintent = new Intent(SalesSlipmentActivity.this,LogsActivity.class);
                mintent.putExtra("receiptNo",receiptNo);
                mintent.putExtra("receiptType",WmsApi.billForm.saleForm);
                startActivity(mintent);
            }
        });

        assbind.includeSalesSlipTop.llHideShow.setVisibility(View.GONE);
        assbind.includeSalesSlipTop.tvSpreadPackup.setText("查看详情");
        assbind.includeSalesSlipTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);
        assbind.includeSalesSlipTop.llCtrlHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assbind.includeSalesSlipTop.llHideShow.getVisibility() == View.GONE){
                    assbind.includeSalesSlipTop.llHideShow.setVisibility(View.VISIBLE);
                    assbind.includeSalesSlipTop.tvSpreadPackup.setText("收起");
                    assbind.includeSalesSlipTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_packup);

                }else{
                    assbind.includeSalesSlipTop.llHideShow.setVisibility(View.GONE);
                    assbind.includeSalesSlipTop.tvSpreadPackup.setText("查看详情");
                    assbind.includeSalesSlipTop.ivSpreadPackup.setBackgroundResource(R.mipmap.wms_requirement_spread);

                }
            }
        });



    }

    private void _hide_defore_request() {
        assbind.svMainContent.setVisibility(View.GONE);
        assbind.RlBottomBtnTwo.setVisibility(View.GONE);
    }


    private void _show_after_request(){
        assbind.svMainContent.setVisibility(View.VISIBLE);
        if (todoTrueBillFalse){
            assbind.RlBottomBtnTwo.setVisibility(View.VISIBLE);
            assbind.includeSalesSlipTop
                    .llSaleState.setVisibility(View.GONE);
        }else{
            assbind.RlBottomBtnTwo.setVisibility(View.GONE);
            assbind.includeSalesSlipTop
                    .llSaleState.setVisibility(View.VISIBLE);
        }
    }

    private void _initData() {
        ssgmLists = new ArrayList<>();
        ssModule = new SalesSlipModule();
        WeakHashMap<String, String> params = new WeakHashMap<>();
        this.getSaleSlip(params);
    }


    @Override
    public void getSaleSlip(WeakHashMap<String, String> map) {
        map.put("receiptType", WmsApi.billForm.saleForm);
        map.put("receiptNo",receiptNo);
        map.put("isOrder",todoTrueBillFalse ? "T" : "F");
        mPresenter.getSaleSlip(map);

    }

    @Override
    public void refuseAgree(Map<String, String> map) {
        mPresenter.refuseAgree(map);
    }

    @Override
    public void approveCheck(Map<String, String> map) {
        mPresenter.approveCheck(map);
    }

    @Override
    public SalesSlipmentPresenter getPresenter() {
        return new SalesSlipmentPresenter(this);
    }


    /**
     * 接收数据成功success
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETSALESLIP_SUCCESS)
            }
    )
    public void getSuccess(SalesSlipModuleDto response){
        ssModule = response.getData();
        if (CommonUtils.notEmpty(ssModule.getMatnrs())){
            for (int i = 0; i < ssModule.getMatnrs().size(); i++) {
                ssgmLists.add(ssModule.getMatnrs().get(i));
            }
        }
        _initView();
        assbind.invalidateAll();

    }

    /**
     * 接收数据失败fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETSALESLIP_FAIL)
            }
    )
    public  void getApproalfail(SalesSlipModuleDto response) {
        ToastUtils.showToast(response.getUserMsg());
    }

    /**
     * 提交数据成功，success
     * @param response
     */
    @Subscribe(
            thread= EventThread.MAIN_THREAD,
            tags = {
                    @Tag(SALES_APPROVE_SUCCESS)
            }
    )
    public void getapprovalSuccess(final ApprovalResponseSaleModule response){
        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtils.showToast(response.getRetMsg());
        //生成日志信息成功，推出这个界面，并且刷新需求列表
        assbind.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRxBus.post(WmsApi.removeReceiptNo,new RemoveBean(receiptNo));
            }
        },500);
        assbind.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },700);

    }


    /**
     * 接收数据失败fail
     * @param response
     */
    @Subscribe(
            thread= EventThread.MAIN_THREAD,
            tags = {
                    @Tag(SALES_APPROVE_NOT_SUCCESS)
            }
    )
    public void getApprovalNotSuccess(ApprovalResponseSaleModule response){
        CommonUtils.hideProgressDialog(progressDialog);
        ToastUtil.show(SalesSlipmentActivity.this,response.getRetMsg(),Toast.LENGTH_SHORT);
    }


    /**
     * 网络连接错误，关闭提示框
     * @param object
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
            @Tag(SALES_APPROVE_FAIL)
        }
    )
    public void getFail(Object object){
        CommonUtils.hideProgressDialog(progressDialog);
    }

    private void _initView() {
        _show_after_request();

        if (CommonUtils.notEmptyStr(ssModule.getSalesType()) ){
            if (!ssModule.getSalesType().equals(WmsApi.saleType.SALESTYPE_tripartite_agreement1)
                    && !ssModule.getSalesType().equals(WmsApi.saleType.getSALESTYPE_tripartite_agreement2)){
                assbind.includeSalesSlipTop.llUnitCode.setVisibility(View.GONE);
            }
        }


        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
        assbind.rcvSalesGoods.setLayoutManager(mLayoutManager);
        assbind.rcvSalesGoods.setNestedScrollingEnabled(false);
        assbind.rcvSalesGoods.setAdapter(
                new SalesSlipmentAdapter(ssgmLists,
                        SalesSlipmentActivity.this,todoTrueBillFalse));
        assbind.setVariable(BR.sale_layout_data,ssModule);
        assbind.btnAllSpreadPackUpSalesSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_visible[0]){
                    assbind.rcvSalesGoods.setVisibility(View.GONE);
                    assbind.btnAllSpreadPackUpSalesSlip.setText("展开");
                    flag_visible[0] = false;

                }else{
                    assbind.rcvSalesGoods.setVisibility(View.VISIBLE);
                    assbind.btnAllSpreadPackUpSalesSlip.setText("收起");
                    flag_visible[0] = true;
                }
            }
        });

        _judgeHavingData();

        assbind.includeSalesSlipTop.llCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = CommonUtils.notEmptyStr(ssModule.getContactPhone())?ssModule.getContactPhone():"";
                if (!TextUtils.isEmpty(phone)){
                    CommonUtils.callPhone(phone,SalesSlipmentActivity.this);
                }else{
                    ToastUtil.show(SalesSlipmentActivity.this,WmsApi.tip.noRecordPhone,Toast.LENGTH_SHORT);
                }
            }
        });


        assbind.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _saleApprovalSubmit("","");
                progressDialog = CommonUtils.showProgessDialog(progressDialog,SalesSlipmentActivity.this,"正在校验...");
                approveCheck(map);
            }
        });


        assbind.tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(SalesSlipmentActivity.this);
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
                        //拒绝理由
                        String comment = "";
                        if (builder.getEditText().getText().toString().trim()==null
                                || (builder.getEditText().getText().toString().trim().equals(""))
                                ){
                            comment = "";
                        }else{
                            comment = builder.getEditText().getText().toString().trim();

                            dialog.dismiss();
                            _saleApprovalSubmit(WmsApi.approveCode.REJECTED,comment);
                            progressDialog = CommonUtils.showProgessDialog(progressDialog,SalesSlipmentActivity.this,"正在提交...");
                            refuseAgree(map);
                        }
                    }
                });
                CommonUtils.getWidthHeight(builder.create(),SalesSlipmentActivity.this).show();
            }
        });

    }

    /**
     * 校验失败，fail
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(SALESCHECK_FAILED)
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
                    @Tag(SALESCHECK_CONFIRM)
            }
    )
    public void checgConfim(final HttpResults response){
        CommonUtils.hideProgressDialog(progressDialog);
        assbind.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(customDialog == null){
                    customDialog=new CustomDialog(SalesSlipmentActivity.this,R.style.project_back)
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
                    @Tag(SALESCHECK_SUCCESS)
            }
    )
    public void getCheckSuccess(HttpResults response){

        CommonUtils.hideProgressDialog(progressDialog);
        assbind.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        },500);
    }

    private void _saleApprovalSubmit(String outcome,String comment){
        ApprovalRequestSaleModule rrsModule = new ApprovalRequestSaleModule();
        rrsModule.setUser(AppUtil.getValue(SalesSlipmentActivity.this,WMS_USER));
        rrsModule.setComment(comment);
        rrsModule.setOperation(operation);
        rrsModule.setReceiptNo(ssModule.getReceiptNo());
        rrsModule.setOutcome(outcome);
        String strJson = CommonUtils.ObjectToJson(rrsModule);
        map = CommonUtils.parseData(strJson);
        map.put("receiptType", WmsApi.billForm.saleForm);
    }

    public void showDialog(){

        final CustomePopupWindow.Builder builder = new CustomePopupWindow.Builder(SalesSlipmentActivity.this);
        builder.setTitle("审批意见");
        builder.seteditTextHint("非必填（最多200字）");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //想后台提交数据，表示提交

                String comment = "";
                if (builder.getEditText().getText().toString().trim()==null
                        || (builder.getEditText().getText().toString().trim().equals(""))
                        ){
                    comment = "";
                }else{
                    comment = builder.getEditText().getText().toString().trim();
                }
                dialog.dismiss();
                _saleApprovalSubmit(WmsApi.approveCode.APPROVED,comment);
                progressDialog = CommonUtils.showProgessDialog(progressDialog,SalesSlipmentActivity.this,"正在提交...");
                refuseAgree(map);
            }
        });
        CommonUtils.getWidthHeight(builder.create(),SalesSlipmentActivity.this).show();
    }

}
