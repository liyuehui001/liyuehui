package com.smec.wms.android.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.InboundBoxDetailAdapter;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.bean.StockInOrderResponse;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class BoxOnlineScanActivity extends BaseScanActivity {
    public static final int STEP_RK = 1;
    public static final int STEP_BOX = 2;
    public static final int STEP_FINISH = 3;

    private ListView boxDetailList;
    private InboundBoxDetailAdapter boxDetailAdapter;
    private int operationStep = 1;        //标识操作步骤 1：扫描入库单号 2：扫描箱号 3：操作完成
    private boolean isOrderNoByBox = false;   //入库单号是否根据箱号生成
    private String rkNo = null;
    private String boxNo = null;

    private Button generateOrderNoAction;
    private TextView orderNoView;
    private TextView supplyView;
    private View.OnClickListener actionHandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.inboud_box_generate_orderno:
                    generateOrderNoAction();
                    break;
                default:
                    break;

            }
        }
    };


    private ScanPageInformation baseComponents;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_inbound;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViews();
    }

    private void initViews() {
        boxDetailList = (ListView) this.findViewById(R.id.inboud_box_detail_list);
        boxDetailAdapter = new InboundBoxDetailAdapter(this, IFace.RF_QUERY_RDC_STOCKLN_DETAILS);
        boxDetailList.setAdapter(boxDetailAdapter);
        generateOrderNoAction = (Button) this.findViewById(R.id.inboud_box_generate_orderno);
        generateOrderNoAction.setOnClickListener(actionHandler);
        orderNoView = (TextView) this.findViewById(R.id.inbound_scan_orderno);
        supplyView = (TextView) this.findViewById(R.id.inbound_scan_order_supply);
    }

    @Override
    protected ScanPageInformation getPageComponentInf() {
        if (baseComponents == null) {
            baseComponents = new ScanPageInformation(this);
            baseComponents.addTimeLine("扫描入库单号");
            baseComponents.addTimeLine("扫描箱号");
            baseComponents.addTimeLine("操作完成");
            baseComponents.setBarcodeShowViewId(R.id.inboud_barcode);
            baseComponents.setSummaryViewId(R.id.inbound_scan_summary);
        }
        return baseComponents;
    }

    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        if (this.operationStep == STEP_RK) {
            //扫描入库单号
            this.requestRK(value);
        } else if (this.operationStep == STEP_BOX) {
            this.boxNo = value;
            //扫描箱号
            if (this.rkNo == null) {
                //根据箱号生成入库单号
                this.requestRkByBox(value);
            } else {
                this.requestBox(value);
            }
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
        if (operationStep == STEP_RK) {
            return "已扫描入库单:" + barcode;
        } else if (operationStep == STEP_BOX) {
            return "已扫描箱号:" + barcode;
        }
        return barcode;
    }

    private void requestRK(String rk) {
        Map<String, String> params = new HashMap();
        params.put("InboundOrderNo", rk);
        params.put("IsDownAllBoxNo", "1");
        this.request(IFace.RF_CHECK_RDC_STOCKIN_ORDER, params, StockInOrderResponse.class, "正在请求入库单信息...");
    }

    private void requestBox(String box) {
        this.boxNo = box;
        Map<String, String> params = new HashMap();
        params.put("InboundOrderNo", this.rkNo);
        params.put("BoxNo", boxNo);
        this.request(IFace.RF_CHECK_RDC_STOCKIN_BOXID, params, CommonResponse.class, String.format("正在验证箱号%s....", boxNo));
    }

    private void requestRkByBox(String box) {
        this.boxNo = box;
        Map<String, String> params = new HashMap();
        params.put("BoxNo", box);
        params.put("User", this.getWmsApplication().getUserProfile().getUid());
        //开始多次请求
        this.beginMulRequest();
        this.request(IFace.RF_GETORDERNOWITHBOX, params, CommonResponse.class, "正在获取入库单...");
    }

    private void RdcStockInOrderResponse(ServerResponse response) {
        //RDC入库_验证单号
        this.rkNo = response.getRequestParams().get("InboundOrderNo");
        this.goToTimeLine(1);   //设置时间轴
        StockInOrderResponse order = (StockInOrderResponse) response.getData();
        this.boxDetailAdapter.setBoxDetail(order);
        String sumary = String.format("总收箱数:%s 已收箱数:%s", order.getAllBoxCount(), order.getReceivedCount());
        this.setSumaryText(sumary);
        orderNoView.setText("入库单:" + rkNo);
        supplyView.setText("供应商:" + order.getSupplierInfo());
        this.operationStep = STEP_BOX;   //扫描箱号
    }

    private void RdcStockInBoxIdResponse(ServerResponse response) {
        //RDC入库_验证箱号
        String boxNo = response.getRequestParams().get("BoxNo");
        this.boxDetailAdapter.boxChecked(boxNo);
        StockInOrderResponse order = this.boxDetailAdapter.getBoxDetail();
        String isComplete=((CommonResponse)response.getData()).getIsCompletedFlag();
        Integer received = order.getReceivedCountInt();
        received++;
        order.setReceivedCount(String.valueOf(received));
        this.boxDetailAdapter.setBoxDetail(order);
        String sumary = String.format("总收箱数:%s 已收箱数:%s", order.getAllBoxCount(), order.getReceivedCount());
        this.setSumaryText(sumary);
        if ("true".equals(isComplete)) {
            this.nextTimeLine();
            this.operationStep = STEP_FINISH;//操作完成
            this.completeOperation();
        }
        this.isOrderNoByBox = false;
        this.endMulRequest();
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
        if (IFace.RF_CHECK_RDC_STOCKIN_ORDER.equals(iface)) {
            this.RdcStockInOrderResponse(response);
            if (this.isOrderNoByBox) {
                //验证箱号
                this.isOrderNoByBox=false;
                this.requestBox(this.boxNo);
            }
        } else if (IFace.RF_CHECK_RDC_STOCKIN_BOXID.equals(iface)) {
            this.RdcStockInBoxIdResponse(response);
        } else if (IFace.RF_GETORDERNOWITHBOX.equals(iface)) {
            //根据箱号生成入库单号
            String rk = ((CommonResponse) response.getData()).getInboundOrderNo();
            //请求入库单信息
            this.requestRK(rk);
        }

    }

    /**
     * 根据箱号生成入库单号
     */
    private void generateOrderNoAction() {

        if (this.operationStep == STEP_RK) {
            this.isOrderNoByBox = true;
            this.playSound();
            this.operationStep = STEP_BOX;
            this.nextTimeLine();
            ToastUtil.show(this, "请扫描箱号");
        } else if (this.operationStep == STEP_BOX) {
            ToastUtil.show(this, "请扫描箱号");
        } else if (this.operationStep == STEP_FINISH) {
            this.back();
        }
    }

    public void completeOperation() {
       // ToastUtil.show(this, "入库操作完成");
        this.generateOrderNoAction.setText("完成");
        this.showCompleteDialog("入库操作完成");
    }

    /**
     * 用户撤销动作。返回上一步
     */
    @Override
    protected void cancelAction() {

    }
}
