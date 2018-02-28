package com.smec.wms.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.PickDetail;
import com.smec.wms.android.bean.PickDetailResponse;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PickActivity extends BaseScanActivity {
    public static final int STEP_JH = 0;
    public static final int STEP_SHELF = 1;
    public static final int STEP_MATNR = 2;
    public static final int STEP_AMOUNT = 3;
    public static final int STEP_FINISH = 4;

    private int operationStep = STEP_JH;

    private String pickNo;
    private String shelfNo;
    private String matnr;

    //views
    private TextView tvJH;      //拣货单
    private TextView tvShelf;   //货位号
    private TextView tvMatnr;   //物料号
    private TextView tvMatnrName;   //物料名称
    private TextView tvShouldQty;   //应拣数量
    private TextView tvRealQty;     //已拣数量
    private TextView tvUnit;    //单位
    private EditText etAmount;  //数量
    private Button btDetail;    //操作明细
    private Button btCommit;    //提交
    private TextView[] allTextViews;
    private ScanPageInformation baseComponents;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pick;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        tvJH = (TextView) this.findViewById(R.id.pick_no);
        tvShelf = (TextView) this.findViewById(R.id.pick_shelf);
        tvMatnr = (TextView) this.findViewById(R.id.pick_matnr);
        tvMatnrName = (TextView) this.findViewById(R.id.pick_matnr_name);
        tvShouldQty = (TextView) this.findViewById(R.id.pick_should_qty);
        tvRealQty = (TextView) this.findViewById(R.id.pick_real_qty);
        tvUnit = (TextView) this.findViewById(R.id.pick_unit);
        etAmount = (EditText) this.findViewById(R.id.pick_amount);
        btDetail = (Button) this.findViewById(R.id.pick_action_detail);
        btCommit = (Button) this.findViewById(R.id.pick_action_submit);
        btDetail.setOnClickListener(this);
        btCommit.setOnClickListener(this);
        allTextViews = new TextView[8];
        allTextViews[0] = tvJH;
        allTextViews[1] = tvShelf;
        allTextViews[2] = tvMatnr;
        allTextViews[3] = tvMatnrName;
        allTextViews[4] = tvShouldQty;
        allTextViews[5] = tvRealQty;
        allTextViews[6] = tvUnit;
        allTextViews[7] = etAmount;
        this.gotoStep(STEP_JH);

        this.bindLayoutDoubleClick(new int[]{R.id.step_layout_1,
                        R.id.step_layout_2, R.id.step_layout_3},
                new int[]{STEP_JH, STEP_SHELF, STEP_MATNR});
    }

    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        Map<String, String> params = new HashMap<>();
        if (this.operationStep == STEP_JH) {
            //拣货_拣货单验证
            params.put("WareHouseNo", this.getUserProfile().getWarehouseNo());
            params.put("PickOrderNo", value);
            this.request(IFace.PICK_CHECK_ORDER, params, ResponseData.class, String.format("正在验证拣货单:%s...", value));
        } else if (this.operationStep == STEP_SHELF) {
            //拣货_货位号验证
            params.put("WarehouseNo", this.getUserProfile().getWarehouseNo());
            params.put("ShelfSpaceNo", value);
            this.beginMulRequest();
            this.request(IFace.COMMON_CHECK_SHELF_NO, params, ResponseData.class, "正在验证货位" + value + "...");
        } else if (this.operationStep == STEP_MATNR) {
            //拣货_物料号验证
            params.put("PickOrderNo", this.pickNo);
            params.put("ShelfSpaceNo", this.shelfNo);
            params.put("Matnr", value);
            this.request(IFace.PICK_CHECK_MATNR, params, PickDetailResponse.class, String.format("正在获取物料:%s...", value));
        }
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
        String iface = response.getIface();
        Map<String, String> params = response.getRequestParams();
        if (IFace.PICK_CHECK_ORDER.equals(iface)) {
            //拣货_拣货单验证返回
            this.pickNo = params.get("PickOrderNo");
            this.tvJH.setText(this.pickNo);
            this.gotoStep(STEP_SHELF);
        } else if (IFace.PICK_CHECK_SHELF.equals(iface)) {
            //拣货_货位号验证返回
            this.endMulRequest();
            this.shelfNo = params.get("ShelfSpaceNo");
            this.tvShelf.setText(this.shelfNo);
            this.gotoStep(STEP_MATNR);
        } else if (IFace.PICK_CHECK_MATNR.equals(iface)) {
            //拣货_物料号验证返回
            PickDetailResponse data = (PickDetailResponse) response.getData();
            List<PickDetail> detailList = data.getPickDetail();
            if (detailList == null || detailList.size() == 0) {
                this.showMessage("没有该物料信息");
                return;
            }
            PickDetail item = detailList.get(0);
            this.matnr = params.get("Matnr");
            this.tvMatnr.setText(this.matnr);
            this.tvMatnrName.setText(item.getMatnrName());
            this.tvShouldQty.setText(item.getShouldQty());
            this.tvRealQty.setText(item.getRealQty());
            this.tvUnit.setText(item.getPkgUnit());
            this.gotoStep(STEP_AMOUNT);
        } else if (IFace.PICK_GET_DETAIL.equals(iface)) {
            //拣货_未拣货明细
            List<PickDetail> detailList = ((PickDetailResponse) response.getData()).getPickDetail();
            Intent intent = new Intent();
            intent.setClass(PickActivity.this, PickDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) detailList);
            bundle.putString("pickNo", this.pickNo);
            intent.putExtras(bundle);
            this.startActivity(intent);
        } else if (IFace.PICK_COMMIT_PICK.equals(iface)) {
            //拣货_提交拣货信息
            CommonResponse result = (CommonResponse) response.getData();
            String completed = result.getIsCompletedFlag();
            if ("true".equals(completed)) {
                // this.showMessage("所有物料都已拣货完成,请扫描新拣货单");
                this.gotoStep(STEP_JH);
                this.showCompleteDialog("所有物料都已拣货完成,请扫描新拣货单");
            } else {
                this.gotoStep(STEP_SHELF);
            }
        } else if (IFace.COMMON_CHECK_SHELF_NO.equals(iface)) {
            //拣货_验证货位(公共接口)
            Map<String, String> shelfParams = new HashMap<>();
            shelfParams.put("PickOrderNo", this.pickNo);
            shelfParams.put("ShelfSpaceNo", params.get("ShelfSpaceNo"));
            this.request(IFace.PICK_CHECK_SHELF, shelfParams, ResponseData.class, String.format("正在验证货位:%s...", params.get("ShelfSpaceNo")));
        }
    }

    @Override
    protected ScanPageInformation getPageComponentInf() {
        if (baseComponents == null) {
            baseComponents = new ScanPageInformation(this);
            baseComponents.addTimeLine("扫描拣货单");
            baseComponents.addTimeLine("扫描货位");
            baseComponents.addTimeLine("扫描物料号");
            baseComponents.addTimeLine("输入数量");
        }
        return baseComponents;
    }

    @Override
    protected void clickActionHandler(View v) {
        switch (v.getId()) {
            case R.id.pick_action_detail:
                showDetail();
                break;
            case R.id.pick_action_submit:
                submitPick();
                break;
            default:
                break;
        }
    }


    private void showDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("PickOrderNo", this.pickNo);
        this.request(IFace.PICK_GET_DETAIL, params, PickDetailResponse.class, "正确请求数据...");
    }

    private void submitPick() {
        if (AppUtil.strNull(this.pickNo)) {
            this.showMessage("请先扫描拣货单");
            return;
        }
        if (AppUtil.strNull(this.shelfNo)) {
            this.showMessage("请先扫描货位号");
            return;
        }
        if (AppUtil.strNull(this.matnr)) {
            this.showMessage("请先扫描物料号");
            return;
        }
        String amount = this.etAmount.getText().toString();
        if (AppUtil.strNull(amount)) {
            this.showMessage("请输入数量");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("PickOrderNo", this.pickNo);
        params.put("ShelfSpaceNo", this.shelfNo);
        params.put("Matnr", this.matnr);
        params.put("PickQty", amount);
        params.put("UserName", this.getUserProfile().getUid());
        this.request(IFace.PICK_COMMIT_PICK, params, CommonResponse.class, "正在提交数据....");
    }


    //    allTextViews[0]=tvJH;
//    allTextViews[1]=tvShelf;
//    allTextViews[2]=tvMatnr;
//    allTextViews[3]=tvMatnrName;
//    allTextViews[4]=tvShouldQty;
//    allTextViews[5]=tvRealQty;
//    allTextViews[6]=tvUnit;
//    allTextViews[7]=etAmount;
    private void gotoStep(int step) {
        this.operationStep = step;
        this.goToTimeLine(step);
        if (step == STEP_JH) {
            this.focusStepLayout(R.id.step_layout_1);
            pickNo = null;
            shelfNo = null;
            matnr = null;
            this.resetTextView(allTextViews, 0, allTextViews.length);
        } else if (step == STEP_SHELF) {
            this.focusStepLayout(R.id.step_layout_2);
            this.resetTextView(allTextViews, 1, allTextViews.length - 1);
            shelfNo = null;
            matnr = null;
        } else if (step == STEP_MATNR) {
            this.focusStepLayout(R.id.step_layout_3);
            this.resetTextView(allTextViews, 2, allTextViews.length - 2);
            this.matnr=null;
        } else if (step == STEP_AMOUNT) {
            this.focusStepLayout(R.id.step_layout_4);
        }
    }

    @Override
    protected void cancelAction() {
        if (this.operationStep == STEP_JH) {
            return;
        }
        this.gotoStep(this.operationStep - 1);
    }

    @Override
    protected boolean isResponseOK(ServerResponse response) {
        //货位验证只提醒不报错
        boolean bok = super.isResponseOK(response);
        if ("CheckPickShelfSpacePS".equals(response.getIface()) && !bok) {
            this.showMessage("警告:该货位不在建议货位中!");
            return true;
        }
        return bok;
    }

    @Override
    protected int getCurrentOperationStep() {
        return this.operationStep;
    }
}
