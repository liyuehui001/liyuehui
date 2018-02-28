package com.smec.wms.android.activity;

/**
 * Created by Adobe on 2016/1/20.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.CheckDetailItem;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.GetCheckOrderNoPSResponse;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点操作
 */
public class CheckMatnrActivity extends BaseScanActivity {
    private static final int STEP_CHECK_ORDER = 1;    //扫描盘点单
    private static final int STEP_SHELF = 2;          //扫描货位
    private static final int STEP_MATNR = 3;          //扫描物料
    private static final int STEP_AMOUNT = 4;         //输入数量
    private int operationStep = STEP_CHECK_ORDER;     //默认第一步是扫描盘点单
    private ScanPageInformation baseComponents;

    private String checkOrderNo = null;
    private String shelfNo = null;
    private String matnrNo = null;

    //views
    private TextView tvCheckOrder;  //盘点单
    private TextView tvShelf;       //货位
    private TextView tvMatnr;       //物料
    private TextView tvMatnrName;   //物料名称
    private TextView tvUnit;        //单位
    private TextView tvChecked;     //已盘数量
    private TextView etAmount;      //盘点数量
    private Button btSubmit;        //提交


    @Override
    protected int getContentViewId() {
        return R.layout.activity_check;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViews();
        this.gotoStep(STEP_CHECK_ORDER);
    }


    private void initViews() {
        tvCheckOrder = (TextView) this.findViewById(R.id.check_orderno);
        tvShelf = (TextView) this.findViewById(R.id.check_shelf);
        tvMatnr = (TextView) this.findViewById(R.id.check_matnr);
        tvMatnrName = (TextView) this.findViewById(R.id.check_matnrname);
        tvUnit = (TextView) this.findViewById(R.id.check_unit);
        tvChecked = (TextView) this.findViewById(R.id.check_checked);
        etAmount = (EditText) this.findViewById(R.id.check_amount);
        btSubmit = (Button) this.findViewById(R.id.check_submit);
        btSubmit.setOnClickListener(this);

        this.bindLayoutDoubleClick(new int[]{R.id.step_layout_1,
                        R.id.step_layout_2, R.id.step_layout_3},
                new int[]{STEP_CHECK_ORDER, STEP_SHELF, STEP_MATNR});
    }

    @Override
    protected ScanPageInformation getPageComponentInf() {
        if (baseComponents == null) {
            baseComponents = new ScanPageInformation(this);
            baseComponents.addTimeLine("盘点单号");
            baseComponents.addTimeLine("货位号");
            baseComponents.addTimeLine("物料编码");
            baseComponents.addTimeLine("输入数量");
            baseComponents.setBarcodeShowViewId(R.id.inboud_barcode);
            baseComponents.setSummaryViewId(R.id.inbound_scan_summary);
        }
        return baseComponents;
    }

    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        if (this.operationStep == STEP_CHECK_ORDER) {
            //验证盘点单号是否有效
            Map<String, String> params = new HashMap<>();
            params.put("WarehouseNo", this.getUserProfile().getWarehouseNo());
            params.put("CheckOrderNo", value);
            this.request(IFace.RF_CHECK_PD_ORDER, params, CommonResponse.class, "正在验证盘点单" + value + ".....");
        } else if (this.operationStep == STEP_SHELF) {
            //获取已盘点的数量
            this.tvShelf.setText(value);
            this.shelfNo = value;
            this.nextTimeLine();
            this.gotoStep(STEP_MATNR);
        } else if (this.operationStep == STEP_MATNR) {
            //获取已盘点的数量
            Map<String, String> params = new HashMap<>();
            params.put("CheckOrderNo", this.checkOrderNo);
            params.put("ShelfSpaceNo", this.shelfNo);
            params.put("Matnr", value);
            this.request(IFace.RF_GET_PD_FINISH_QTY, params, GetCheckOrderNoPSResponse.class, "正在获取物料信息....");
        }
    }


    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
        String iface = response.getIface();
        if (IFace.RF_CHECK_PD_ORDER.equals(iface)) {
            //验证盘点单号是否有效
            this.checkOrderNo = response.getRequestParams().get("CheckOrderNo");
            this.tvCheckOrder.setText(this.checkOrderNo);
            this.nextTimeLine();
            this.gotoStep(STEP_SHELF);
        } else if (IFace.RF_GET_PD_FINISH_QTY.equals(iface)) {
            //获取已盘点的数量
            this.matnrNo = response.getRequestParams().get("Matnr");
            List<CheckDetailItem> detailList = ((GetCheckOrderNoPSResponse) response.getData()).getCheckDetail();
            if (detailList == null || detailList.size() == 0) {
                ToastUtil.show(this, "没有物料信息");
                return;
            }
            CheckDetailItem detailItem = detailList.get(0);

            tvMatnr.setText(this.matnrNo);
            tvMatnrName.setText(detailItem.getMatnrName());
            tvUnit.setText(detailItem.getPkgUnit());
            tvChecked.setText(detailItem.getCheckQty());
            this.nextTimeLine();
            this.gotoStep(STEP_AMOUNT);
        } else if (IFace.RF_COMMIT_PD_QTY.equals(iface)) {
            //提交盘点操作信息成功返回
            this.shelfNo = null;
            this.matnrNo = null;
            this.tvShelf.setText(this.shelfNo);
            this.tvMatnr.setText(this.matnrNo);
            this.tvMatnrName.setText(null);
            this.tvChecked.setText(null);
            this.tvUnit.setText(null);
            this.etAmount.setText(null);
            this.gotoStep(STEP_SHELF);
            this.goToTimeLine(1);
            ToastUtil.show(this, "提交成功,请扫描下一个货位号");
        }
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        if (v.getId() == R.id.check_submit) {
            //提交盘点操作信息
            if (AppUtil.strNull(this.checkOrderNo)) {
                ToastUtil.show(this, "请先扫描盘点单号");
                return;
            }
            if (AppUtil.strNull(this.shelfNo)) {
                ToastUtil.show(this, "请先扫描货位号");
                return;
            }
            if (AppUtil.strNull(this.matnrNo)) {
                ToastUtil.show(this, "请先扫描物料号");
                return;
            }
            String content = this.etAmount.getText().toString();
            if (AppUtil.strNull(content)) {
                ToastUtil.show(this, "请输入物料数量");
                return;
            }
            Map<String, String> params = new HashMap<>();
            params.put("CheckOrderNo", this.checkOrderNo);
            params.put("ShelfSpaceNo", this.shelfNo);
            params.put("Matnr", this.matnrNo);
            params.put("CheckQty", content);
            params.put("UserName", this.getUserProfile().getUid());
            this.request(IFace.RF_COMMIT_PD_QTY, params, ResponseData.class, "正在提交数据...");
        }
    }

    private void gotoStep(int step) {
        this.operationStep = step;
        if (step == STEP_CHECK_ORDER) {
            this.focusStepLayout(R.id.step_layout_1);
        } else if (step == STEP_SHELF) {
            this.focusStepLayout(R.id.step_layout_2);
        } else if (step == STEP_MATNR) {
            this.focusStepLayout(R.id.step_layout_3);
        } else if (step == STEP_AMOUNT) {
            //打开键盘
            AppUtil.openIMM(this,this.etAmount);
            this.focusStepLayout(R.id.step_layout_4);
        }
    }


    /**
     * 用户撤销动作。返回上一步
     */
    @Override
    protected void cancelAction() {
        if(this.operationStep==STEP_CHECK_ORDER){

        }else if(this.operationStep==STEP_SHELF){
            this.tvShelf.setText(null);
            this.shelfNo=null;
            this.checkOrderNo=null;
            this.tvCheckOrder.setText(null);
            this.preTimeLine();
            this.gotoStep(STEP_CHECK_ORDER);
        }else if(this.operationStep==STEP_MATNR){
            this.matnrNo=null;
            this.shelfNo=null;
            this.tvMatnr.setText(null);
            this.tvShelf.setText(null);
            this.preTimeLine();
            this.gotoStep(STEP_SHELF);
        }else if(this.operationStep==STEP_AMOUNT){
            this.matnrNo=null;
            this.tvMatnr.setText(null);
            this.etAmount.setText(null);
            this.tvMatnrName.setText(null);
            this.tvUnit.setText(null);
            this.tvChecked.setText(null);
            this.preTimeLine();
            this.gotoStep(STEP_MATNR);
        }
    }


}
