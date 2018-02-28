package com.smec.wms.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.MatnrStockQueryAdapter;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.MatnrStockResponse;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adobe on 2016/3/10.
 */
public class MatnrStockQueryActivity extends BasePageActivity implements ScanSuccessListener {
    private EditText etMatnrStock;
    private TextView btQuery;
    private ListView lvMatnrStock;
    private String matnrStock;
    private MatnrStockQueryAdapter matnrStockQueryAdapter;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        matnrStockQueryAdapter = new MatnrStockQueryAdapter(this);
        this.initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_matnr_stock_query;
    }

    private void initView() {
        etMatnrStock = (EditText) findViewById(R.id.matnr_query_matnr);
        btQuery = (TextView) findViewById(R.id.matnr_query_query);
        lvMatnrStock = (ListView) findViewById(R.id.matnr_query_lv);
        btQuery.setOnClickListener(this);
        lvMatnrStock.setAdapter(this.matnrStockQueryAdapter);
        this.enableBackAction();
        this.setTitle("物料库存查询");
        WmsApplication.getInstance().setCurrentScanActivity(this);
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        int id = v.getId();
        if (R.id.matnr_query_query == id) {
            //查询物料
            this.queryMatnrStockHandle();
        }
    }

    private void queryMatnrStockHandle(){
        this.matnrStock = this.etMatnrStock.getText().toString();
        matnrStockQueryAdapter.clear();
        queryMatnrStock();
    }

    private void queryMatnrStock() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("WarehouseNo", this.getWmsApplication().getUserProfile().getWarehouseNo());
        params.put("Matnr", this.matnrStock);
        this.request(IFace.COMMON_QUERY_MATNR, params, MatnrStockResponse.class, "正在请求数据....");
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        Map<String, String> params = response.getRequestParams();
        AppUtil.hideIMM(this, etMatnrStock);
        this.matnrStock = params.get("Matnr");
        MatnrStockResponse matnrStockList = (MatnrStockResponse) response.getData();
        this.etMatnrStock.setText("");
        if (matnrStockList.getStockDetail() == null || matnrStockList.getStockDetail().size() == 0) {
            this.showMessage("没有查到物料" + matnrStock + "库存信息");
            return;
        }
        //this.curPageNum++;
        matnrStockQueryAdapter.addAll(matnrStockList);
    }


    @Override
    public void scanBarcodeSuccess(String text) {
        this.etMatnrStock.setText(text);
        this.queryMatnrStockHandle();
    }
}

