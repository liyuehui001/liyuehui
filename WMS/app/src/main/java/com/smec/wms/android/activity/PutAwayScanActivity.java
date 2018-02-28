package com.smec.wms.android.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.CheckRDCFGSPutawayBoxNoRespnose;
import com.smec.wms.android.bean.CheckRDCFGSPutawayMatnrResponse;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.PutawayDetail;
import com.smec.wms.android.bean.PutawayDetailOfflineResponse;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/18.
 */
public class PutAwayScanActivity extends BaseScanActivity {
    private final static int STEP_RK = 1;     //扫描入库单号
    private final static int STEP_BOX = 2;    //扫描箱号
    private final static int STEP_MATNR = 3;  //扫描物料号
    private final static int STEP_SHELF = 4;  //扫描货架
    private final static int STEP_AMOUNT = 5;   //输入数量
    private final static int STEP_FINISH = 6; //已完成

    private int operationStep = STEP_RK;
    private String rkNo;    //当前入库单号
    private String boxNo;   //当前箱号
    private String matnrNo; //当前物料号
    private String shelfNo; //当前货位好
    private ScanPageInformation baseComponents;


    //views
    private TextView tvRk;          //入库单
    private TextView tvBox;         //箱号
    private TextView tvMatnr;       //物料号
    private TextView tvOrderQty;    //装箱数量
    private TextView tvPutawayQty;  //已收数量
    private TextView tvSuggetst;    //建议货位
    private TextView tvUnit;        //单位
    private EditText etAmount;      //数量
    private TextView tvShelf;       //货架
    private Button btDetail;        //操作明细按钮
    private Button btGenerate;      //返回
    private Button btSubmit;        //提交

    private boolean isNoBox = false;  //是否有箱子


    protected int getContentViewId() {
        return R.layout.activity_putaway;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViews();
        this.gotoStep(STEP_RK);
    }

    private void initViews() {
        tvRk = (TextView) this.findViewById(R.id.putaway_rk);
        tvBox = (TextView) this.findViewById(R.id.putaway_box);
        tvMatnr = (TextView) this.findViewById(R.id.putaway_matnr);
        tvShelf = (TextView) this.findViewById(R.id.putaway_shelf);
        tvOrderQty = (TextView) this.findViewById(R.id.putaway_orderqty);
        tvPutawayQty = (TextView) this.findViewById(R.id.putaway_putawayqty);
        tvSuggetst = (TextView) this.findViewById(R.id.putaway_suggestshelfspac);
        etAmount = (EditText) this.findViewById(R.id.putaway_amount);
        tvUnit = (TextView) this.findViewById(R.id.putaway_unit);
        btDetail = (Button) this.findViewById(R.id.putaway_action_detail);
        btGenerate = (Button) this.findViewById(R.id.putaway_action_getrk);
        btSubmit = (Button) this.findViewById(R.id.putaway_action_submit);
        btDetail.setOnClickListener(this);
        btGenerate.setOnClickListener(this);
        btSubmit.setOnClickListener(this);

        this.bindLayoutDoubleClick(new int[]{R.id.step_layout_1,
                        R.id.step_layout_2, R.id.step_layout_3,
                        R.id.step_layout_4},
                new int[]{STEP_RK, STEP_BOX, STEP_MATNR, STEP_SHELF});

    }

    @Override
    protected ScanPageInformation getPageComponentInf() {
        if (baseComponents == null) {
            baseComponents = new ScanPageInformation(this);
            baseComponents.addTimeLine("扫描入库单");
            baseComponents.addTimeLine("扫描箱号");
            baseComponents.addTimeLine("扫描物料号");
            baseComponents.addTimeLine("扫描货位");
        }
        return baseComponents;
    }


    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        if (this.operationStep == STEP_RK) {
            //RDC上架_验证单号
            Map<String, String> params = new HashMap();
            params.put("InboundOrderNo", value);
            this.request(IFace.CHECK_RDCFGS_PUTAWAY_ORDER_NOA, params, CommonResponse.class, "正在验证入库单" + value + "...");
        } else if (this.operationStep == STEP_BOX) {
            //RDC上架_验证箱号
            if (AppUtil.strNull(this.rkNo)) {
                //根据箱号生成入库单
                Map<String, String> params = new HashMap();
                params.put("BoxNo", value);
                params.put("User", this.getWmsApplication().getUserProfile().getUid());
                //开始多次请求
                this.beginMulRequest();
                this.request(IFace.RF_GETORDERNOWITHBOX, params, CommonResponse.class, "正在获取入库单...");
                return;
            }
            Map<String, String> params = new HashMap();
            params.put("InboundOrderNo", this.rkNo);
            params.put("BoxNo", value);
            this.request(IFace.CHECK_RDCFGS_PUTAWAY_BOX_NO, params, CheckRDCFGSPutawayBoxNoRespnose.class, "正在请求箱子" + value + "...");
        } else if (this.operationStep == STEP_MATNR) {
            //RDC上架_验证物料号
            Map<String, String> params = new HashMap<>();
            params.put("InboundOrderNo", this.rkNo);
            params.put("BoxNo", this.boxNo);
            params.put("Matnr", value);
            this.request(IFace.CHECK_RDCFGS_PUTAWAY_MATNR, params, CheckRDCFGSPutawayMatnrResponse.class, "正在请求物料" + value + "...");
        } else if (this.operationStep == STEP_SHELF) {
            //RDC_上架
            Map<String, String> params = new HashMap();
            params.put("WarehouseNo", this.getUserProfile().getWarehouseNo());
            params.put("ShelfSpaceNo", value);
            this.request(IFace.COMMON_CHECK_SHELF_NO, params, ResponseData.class, "正在验证货位" + value + "...");
        }
    }

    /**
     * 所有接口数据调用返回成功
     *
     * @param response
     */
    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
        String iface = response.getIface();
        Map<String, String> requestParams = response.getRequestParams();
        if (IFace.CHECK_RDCFGS_PUTAWAY_ORDER_NOA.equals(iface)) {
            //RDC上架_验证单号
            this.rkNo = response.getRequestParams().get("InboundOrderNo");
            this.tvRk.setText(this.rkNo);
            CommonResponse data = (CommonResponse) response.getData();
            this.isNoBox = "true".equals(data.getIsNoBox());
            String flag = requestParams.get("flag");
            if (this.isNoBox) {
                this.tvBox.setText("无箱上架");
                this.boxNo = "";
                this.gotoStep(STEP_MATNR);//无箱上架,跳过箱号扫描直接扫描物料
            } else if (flag == null) {
                this.gotoStep(STEP_BOX);    //扫描入库单号当前步骤修改为扫描箱号
            }
            if (flag != null) {
                this.endMulRequest();
                this.gotoStep(STEP_MATNR);
            }
        } else if (IFace.CHECK_RDCFGS_PUTAWAY_BOX_NO.equals(iface)) {
            //RDC上架_验证箱号
            this.boxNo = response.getRequestParams().get("BoxNo");
            this.tvBox.setText(this.boxNo);
            this.gotoStep(STEP_MATNR);      //扫描箱号后当前步骤修改为扫描物料号
        } else if (IFace.CHECK_RDCFGS_PUTAWAY_MATNR.equals(iface)) {
            //RDC上架_验证物料号
            this.matnrNo = response.getRequestParams().get("Matnr");
            this.tvMatnr.setText(this.matnrNo);
            CheckRDCFGSPutawayMatnrResponse matnrData = (CheckRDCFGSPutawayMatnrResponse) response.getData();
            PutawayDetail matnrDetail = matnrData.getPutawayDetail().get(0);
            this.setMatnrDetail(matnrDetail);
            this.gotoStep(STEP_SHELF);      //扫描物料号后当前步骤修改为扫描货位
        } else if (IFace.RF_COMMIT_RDC_SLOTIN_QTY.equals(iface)) {
            //RDC分公司上架_提交操作数量
            this.boxNo = null;
            this.shelfNo = null;
            this.setMatnrDetail(new PutawayDetail());
            String isComplete = ((CommonResponse) response.getData()).getIsCompletedFlag();
            if ("true".equals(isComplete)) {
                this.gotoStep(STEP_FINISH); //结束
            } else {
                this.gotoStep(STEP_MATNR);
                ToastUtil.show(this, "提交成功,请继续扫描物料号");
//                if (this.isNoBox) {
//                    this.gotoStep(STEP_MATNR);  //无箱上架,返回到物料号扫描
//                    ToastUtil.show(this, "提交成功,请继续扫描物料号");
//                } else {
//                    ToastUtil.show(this, "提交成功,请继续扫描箱号");
//                    this.gotoStep(STEP_BOX);    //返回到箱号输入步骤
//                }
            }
        } else if (IFace.RF_QUERY_RDC_SLOTIN_DETAILS.equals(iface)) {
            //RDC上架_获取未上架明细
            List<PutawayDetail> detailList = ((CheckRDCFGSPutawayMatnrResponse) response.getData()).getPutawayDetail();
            Intent intent = new Intent();
            intent.setClass(PutAwayScanActivity.this, PutawayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) detailList);
            bundle.putString("rkNo", this.rkNo);
            intent.putExtras(bundle);
            this.startActivity(intent);
        } else if (IFace.RF_GETORDERNOWITHBOX.equals(iface)) {
            //根据箱号生成入库单号
            this.rkNo = ((CommonResponse) response.getData()).getInboundOrderNo();
            this.boxNo = response.getRequestParams().get("BoxNo");
            this.tvRk.setText(this.rkNo);
            this.tvBox.setText(this.boxNo);
            Map<String, String> params = new HashMap();
            params.put("flag", "");
            params.put("InboundOrderNo", this.rkNo);
            this.request(IFace.CHECK_RDCFGS_PUTAWAY_ORDER_NOA, params, CommonResponse.class, "正在验证入库单" + this.rkNo + "...");
        } else if (IFace.COMMON_CHECK_SHELF_NO.equals(iface)) {
            //验证货位号
            this.shelfNo = requestParams.get("ShelfSpaceNo");
            this.tvShelf.setText(this.shelfNo);
            this.gotoStep(STEP_AMOUNT);
        }

    }

    /**
     * 显示在条码扫描区文字默认是扫描到的条码
     *
     * @param barcode
     * @return
     */
    @Override
    protected String getBarcodeShow(String barcode) {
        return barcode;
    }

    @Override
    protected void clickActionHandler(View v) {
        switch (v.getId()) {
            case R.id.putaway_action_detail:
                showDetail();
                break;
            case R.id.putaway_action_submit:
                submitPutaway();
                break;
            case R.id.putaway_action_getrk:
                generateRKByBoxNoAction();
                break;
            default:
                break;
        }
    }

    /**
     * 显示操作明细
     */
    private void showDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("InboundOrderNo", this.rkNo);
        // params.put("boxNo", this.boxNo);
        params.put("detailType", "2");
        this.request(IFace.RF_QUERY_RDC_SLOTIN_DETAILS, params, CheckRDCFGSPutawayMatnrResponse.class, "正在请求数据....");
    }

    private void generateRKByBoxNoAction() {
        this.gotoStep(STEP_BOX);
        ToastUtil.show(this, "请扫描箱号");
    }

    /**
     * 提交上架信息
     */
    private void submitPutaway() {
//        if(this.operationStep==STEP_BOX){
//            this.boxNo="";
//            this.gotoStep(STEP_MATNR);
//            return;
//        }
        if (AppUtil.strNull(this.rkNo)) {
            ToastUtil.show(this, "请先扫描入库单号");
            return;
        }
        if (AppUtil.strNull(this.boxNo) && !this.isNoBox) {
            ToastUtil.show(this, "请先扫描箱号");
            return;
        }
        if (AppUtil.strNull(this.matnrNo)) {
            ToastUtil.show(this, "请先扫描物料号");
            return;
        }
        if (AppUtil.strNull(this.shelfNo)) {
            ToastUtil.show(this, "请先扫描货位号");
            return;
        }
        String content = etAmount.getText().toString();
        if (AppUtil.strNull(content)) {
            ToastUtil.show(this, "请输入数量");
            return;
        }
        content = content.trim();
        Integer amount = 0;
        try {
            amount = Integer.parseInt(content);
        } catch (Exception ex) {
            this.etAmount.setText(null);
            ToastUtil.show(this, "数量输入有误");
            return;
        }

        //提交信息
        Map<String, String> params = new HashMap<>();
        params.put("InboundOrderNo", this.rkNo);
        params.put("BoxNo", this.boxNo);
        params.put("Matnr", this.matnrNo);
        params.put("ShelfSpaceNo", this.shelfNo);
        params.put("InboundQty", String.valueOf(amount));
        params.put("UserName", this.getUserProfile().getUid());
        this.request(IFace.RF_COMMIT_RDC_SLOTIN_QTY, params, CommonResponse.class, "正在提交数据....");

    }

    private void setMatnrDetail(PutawayDetail detail) {
        tvOrderQty.setText(detail.getOrderQty());
        tvPutawayQty.setText(detail.getPutawayQty());
        tvSuggetst.setText(detail.getSuggestShelfSpace());
        tvUnit.setText(detail.getPkgUnit());
    }

    private void gotoStep(int step) {
        this.operationStep = step;
        if (step == STEP_RK) {
            this.focusStepLayout(R.id.step_layout_1);
            this.goToTimeLine(0);
        } else if (step == STEP_BOX) {
            this.tvBox.setText(this.boxNo);
            this.tvShelf.setText(this.shelfNo);
            this.etAmount.setText(null);
            this.tvMatnr.setText(null);
            this.boxNo=null;
            this.matnrNo=null;
            this.shelfNo=null;
            this.focusStepLayout(R.id.step_layout_2);
            this.goToTimeLine(1);
        } else if (step == STEP_MATNR) {
            this.shelfNo = null;
            this.matnrNo = null;
            this.tvShelf.setText(null);
            this.etAmount.setText(null);
            this.tvMatnr.setText(null);
            this.focusStepLayout(R.id.step_layout_3);
            this.goToTimeLine(2);
        } else if (step == STEP_SHELF) {
            this.focusStepLayout(R.id.step_layout_5);
            this.goToTimeLine(3);
        } else if (step == STEP_AMOUNT) {
            //打开键盘
            AppUtil.openIMM(this, this.etAmount);
            this.focusStepLayout(R.id.step_layout_4);
            this.goToTimeLine(3);
        } else if (step == STEP_FINISH) {
        //    ToastUtil.show(this, "所有物料已上架完成");
            stepAllReset();
            this.showCompleteDialog("所有物料已上架完成");

        }
    }

    /**
     * 用户撤销动作。返回上一步
     */
    @Override
    protected void cancelAction() {
        if (operationStep == STEP_RK) {

        } else if (operationStep == STEP_BOX) {
            this.stepBoxReset();
        } else if (operationStep == STEP_MATNR) {
            this.stepMatnrReset();
        } else if (operationStep == STEP_SHELF) {
            this.stepShelfReset();
        } else if (operationStep == STEP_AMOUNT) {
            this.etAmount.setText(null);
            this.shelfNo = null;
            this.tvShelf.setText(null);
            this.gotoStep(STEP_MATNR);
        } else if (operationStep == STEP_FINISH) {

        }
    }

    private void stepBoxReset() {
        this.gotoStep(STEP_RK);
        this.preTimeLine();
        this.boxNo = null;
        this.rkNo = null;
        this.tvRk.setText(null);
        this.tvBox.setText(null);
    }

    private void stepShelfReset() {
        this.gotoStep(STEP_MATNR);
        this.preTimeLine();
        this.matnrNo = null;
        this.tvMatnr.setText(null);
        this.tvUnit.setText(null);
        this.tvOrderQty.setText(null);
        this.tvPutawayQty.setText(null);
        this.tvSuggetst.setText(null);
        this.etAmount.setText(null);
    }

    private void stepMatnrReset() {
        this.gotoStep(STEP_BOX);
        this.preTimeLine();
        this.boxNo = null;
        this.tvBox.setText(null);
    }

    private void stepAllReset() {
        this.stepMatnrReset();
        this.stepShelfReset();
        this.stepBoxReset();
    }

    @Override
    protected int getCurrentOperationStep() {
        return this.operationStep;
    }

}
