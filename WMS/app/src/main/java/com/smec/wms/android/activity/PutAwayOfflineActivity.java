package com.smec.wms.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.CheckRDCFGSPutawayMatnrResponse;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.PutawayDetail;
import com.smec.wms.android.bean.PutawayDetailOffLine;
import com.smec.wms.android.bean.PutawayDetailOfflineResponse;
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
public class PutAwayOfflineActivity extends BaseScanActivity {
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
    private Button btOK;            //确认(输入完数量之后本地提交)

    private boolean isNoBox = false;  //是否有箱子
    private PutawayDetailOfflineResponse putawayDetail;


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
        btOK = (Button) this.findViewById(R.id.putaway_action_ok);
        btDetail.setOnClickListener(this);
        btGenerate.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        btOK.setOnClickListener(this);
        etAmount.setOnEditorActionListener(keyListener);

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
            baseComponents.addTimeLine("输入数量");
        }
        return baseComponents;
    }


    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        if (this.operationStep == STEP_RK) {
            //RDC分公司收货清点_验证单号获取入库单详细信息
            Map<String, String> params = new HashMap();
            params.put("InboundOrderNo", value);
            params.put("UserName", this.getUserProfile().getUid());
            this.request(IFace.GET_RDCFGS_INBOUND_DETAIL, params, PutawayDetailOfflineResponse.class, "正在验证入库单" + value + "...");
        } else if (this.operationStep == STEP_BOX) {
            //RDC分公司收货清点_验证箱号
            if (this.putawayDetail == null) {
                //根据箱号生成入库单
                Map<String, String> params = new HashMap();
                params.put("BoxNo", value);
                params.put("User", this.getWmsApplication().getUserProfile().getUid());
                //开始多次请求
                this.beginMulRequest();
                this.request(IFace.RF_GETORDERNOWITHBOX, params, CommonResponse.class, String.format("正在获取箱号%s入库单...", value));
                return;
            }
            boolean result = this.putawayDetail.validBox(value);
            if (result) {
                this.boxNo = value;
                this.tvBox.setText(value);
                this.gotoStep(STEP_MATNR);
            } else {
                ToastUtil.show(this, String.format("箱号 %s 不存在", value));
            }
        } else if (this.operationStep == STEP_MATNR) {
            //RDC分公司收货清点_验证物料号
            PutawayDetailOffLine detailItem = this.putawayDetail.getDetail(boxNo, value);
            if (detailItem == null) {
                ToastUtil.show(this, String.format("物料 %s 不存在", value));
            } else {
                this.shelfNo = putawayDetail.getShelfSpaceNo();
                this.matnrNo = detailItem.getMatnr();
                tvMatnr.setText(this.matnrNo);
                tvOrderQty.setText(detailItem.getShouldQty());
                tvPutawayQty.setText(detailItem.getRealQty());
                tvSuggetst.setText(detailItem.getSuggestShelfSpace());
                tvUnit.setText(detailItem.getPkgUnit());
                tvShelf.setText(putawayDetail.getShelfSpaceNo());
                this.gotoStep(STEP_AMOUNT);
            }
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
        if (IFace.GET_RDCFGS_INBOUND_DETAIL.equals(iface)) {
            //RDC分公司收货清点_验证单号
            this.rkNo = response.getRequestParams().get("InboundOrderNo");
            this.tvRk.setText(this.rkNo);
            putawayDetail = (PutawayDetailOfflineResponse) response.getData();
            this.isNoBox = putawayDetail.isNoBox();
            String flag = requestParams.get("flag");
            if (this.isNoBox) {
                this.tvBox.setText("无箱上架");
                this.boxNo = "";
                this.gotoStep(STEP_MATNR);//无箱上架,跳过箱号扫描直接扫描物料
            } else if (flag == null) {
                this.gotoStep(STEP_BOX);    //扫描入库单号当前步骤修改为扫描箱号
            }

            if (requestParams.get("flag") != null) {
                this.endMulRequest();
                boolean result = this.putawayDetail.validBox(this.boxNo);
                if (result) {
                    this.gotoStep(STEP_MATNR);
                } else {
                    ToastUtil.show(this, String.format("箱号 %s 不存在", this.boxNo));
                }
            }
        } else if (IFace.RF_QUERY_RDC_SLOTIN_DETAILS.equals(iface)) {
            //RDC上架_获取未上架明细
            List<PutawayDetail> detailList = ((CheckRDCFGSPutawayMatnrResponse) response.getData()).getPutawayDetail();
            if (detailList == null || detailList.size() == 0) {
                this.showMessage("没有明细信息");
                return;
            }
            Intent intent = new Intent();
            intent.setClass(PutAwayOfflineActivity.this, PutawayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) detailList);
            intent.putExtras(bundle);
            this.startActivity(intent);
        } else if (IFace.RF_GETORDERNOWITHBOX.equals(iface)) {
            //根据箱号生成入库单号
            this.rkNo = ((CommonResponse) response.getData()).getInboundOrderNo();
            this.boxNo = response.getRequestParams().get("BoxNo");
            this.tvRk.setText(this.rkNo);
            this.tvBox.setText(this.boxNo);
            Map<String, String> params = new HashMap();
            params.put("InboundOrderNo", this.rkNo);
            params.put("UserName", this.getUserProfile().getUid());
            params.put("flag", "");
            this.request(IFace.GET_RDCFGS_INBOUND_DETAIL, params, PutawayDetailOfflineResponse.class, "正在验证入库单" + this.rkNo + "...");
            //this.gotoStep(STEP_MATNR);
        } else if (IFace.RF_COMMIT_RDC_SLOTIN_QTY.equals(iface)) {
            String nextseq = requestParams.get("nextseq");
            if (nextseq.equals(String.valueOf(this.putawayDetail.getInboundDetail().size()))) {
                this.endMulRequest();
                this.showMessage("数据提交成功");
                this.gotoStep(STEP_RK);
            } else {
                this.commitPutawayItem(nextseq);
            }
        }

    }

    private void commitPutawayItem(String seq) {
        if(putawayDetail==null){
            this.showMessage("请先扫描入库单");
            return;
        }
        Integer index = Integer.parseInt(seq);
        List<PutawayDetailOffLine> putawayList = putawayDetail.getInboundDetail();
        Map<String, String> params = new HashMap<>();
        PutawayDetailOffLine item = putawayList.get(index);
        params.put("InboundOrderNo", item.getInboundOrderNo());
        params.put("BoxNo", item.getBoxNo());
        params.put("Matnr", item.getMatnr());
        params.put("ShelfSpaceNo", item.getSuggestShelfSpace());
        params.put("InboundQty", String.valueOf(item.getAmount()));
        params.put("UserName", this.getUserProfile().getUid());
        params.put("nextseq", String.valueOf(index + 1));
        this.request(IFace.RF_COMMIT_RDC_SLOTIN_QTY, params, CommonResponse.class,
                String.format("正在提交数据%s:%s....", item.getBoxNo(), item.getMatnr()));
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
//            case R.id.putaway_action_ok:
//                nextBoxAction();
            default:
                break;
        }
    }

    /**
     * 显示操作明细
     */
    private void showDetail() {
        if (AppUtil.strNull(rkNo)) {
            this.showMessage("请先扫描入库单");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("InboundOrderNo", this.rkNo);
        // params.put("boxNo", this.boxNo);
        params.put("detailType", "2");
        this.request(IFace.RF_QUERY_RDC_SLOTIN_DETAILS, params, CheckRDCFGSPutawayMatnrResponse.class, "正在请求数据....");
    }

    private void generateRKByBoxNoAction() {
        if (this.operationStep == STEP_RK || this.operationStep == STEP_BOX) {
            this.gotoStep(STEP_BOX);
            ToastUtil.show(this, "请扫描箱号");
        }
    }

    private void nextBoxAction() {
        this.putawayDetail.matnrPutway(this.boxNo, this.matnrNo);
        if (this.putawayDetail.isCompleted()) {
            this.gotoStep(STEP_FINISH);
        } else {
            this.gotoStep(STEP_BOX);
        }
    }

    /**
     * 提交上架信息
     */
    private void submitPutaway() {
        //提交信息
        String a = etAmount.getText().toString();
        if (!AppUtil.strNull(a)) {
            putawayDetail.setPutawayAmount(boxNo, matnrNo, a);
            gotoStep(STEP_BOX);
        }
        this.beginMulRequest();
        this.commitPutawayItem("0");
    }


    private TextView.OnEditorActionListener keyListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == 0) {
                String a = etAmount.getText().toString();
                if (AppUtil.strNull(a)) {
                    showMessage("请输入数量");
                    return true;
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                putawayDetail.setPutawayAmount(boxNo, matnrNo, a);
                if (putawayDetail.isCompleted()) {
                    showMessage("操作已完成，请提交数据");
                } else {
                    showMessage("输入下一个箱号");
                }
                gotoStep(STEP_BOX);
                return true;
            }
            return false;
        }
    };

    private void gotoStep(int step) {
        this.operationStep = step;
        if (step == STEP_RK) {
            this.focusStepLayout(R.id.step_layout_1);
            this.goToTimeLine(0);
            this.putawayDetail=null;
            this.boxNo = null;
            this.shelfNo = null;
            this.matnrNo = null;
        } else if (step == STEP_BOX) {
            this.boxNo = null;
            this.shelfNo = null;
            this.matnrNo = null;
            this.tvBox.setText(this.boxNo);
            this.tvShelf.setText(this.shelfNo);
            this.etAmount.setText(null);
            this.tvMatnr.setText(null);
            this.tvUnit.setText(null);
            this.tvOrderQty.setText(null);
            this.tvPutawayQty.setText(null);
            this.tvSuggetst.setText(null);
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
        } else if (step == STEP_AMOUNT) {
            //打开键盘
            AppUtil.openIMM(this, this.etAmount);
            this.focusStepLayout(R.id.step_layout_4);
            this.goToTimeLine(3);
        } else if (step == STEP_FINISH) {
            ToastUtil.show(this, "所有物料已上架完成");
            stepAllReset();
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
            this.gotoStep(STEP_SHELF);
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
