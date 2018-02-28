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
import com.smec.wms.android.bean.BindBoxDetail;
import com.smec.wms.android.bean.BindBoxDetailResponse;
import com.smec.wms.android.bean.CheckRDCFGSPutawayMatnrResponse;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.PutawayDetail;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.bean.UnbindBox;
import com.smec.wms.android.bean.UnbindBoxResponse;
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
public class BoxBindActivity extends BaseScanActivity {
    public static final int STEP_BOX = 0;
    public static final int STEP_GG = 1;
    public static final int STEP_AMOUNT = 2;


    private int operationStep = STEP_BOX;
    private ScanPageInformation baseComponents;

    //views
    private TextView tvBox;
    private EditText etGg;
    private TextView tvRemark;
    private EditText etWeight;
    private EditText etLength;
    private EditText etWidth;
    private EditText etHeight;
    private Button btDetail;
    private Button btGg;
    private Button btCommit;

    private TextView[] allTextViews;

    private String boxNo;
    private String standNo;
    private String length;
    private String height;
    private String width;
    private String weight;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_box_bind;
    }

    @Override
    protected ScanPageInformation getPageComponentInf() {
        if (baseComponents == null) {
            baseComponents = new ScanPageInformation(this);
            baseComponents.addTimeLine("扫描箱号");
            baseComponents.addTimeLine("扫描规格号");
            baseComponents.addTimeLine("输入数量");
        }
        return baseComponents;
    }

    private TextView.OnEditorActionListener keyListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                Map<String, String> params = new HashMap<>();
                params.put("BoxNo", boxNo);
                params.put("BoxStandardNo", etGg.getText().toString());
                request(IFace.BIND_CHECK_BOX_STANDARD, params, BindBoxDetailResponse.class, "正在验证箱子规格..");
                return true;
            }
            return false;
        }
    };

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        this.tvBox = (TextView) findViewById(R.id.bind_box_no);
        this.etGg = (EditText) findViewById(R.id.bind_stand);
        this.tvRemark = (TextView) findViewById(R.id.bind_remark);
        this.etWeight = (EditText) findViewById(R.id.bind_weight);
        this.etLength = (EditText) findViewById(R.id.bind_length);
        this.etHeight = (EditText) findViewById(R.id.bind_height);
        this.etWidth = (EditText) findViewById(R.id.bind_width);
        this.btDetail = (Button) findViewById(R.id.bind_detail);
        this.btCommit = (Button) findViewById(R.id.bind_commit);
        this.btGg = (Button) findViewById(R.id.bind_gg);
        //  this.etGg.setOnKeyListener(keyListener);
        this.etGg.setOnEditorActionListener(keyListener);
        allTextViews = new TextView[6];
        allTextViews[0] = this.tvBox;
        allTextViews[1] = this.etGg;
        allTextViews[2] = this.etLength;
        allTextViews[3] = this.etWidth;
        allTextViews[4] = this.etHeight;
        allTextViews[5] = this.etWeight;

        this.btDetail.setOnClickListener(this);
        this.btCommit.setOnClickListener(this);
        this.btGg.setOnClickListener(this);
        this.gotoStep(STEP_BOX);

        this.bindLayoutDoubleClick(new int[]{R.id.step_layout_1,
                        R.id.step_layout_2},
                new int[]{STEP_BOX, STEP_GG});
    }

    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        Map<String, String> params = new HashMap<>();
        if (this.operationStep == STEP_BOX) {
            this.tvBox.setText(value);
            this.boxNo = value;
            this.gotoStep(STEP_GG);
        }
//        else if (this.operationStep == STEP_GG) {
//            //验证箱子规格
//            params.put("BoxNo", this.boxNo);
//            params.put("BoxStandardNo", value);
//            this.request(IFace.BIND_CHECK_BOX_STANDARD, params, BindBoxDetailResponse.class, "正在验证箱子规格..");
//        }
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
        String iface = response.getIface();
        Map<String, String> params = response.getRequestParams();
        if (IFace.BIND_CHECK_BOX_STANDARD.equals(iface)) {
            //验证箱子规格
            BindBoxDetailResponse result = (BindBoxDetailResponse) response.getData();
            List<BindBoxDetail> boxDetailList = result.getBoxStandardDetail();
            if (boxDetailList == null || boxDetailList.isEmpty()) {
                this.showMessage("该箱号下找不到指定规格");
                return;
            }
            this.standNo = params.get("BoxStandardNo");
            BindBoxDetail detail = boxDetailList.get(0);
            this.etGg.setText(this.standNo);
            this.etWidth.setText(detail.getWidth());
            this.etHeight.setText(detail.getHeight());
            this.etLength.setText(detail.getLength());
            this.tvRemark.setText(detail.getRemark());
            this.gotoStep(STEP_AMOUNT);
        } else if (IFace.PACK_RELATION_BOX.equals(iface)) {
            //提交箱子相关信息
            CommonResponse result = (CommonResponse) response.getData();
            if (result.isCompleted()) {
                //完成
                this.showMessage("箱子%s已全部绑定", this.boxNo);
            }
            this.gotoStep(STEP_BOX);
        } else if (IFace.PACK_QUERY_UNBIND.equals(iface)) {
            List<UnbindBox> detailList = ((UnbindBoxResponse) response.getData()).getBoxNoList();
            if (detailList == null || detailList.size() == 0) {
                this.showMessage("没有绑定箱子");
                return;
            }
            Intent intent = new Intent();
            intent.setClass(BoxBindActivity.this, BoxBindUnbindListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) detailList);
            bundle.putString("boxNo", this.boxNo);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        if (v.getId() == R.id.bind_detail) {
            showDetailAction();
        } else if (v.getId() == R.id.bind_commit) {
            commitAction();
        } else if (v.getId() == R.id.bind_gg) {
            customeStandAction();
        }
    }

    private void showDetailAction() {
        if (AppUtil.strNull(this.boxNo)) {
            showMessage("请先扫描箱号");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("BoxNo", this.boxNo);
        this.request(IFace.PACK_QUERY_UNBIND, params, UnbindBoxResponse.class, "正在请求数据...");
    }

    private void commitAction() {
        if (AppUtil.strNull(this.boxNo)) {
            this.showMessage("请扫描箱号");
            return;
        }
        this.width = this.etWidth.getText().toString();
        this.height = this.etHeight.getText().toString();
        this.length = this.etLength.getText().toString();
        this.weight = this.etWeight.getText().toString();
        if (AppUtil.strNull(this.length) || AppUtil.strNull(this.width) || AppUtil.strNull(this.height)) {
            this.showMessage("箱子长度、宽度、高度不能为空");
            return;
        }
        if (AppUtil.strNull(this.weight)) {
            this.showMessage("请输入箱子重量");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("BoxNo", this.boxNo);
        params.put("BoxStandardNo", this.standNo);
        params.put("Length", this.length);
        params.put("Width", this.width);
        params.put("Height", this.height);
        params.put("Weight", this.weight);
        params.put("UserName", this.getUserProfile().getUid());
        this.request(IFace.PACK_RELATION_BOX, params, CommonResponse.class, "正在提交数据....");
    }

    private void customeStandAction() {
        this.etLength.setText(null);
        this.etHeight.setText(null);
        this.etWidth.setText(null);
        this.etGg.setText(null);
        this.etLength.requestFocus();
        AppUtil.openIMM(this, this.etLength);
    }

    private void gotoStep(int step) {
        this.operationStep = step;
        this.goToTimeLine(step);
        if (this.operationStep == STEP_BOX) {
            this.boxNo = null;
            this.standNo = null;
            this.tvBox.setText(null);
            this.resetTextView(allTextViews);
            this.tvRemark.setText(null);
            this.focusStepLayout(R.id.step_layout_1);
        } else if (this.operationStep == STEP_GG) {
            this.resetTextView(allTextViews, 1, allTextViews.length - 1);
            this.tvRemark.setText(null);
            this.focusStepLayout(R.id.step_layout_2);
        } else if (this.operationStep == STEP_AMOUNT) {
            this.focusStepLayout(R.id.step_layout_3);
            this.resetTextView(etWeight);
            this.etWeight.requestFocus();
        }
    }

    @Override
    protected void cancelAction() {
        if (this.operationStep == STEP_BOX) {
            return;
        }
        this.gotoStep(this.operationStep - 1);
    }

    @Override
    protected int getCurrentOperationStep() {
        return this.operationStep;
    }

}
