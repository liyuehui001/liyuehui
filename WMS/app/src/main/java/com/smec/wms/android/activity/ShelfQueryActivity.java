package com.smec.wms.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.ShelfQueryAdapter;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.ShelfListResponse;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adobe on 2016/3/10.
 */
public class ShelfQueryActivity extends BasePageActivity implements ScanSuccessListener {
    private EditText etShelf;
    private TextView btQuery;
    private ListView lvShelf;
    private String shelf;
    private ShelfQueryAdapter shelfQueryAdapter;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        shelfQueryAdapter = new ShelfQueryAdapter(this);
        this.initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shelf_query;
    }

    private void initView() {
        etShelf = (EditText) findViewById(R.id.shelf_query_shelf);
        btQuery = (TextView) findViewById(R.id.shelfquery_query);
        lvShelf = (ListView) findViewById(R.id.shelfquery_lv);
        btQuery.setOnClickListener(this);
        lvShelf.setAdapter(this.shelfQueryAdapter);
        this.enableBackAction();
        this.setTitle("货位库存查询");
        WmsApplication.getInstance().setCurrentScanActivity(this);
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        int id = v.getId();
        if (R.id.shelfquery_query == id) {
            //查询物料
            this.queryShelfHandle();
        }
    }

    private void queryShelfHandle(){
        this.shelf = this.etShelf.getText().toString();
        shelfQueryAdapter.clear();
        queryShelf();
    }

    private void queryShelf() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("WarehouseNo", this.getWmsApplication().getUserProfile().getWarehouseNo());
        params.put("ShelfSpaceNo", this.shelf);
        this.request(IFace.COMMON_QUERY_SHELF, params, ShelfListResponse.class, "正在请求数据....");
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        Map<String, String> params = response.getRequestParams();
        AppUtil.hideIMM(this, etShelf);
        this.shelf = params.get("ShelfSpaceNo");
        ShelfListResponse shelfList = (ShelfListResponse) response.getData();
        this.etShelf.setText("");
        if (shelfList.getStockDetail() == null || shelfList.getStockDetail().size() == 0) {
            this.showMessage("没有查到货位" + shelf + "库存信息");
            return;
        }
        //this.curPageNum++;
        shelfQueryAdapter.addAll(shelfList);
    }


    @Override
    public void scanBarcodeSuccess(String text) {
        this.etShelf.setText(text);
        this.queryShelfHandle();
    }
}

