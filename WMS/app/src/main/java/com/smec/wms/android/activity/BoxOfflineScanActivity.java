package com.smec.wms.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.InboundBoxDetailAdapter;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.bean.StockInOrderResponse;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxOfflineScanActivity extends BaseScanActivity {
    public static final int STEP_RK = 1;
    public static final int STEP_BOX = 2;
    public static final int STEP_FINISH = 3;

    private ListView boxDetailList;
    private InboundBoxDetailAdapter boxDetailAdapter;
    private int operationStep = 1;        //标识操作步骤 1：扫描入库单号 2：扫描箱号 3：操作完成
    private String rkNo = null;

    private Button btCommit;
    private TextView orderNoView;
    private TextView supplyView;
    private View.OnClickListener actionHandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.inboud_box_generate_orderno:
                    commit();
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
        btCommit = (Button) this.findViewById(R.id.inboud_box_generate_orderno);
        btCommit.setOnClickListener(actionHandler);
        orderNoView = (TextView) this.findViewById(R.id.inbound_scan_orderno);
        supplyView = (TextView) this.findViewById(R.id.inbound_scan_order_supply);
        btCommit.setText("提交");
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
            //扫描箱号
            boolean result = this.boxDetailAdapter.boxChecked(value);
            if (false == result) {
                ToastUtil.show(this, "未找到箱号:" + value);
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
        this.request(IFace.RF_CHECK_RDC_STOCKIN_ORDER, params, StockInOrderResponse.class, "正在请求入库单"+rk+"信息...");
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
        } else if (IFace.RF_CHECK_RDC_STOCKIN_ALLBOX.equals(iface)) {
            this.completeOperation();
        }

    }

    public void completeOperation() {
        this.nextTimeLine();
        this.operationStep = STEP_FINISH;
        ToastUtil.show(this, "入库操作完成");
        this.btCommit.setText("完成");
    }

    private DialogInterface.OnClickListener commitDialogListneer = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    confirmCommit();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 根据箱号生成入库单号
     */
    private void commit() {
        StockInOrderResponse detail = this.boxDetailAdapter.getBoxDetail();
        if (detail == null || detail.getBoxNoList() == null || detail.getBoxNoList().size() == 0) {
            ToastUtil.show(this, "无可提交数据");
            return;
        }
        if (this.operationStep != STEP_FINISH) {
            boolean result = this.boxDetailAdapter.isAllChecked();
            if (false == result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确认").setMessage("还有箱子未扫描入库，是否提交数据?");
                builder.setPositiveButton("确定", commitDialogListneer);
                builder.setNegativeButton("取消", commitDialogListneer);
            } else {
                confirmCommit();
            }
        } else {
            this.back();
        }
    }

    private void confirmCommit() {
        List<String> boxList = this.boxDetailAdapter.getCheckedBoxList();
        StringBuffer buf = new StringBuffer("{\"BoxNoList\":[");
        String spliter = "";
        for (String box : boxList) {
            String tmp = String.format("%s{\"BoxNo\":\"%s\"}", spliter, box);
            spliter = ",";
            buf.append(tmp);
        }
        buf.append("]}");
        Map<String, String> params = new HashMap<>();
        params.put("inboundOrderNo", this.rkNo);
        params.put("boxNoList", buf.toString());
        this.request(IFace.RF_CHECK_RDC_STOCKIN_ALLBOX, params, ResponseData.class, "正在提交数据....");
    }

    /**
     * 用户撤销动作。返回上一步
     */
    @Override
    protected void cancelAction() {
        ToastUtil.show(this, "不支持的操作");
    }
}
