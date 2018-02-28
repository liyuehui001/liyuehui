package com.smec.wms.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.PackPlatAdapter;
import com.smec.wms.android.adapter.SimpleListItemAdapter;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.PackBoxDetail;
import com.smec.wms.android.bean.PackBoxDetailResponse;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PackActivity extends BaseScanActivity {
    public static final int STEP_PACK = 0;
    public static final int STEP_BOX = 1;
    public static final int STEP_MATNR = 2;
    public static final int STEP_AMOUNT = 3;
    public static final int STEP_FINISH = 4;
    public static final int STEP_PLAT = 5;    //印版扫描


    private int operationStep = STEP_PACK;
    private ScanPageInformation baseComponents;

    //views
    private TextView tvPack;        //拣货单号
    private TextView tvDestination; //目的地
    private TextView tvBoxNo;       //装箱箱号
    private TextView tvMatnr;       //物料编码
    private TextView tvMatnrName;   //物料名称
    private TextView tvShouldQty;   //单据数量
    private TextView tvRealQty;     //实拣数量
    private TextView tvAllBoxQty;   //已装箱量
    private TextView tvBoxQty;      //本箱已装
    private TextView tvUnit;        //单位
    private TextView tvPlatTip;     //印版扫描界面提示信息
    private EditText etAmount;      //数量
    private Button btDetail;        //操作明细
    private Button btCommit;        //提交
    private Button btNoBox;         //无箱
    private TextView[] allTextViews; //保存所有的TextView包括EditText方便处理
    private View matnrLayout;   //物料信息面板


    private View platLayout;    //印版信息面板(印版物料需要扫描印版信息)
    private ListView lvPlat;    //印版信息ListView
    private PackPlatAdapter platAdapter;
    private boolean plateFlag;      //标记物料是否为印版，印版操作方式和其他物料不同
    //private List<String> platList;

    private String packNo;
    private String boxNo;
    private String matnr;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_pack;
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
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        tvPack = (TextView) findViewById(R.id.pack_no);
        tvDestination = (TextView) findViewById(R.id.pack_destination);
        tvBoxNo = (TextView) findViewById(R.id.pack_boxno);
        tvMatnr = (TextView) findViewById(R.id.pack_matnr);
        tvMatnrName = (TextView) findViewById(R.id.pack_matnr_name);
        tvShouldQty = (TextView) findViewById(R.id.pack_should_qty);
        tvRealQty = (TextView) findViewById(R.id.pack_real_qty);
        tvAllBoxQty = (TextView) findViewById(R.id.pack_all_box_qty);
        tvBoxQty = (TextView) findViewById(R.id.pack_box_qty);
        etAmount = (EditText) findViewById(R.id.pack_amount);
        tvUnit = (TextView) findViewById(R.id.pack_unit);
        tvPlatTip = (TextView) findViewById(R.id.pack_plat_tip);
        matnrLayout = this.findViewById(R.id.pack_matnr_layout);
        platLayout = this.findViewById(R.id.pack_platinf_layout);
        btDetail = (Button) findViewById(R.id.pack_action_detail);
        btCommit = (Button) findViewById(R.id.pack_action_submit);
        btNoBox = (Button) findViewById(R.id.pack_action_nobox);
        btDetail.setOnClickListener(this);
        btCommit.setOnClickListener(this);
        btNoBox.setOnClickListener(this);
        allTextViews = new TextView[11];
        allTextViews[0] = tvPack;
        allTextViews[1] = tvDestination;
        allTextViews[2] = tvBoxNo;
        allTextViews[3] = tvMatnr;
        allTextViews[4] = tvMatnrName;
        allTextViews[5] = tvShouldQty;
        allTextViews[6] = tvRealQty;
        allTextViews[7] = tvAllBoxQty;
        allTextViews[8] = tvBoxQty;
        allTextViews[9] = tvUnit;
        allTextViews[10] = etAmount;

        platAdapter = new PackPlatAdapter(this);
        lvPlat = (ListView) findViewById(R.id.pack_plat_list);
        lvPlat.setAdapter(platAdapter);
        this.gotoStep(STEP_PACK);
        this.setTitle("装箱");

        this.bindLayoutDoubleClick(new int[]{R.id.step_layout_1,
                                    R.id.step_layout_2, R.id.step_layout_3},
                                    new int[]{STEP_PACK, STEP_BOX, STEP_MATNR});
    }

    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        Map<String, String> params = new HashMap<>();
        if (this.operationStep == STEP_PACK) {
            //装箱_验证拣货单是否完成
            params.put("WareHouseNo", this.getUserProfile().getWarehouseNo());
            params.put("PickOrderNo", value);
            this.request(IFace.PACK_CHECK_ORDER, params, CommonResponse.class, String.format("正在验证拣货单%s...", value));
        } else if (this.operationStep == STEP_BOX) {
            //装箱_验证箱号
//            this.boxNo = value;
//            this.tvBoxNo.setText(this.boxNo);
//            this.gotoStep(STEP_MATNR);
            params.put("WarehouseNo", this.getWmsApplication().getUserProfile().getWarehouseNo());
            params.put("BoxNo",value);
            this.request(IFace.PACK_CHECK_BOX,params,CommonResponse.class,String.format("正在验证箱号%s...",value));

        } else if (this.operationStep == STEP_MATNR) {
            //装箱_验证物料号
            params.put("PickOrderNo", this.packNo);
            params.put("BoxNo", this.boxNo);
            params.put("Matnr", value);
            this.request(IFace.PACK_CHECK_MATNR, params, PackBoxDetailResponse.class, String.format("正在获取物料%s...", value));
        } else if (this.operationStep == STEP_PLAT) {
            //装箱_输入印版
            this.platAdapter.add(value);
            this.platAdapter.notifyDataSetChanged();
            this.lvPlat.setSelection(this.platAdapter.getCount() - 1);
        }
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
        String iface = response.getIface();
        Map<String, String> params = response.getRequestParams();
        if (IFace.PACK_CHECK_ORDER.equals(iface)) {
            //装箱_验证拣货单是否完成返回
            CommonResponse data = (CommonResponse) response.getData();
            this.packNo = params.get("PickOrderNo");
            this.tvPack.setText(this.packNo);
            this.tvDestination.setText(data.getShippingAddress());
            this.gotoStep(STEP_BOX);
        } else if (IFace.PACK_CHECK_BOX.equals(iface)) {
            //装箱_验证箱号
            this.boxNo = params.get("BoxNo");
            this.tvBoxNo.setText(this.boxNo);
            this.gotoStep(STEP_MATNR);
        } else if (IFace.PACK_CHECK_MATNR.equals(iface)) {
            //装箱_验证物料号返回
            PackBoxDetailResponse detailResponse = (PackBoxDetailResponse) response.getData();
            List<PackBoxDetail> detailList = detailResponse.getBoxDetail();
            if (detailList == null || detailList.isEmpty()) {
                this.showMessage("找不到物料%s", params.get("Matnr"));
                return;
            }
            this.matnr = params.get("Matnr");
            PackBoxDetail detail = detailList.get(0);
            this.tvMatnr.setText(detail.getMatnr());
            this.tvMatnrName.setText(detail.getMatnrName());
            this.tvUnit.setText(detail.getPkgUnit());
            this.tvAllBoxQty.setText(detail.getAllBoxQty());
            this.tvBoxQty.setText(detail.getBoxQty());
            this.tvRealQty.setText(detail.getRealQty());
            this.tvShouldQty.setText(detail.getShouldQty());
            plateFlag = "true".equals(detail.getPlateFlag());
            if (plateFlag) {
                //如果是印版转到扫描印版界面
                this.gotoStep(STEP_PLAT);
            } else {
                this.gotoStep(STEP_AMOUNT);
            }
        } else if (IFace.PACK_COMMIT_QTY.equals(iface)) {
            //装箱_提交物料数量
            CommonResponse res = (CommonResponse) response.getData();
            if ("true".equals(res.getIsCompletedFlag())) {
                //已完成
                //this.showMessage("所有物料已装箱");
                this.gotoStep(STEP_FINISH);
                this.showCompleteDialog("所有物料已装箱");
            } else {
               // this.showMessage("提交成功，请扫描下一个箱号");
               // this.gotoStep(STEP_BOX);
                this.gotoStep(STEP_MATNR);
                this.showCompleteDialog("提交成功,请扫描下一个物料号");
            }
        } else if (IFace.PACK_COMMIT_QTY_SNPS.equals(iface)) {
            //装箱_提交印版信息成功
           // this.showMessage("提交成功，请扫描下一个箱号");
            this.platAdapter.clear();
            this.plateFlag = false;
            matnrLayout.setVisibility(View.VISIBLE);
            platLayout.setVisibility(View.GONE);
            //this.gotoStep(STEP_BOX);

            CommonResponse data = (CommonResponse) response.getData();
            if(data.isCompleted()){
                this.gotoStep(STEP_PACK);
                packNo=null;
                boxNo=null;
                matnr=null;
                this.showCompleteDialog("装箱完成,请扫描下一个拣货单号");
                return;
            }
            this.gotoStep(STEP_MATNR);
            //this.showCompleteDialog("提交成功,请扫描下一个物料号");
        } else if (IFace.PACK_GET_DETAIL.equals(iface)) {
            List<PackBoxDetail> detailList = ((PackBoxDetailResponse) response.getData()).getBoxDetail();
            if (detailList == null || detailList.size() == 0) {
                this.showMessage("没有装箱明细");
                return;
            }
            Intent intent = new Intent();
            intent.setClass(PackActivity.this, PackDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) detailList);
            bundle.putString("pickNo", this.packNo);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        switch (v.getId()) {
            case R.id.pack_action_submit:
                submitAction();
                break;
            case R.id.pack_action_detail:
                showDetailAction();
                break;
            case R.id.pack_action_nobox:
                noBoxAction();
                break;
            default:
                break;
        }
    }

    private void submitAction() {
        Map<String, String> params = new HashMap<>();
        List<String>platList=this.platAdapter.getPlatData();
        params.put("PickOrderNo", this.packNo);
        params.put("BoxNo", this.boxNo);
        params.put("Matnr", this.matnr);
        params.put("UserName", this.getUserProfile().getUid());
        params.put("BoxQty",String.valueOf(platList.size()));
        if (false == this.plateFlag) {
            String amount = this.etAmount.getText().toString();
            if (AppUtil.strNull(amount)) {
                this.showMessage("请输入数量");
                return;
            }
            params.put("BoxQty", amount);
            this.request(IFace.PACK_COMMIT_QTY, params, CommonResponse.class, "正在提交数据...");
        } else {
            StringBuffer buf = new StringBuffer("{\"PlateSns\":[");
            for (int i = 0; i < platList.size(); ++i) {
                if (i != 0) {
                    buf.append(",");
                }
                buf.append(String.format("{\"PlateSn\":\"%s\"}", platList.get(i)));
            }
            buf.append("]}");
            params.put("Platesns", buf.toString());
            this.request(IFace.PACK_COMMIT_QTY_SNPS, params, CommonResponse.class, "正在提交数据...");
        }
    }

    public void showDetailAction() {
        Map<String, String> params = new HashMap<>();
        params.put("PickOrderNo", this.packNo);
        this.request(IFace.PACK_GET_DETAIL, params, PackBoxDetailResponse.class, "正确查询信息...");
    }

    private void noBoxAction() {
        String ware=this.getWmsApplication().getUserProfile().getWarehouseNo();
        if("HD01  HD02 HD03 HD05".contains(ware)){
            this.showMessage("装箱箱号不能为空");
            return;
        }
        if (this.operationStep == STEP_BOX) {
            this.gotoStep(STEP_MATNR);
        }
    }

    private void gotoStep(int step) {
        if (step != STEP_PLAT) {
            matnrLayout.setVisibility(View.VISIBLE);
            platLayout.setVisibility(View.GONE);
        }
        this.operationStep = step;
        if (step != STEP_FINISH && step != STEP_PLAT) {
            this.goToTimeLine(step);
        }
        if (this.operationStep == STEP_PACK) {
            this.focusStepLayout(R.id.step_layout_1);
            this.resetTextView(allTextViews);
            packNo=null;
            boxNo=null;
            matnr=null;
            this.plateFlag = false;
        } else if (this.operationStep == STEP_BOX) {
            this.focusStepLayout(R.id.step_layout_2);
            this.resetTextView(allTextViews, 2, allTextViews.length - 2);
            boxNo=null;
            matnr=null;
        } else if (this.operationStep == STEP_MATNR) {
            this.focusStepLayout(R.id.step_layout_3);
            this.resetTextView(allTextViews, 3, allTextViews.length - 3);
            matnr=null;
        } else if (this.operationStep == STEP_AMOUNT) {
            this.focusStepLayout(R.id.step_layout_4);
            this.resetTextView(this.etAmount);
        } else if (this.operationStep == STEP_FINISH) {
            this.focusStepLayout(R.id.step_layout_1);
            this.resetTextView(allTextViews);
        } else if (this.operationStep == STEP_PLAT) {
            //跳到印版输入界面
            this.plateFlag = true;
            matnrLayout.setVisibility(View.GONE);
            platLayout.setVisibility(View.VISIBLE);
            tvPlatTip.setText(String.format("请扫描印版序列号 物料编码:%s 应装数量:%s 已装数量:%s",
                    this.matnr, this.tvShouldQty.getText(), this.tvBoxQty.getText()));
            this.platAdapter.clear();
        }
    }

    @Override
    protected void cancelAction() {
        if (this.operationStep == STEP_PACK) {
            return;
        }
        if (this.operationStep == STEP_PLAT) {
//            this.platAdapter.clear();
//            this.platList.clear();
//            this.plateFlag = false;
            this.gotoStep(STEP_AMOUNT);
            this.etAmount.setText(String.valueOf(this.platAdapter.getCount()));
            return;
        }
        this.gotoStep(this.operationStep - 1);
    }

    @Override
    protected int getCurrentOperationStep(){
        return this.operationStep;
    }

}
