package com.smec.wms.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.MatnrStock;
import com.smec.wms.android.bean.MatnrStockResponse;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/29.
 */
public class MoveActivity extends BaseScanActivity{
    public static final int STEP_OUT_SHELF=0;
    public static final int STEP_MATNR=1;
    public static final int STEP_IN_STEP=2;
    public static final int STEP_AMOUNT=3;
    public static final int STEP_FINISH=4;

    //views
    private TextView tvOutShelf;
    private TextView tvMatnr;
    private TextView tvInShelf;
    private TextView tvMatnrName;
    private TextView tvUnit;
    private TextView tvStockQty;
    private EditText etAmount;
    private Button btCommit;
    private TextView[]allTextViews;

    private String outShelf;
    private String matnr;
    private String inShelf;
    private String stockQty;

    private int operationStep=STEP_OUT_SHELF;
    private ScanPageInformation baseComponents;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_move;
    }

    @Override
    protected ScanPageInformation getPageComponentInf() {
        if (baseComponents == null) {
            baseComponents = new ScanPageInformation(this);
            baseComponents.addTimeLine("移出货位");
            baseComponents.addTimeLine("物料号");
            baseComponents.addTimeLine("移入货位");
            baseComponents.addTimeLine("移动数量");
        }
        return baseComponents;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        tvOutShelf=(TextView)this.findViewById(R.id.move_out_shelf);
        tvMatnr=(TextView)this.findViewById(R.id.move_matnr);
        tvMatnrName=(TextView)this.findViewById(R.id.move_matnrname);
        tvUnit=(TextView)this.findViewById(R.id.move_unit);
        tvStockQty=(TextView)this.findViewById(R.id.move_stock_qty);
        tvInShelf=(TextView)this.findViewById(R.id.move_in_shelf);
        etAmount=(EditText)this.findViewById(R.id.move_amount);
        btCommit=(Button)this.findViewById(R.id.move_commint_action);
        btCommit.setOnClickListener(this);

        allTextViews=new TextView[7];
        allTextViews[0]=tvOutShelf;
        allTextViews[1]=tvMatnr;
        allTextViews[2]=tvMatnrName;
        allTextViews[3]=tvUnit;
        allTextViews[4]=tvStockQty;
        allTextViews[5]=tvInShelf;
        allTextViews[6]=etAmount;
        this.gotoStep(STEP_OUT_SHELF);

        this.bindLayoutDoubleClick(new int[]{R.id.step_layout_1,
                        R.id.step_layout_2, R.id.step_layout_3},
                new int[]{STEP_OUT_SHELF, STEP_MATNR, STEP_IN_STEP});
    }

    @Override
    protected void scanSuccess(String value) {
        super.scanSuccess(value);
        Map<String,String> params=new HashMap<>();
        if(this.operationStep==STEP_OUT_SHELF){
            //移库_验证货位号
            params.put("WarehouseNo",this.getUserProfile().getWarehouseNo());
            params.put("ShelfSpaceNo",value);
            this.request(IFace.COMMON_CHECK_SHELF_NO,params, ResponseData.class,"正在验证货位"+value+"...");
        }else if(this.operationStep==STEP_MATNR){
            //请求物料信息
            params.put("WarehouseNo",this.getUserProfile().getWarehouseNo());
            params.put("Matnr",value);
            this.request(IFace.COMMON_QUERY_MATNR,params,MatnrStockResponse.class,"正在请求物料"+value+"...");
        }else if(this.operationStep==STEP_IN_STEP){
            this.inShelf=value;
            this.tvInShelf.setText(this.inShelf);
            this.gotoStep(STEP_AMOUNT);
        }
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
        String iface=response.getIface();
        Map<String,String>params=response.getRequestParams();
        if(IFace.COMMON_CHECK_SHELF_NO.equals(iface)){
            this.outShelf=params.get("ShelfSpaceNo");
            this.tvOutShelf.setText(this.outShelf);
            this.gotoStep(STEP_MATNR);
        }else if(IFace.COMMON_QUERY_MATNR.equals(iface)){
            MatnrStockResponse result=(MatnrStockResponse)response.getData();
            MatnrStock matnrDetail=result.getMantrnStockByShelf(this.outShelf);
            if(matnrDetail==null){
                this.showMessage("找不到物料"+params.get("Matnr"));
                return;
            }
            this.matnr=params.get("Matnr");
            this.tvMatnr.setText(this.matnr);
            this.tvMatnrName.setText(matnrDetail.getMatnrName());
            this.tvUnit.setText(matnrDetail.getPkgUnit());
            this.tvStockQty.setText(matnrDetail.getStockQty());
            this.gotoStep(STEP_IN_STEP);
        }else if(IFace.MOVE_COMMIT_QTY.equals(iface)){
            this.showMessage("移库完成");
            this.gotoStep(STEP_OUT_SHELF);
        }
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        if(v.getId()==R.id.move_commint_action){
            commitAction();
        }
    }

    private void commitAction(){
        Map<String,String>params=new HashMap<>();
        params.put("WarehouseNo",this.getUserProfile().getWarehouseNo());
        params.put("FromShelfSpace",this.outShelf);
        params.put("ToShelfSpace",this.inShelf);
        params.put("matnr",this.matnr);
        params.put("Qty",this.etAmount.getText().toString());
        params.put("UserName",this.getUserProfile().getUid());
        this.request(IFace.MOVE_COMMIT_QTY,params,ResponseData.class,"正在提交数据...");
    }

    private void gotoStep(int step){
        this.operationStep=step;
        if(this.operationStep==STEP_FINISH){
            return;
        }
        this.goToTimeLine(step);
        if(this.operationStep==STEP_OUT_SHELF){
            this.outShelf=null;
            this.matnr=null;
            this.resetTextView(allTextViews);
            this.focusStepLayout(R.id.step_layout_1);
        }else if(this.operationStep==STEP_MATNR){
            this.matnr=null;
            this.resetTextView(allTextViews,1,allTextViews.length-1);
            this.focusStepLayout(R.id.step_layout_2);
        }else if(this.operationStep==STEP_IN_STEP){
            this.resetTextView(allTextViews,5,allTextViews.length-5);
            this.focusStepLayout(R.id.step_layout_3);
        }else if(this.operationStep==STEP_AMOUNT){
            this.etAmount.setText(null);
            this.focusStepLayout(R.id.step_layout_4);
        }
    }

    @Override
    protected void cancelAction() {
        if(this.operationStep==STEP_OUT_SHELF){
            return;
        }
        this.gotoStep(this.operationStep-1);
    }

    @Override
    protected int getCurrentOperationStep(){
        return this.operationStep;
    }

}
