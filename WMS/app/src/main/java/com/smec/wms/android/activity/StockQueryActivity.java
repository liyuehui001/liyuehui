package com.smec.wms.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.smec.wms.R;
import com.smec.wms.android.adapter.StockQueryAdapter;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.Stock;
import com.smec.wms.android.bean.StockListResponse;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adobe on 2016/3/10.
 */
public class StockQueryActivity extends BasePageActivity implements ScanSuccessListener,StockQueryAdapter.StockQueryAdapterListener {
    private EditText etMatnr;
    private TextView btQuery;
    private TextView btAdvanced;
    private ListView lvStock;
    private String matnr;
    private String matnrName;
    private String wareHouseNo;
    private String wareHouseName;
    private int curPageNum = 1;
    private final static int PAGE_SIZE = 5;

    private AlertDialog advanceDialog;

    private StockQueryAdapter stockQueryAdapter;
    private EditText etAdMatnr;
    private EditText etAdMatnrName;
    private EditText etAdWarehouse;
    private EditText etAdWarehouseName;

    private PhotoView photoView ;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        stockQueryAdapter = new StockQueryAdapter(this,this);
        this.initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_stock_query;
    }

    private void initView() {
        etMatnr = (EditText) findViewById(R.id.stock_query_matnr);
        btQuery = (TextView) findViewById(R.id.stock_query_query);
        btAdvanced = (TextView) findViewById(R.id.stock_query_advance);
        lvStock = (ListView) findViewById(R.id.stock_query_lv);
        btQuery.setOnClickListener(this);
        btAdvanced.setOnClickListener(this);
        lvStock.setAdapter(this.stockQueryAdapter);

        photoView = (PhotoView) findViewById(R.id.zoomBigImage);

      //  etMatnr.setOnEditorActionListener(keyListener);
        this.enableBackAction();
        this.setTitle("库存清单查询");
        WmsApplication.getInstance().setCurrentScanActivity(this);

     //   Camera.open();
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        int id = v.getId();
        if (R.id.stock_query_query == id) {
            //查询物料
            this.queryStockHandle();
        } else if (R.id.stock_query_advance == id) {
            //其他条件查询
            View view = this.getLayoutInflater().inflate(R.layout.layout_stock_query_advance, null);
            etAdMatnr = (EditText) view.findViewById(R.id.stock_query_matnr);
            etAdMatnrName = (EditText) view.findViewById(R.id.stock_query_matnrname);
            etAdWarehouse = (EditText) view.findViewById(R.id.stock_query_warehouse);
            etAdWarehouseName = (EditText) view.findViewById(R.id.stock_query_warehousename);
            etAdMatnr.setText(this.etMatnr.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("高级查询");
            builder.setPositiveButton("查询", handAdvanceDialogListener);
            builder.setNeutralButton("取消", handAdvanceDialogListener);
            builder.setNegativeButton("重置", handAdvanceDialogListener);
            builder.setView(view);
            builder.setCancelable(false);
            this.advanceDialog = builder.create();
            this.advanceDialog.show();
        }
    }

    private void queryStockHandle(){
        this.matnr = this.etMatnr.getText().toString();
        this.matnrName = "";
        this.wareHouseName = "";
        this.wareHouseNo = "";
        stockQueryAdapter.clear();
        this.curPageNum=1;
        queryStock();
    }

    private void queryStock() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("WarehouseNo", this.wareHouseNo);
        params.put("WarehouseName", this.wareHouseName);
        params.put("Matnr", this.matnr);
        params.put("MatnrName", this.matnrName);
        params.put("UserName", this.getWmsApplication().getUserProfile().getUid());
        params.put("PageSize", String.valueOf(PAGE_SIZE));
        params.put("PageNum", String.valueOf(curPageNum));
        this.request(IFace.STOCK_QUERY_SERVICE, params, StockListResponse.class, "正在请求数据....");
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        String iface = response.getIface();
        Map<String, String> params = response.getRequestParams();

        if(IFace.STOCK_QUERY_SERVICE.equals(iface)){
            AppUtil.hideIMM(this, etMatnr);
            this.matnr = params.get("Matnr");
            this.matnrName = params.get("MatnrName");
            this.wareHouseNo = params.get("WarehouseNo");
            this.wareHouseName = params.get("WarehouseName");
            StockListResponse stockList = (StockListResponse) response.getData();
            this.etMatnr.setText("");
            if (this.curPageNum == 1 && (stockList.getStocks() == null || stockList.getStocks().size() == 0)) {
                this.showMessage("没有查到物料" + matnr + "库存信息");
                return;
            }
            //this.curPageNum++;
            stockQueryAdapter.addAll(stockList);
            if (stockList.getStocks() == null || stockList.getStocks().size() < PAGE_SIZE) {
                stockQueryAdapter.setIsEnd(true);
            }
        }else if (IFace.MATNR_QUERY_PRICE.equals(iface)) {
            String position = params.get("postion");
            CommonResponse res = (CommonResponse) response.getData();
            String price = res.getPrice();
            this.stockQueryAdapter.setPrice(Integer.parseInt(position), price);
        }

    }


    /**
     * 请求下一页
     */
    public void queryNextPage() {
        ++curPageNum;
        this.queryStock();
    }

    private DialogInterface.OnClickListener handAdvanceDialogListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    advancedQuery();
                    closeDialog();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    resetQuery();
                    break;
                default:
                    closeDialog();
                    break;
            }
        }
    };

    private void advancedQuery() {
        this.curPageNum = 1;
        this.stockQueryAdapter.clear();
        this.matnr = this.etAdMatnr.getText().toString();
        this.matnrName = this.etAdMatnrName.getText().toString();
        this.wareHouseNo = this.etAdWarehouse.getText().toString();
        this.wareHouseName = this.etAdWarehouseName.getText().toString();
        this.etMatnr.setText(this.matnr);
        this.queryStock();
    }

    private void resetQuery() {
        etAdMatnr.setText("");
        etAdMatnrName.setText("");
        etAdWarehouse.setText("");
        etAdWarehouseName.setText("");
        try {
            // 不关闭
            Field field = this.advanceDialog
                    .getClass()
                    .getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(advanceDialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDialog() {
        try {
            // 不关闭
            Field field = this.advanceDialog
                    .getClass()
                    .getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(advanceDialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        advanceDialog.dismiss();
    }

    private TextView.OnEditorActionListener keyListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if(event.getAction()==KeyEvent.ACTION_DOWN) {
                    matnr = etMatnr.getText().toString().trim();
                    matnrName = "";
                    wareHouseName = "";
                    wareHouseNo = "";
                    stockQueryAdapter.clear();
                    curPageNum = 1;
                    queryStock();
                }
                return true;
            }
            return false;
        }
    };


    @Override
    public void scanBarcodeSuccess(String text) {
        this.etMatnr.setText(text);
        this.queryStockHandle();
    }

    @Override
    public void imageButtonClick(Stock stock) {
        if (stock.getDownloadUrl() == null || "".equals(stock.getDownloadUrl())){
            ToastUtil.show(this,"该物料没有图片");
            return;
        }
        Glide.with(this)
                .load(Uri.parse(stock.getDownloadUrl()))
                .fitCenter()
                .crossFade()
                .dontAnimate()
                .into(photoView);
        photoView.setVisibility(View.VISIBLE);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void lookPriceClick(int position,String matnr) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("arg0", matnr);
        params.put("postion", String.valueOf(position));
        this.request(IFace.MATNR_QUERY_PRICE, params, CommonResponse.class, String.format("正在请求物料%s价格...", matnr));
    }

    @Override
    public boolean backExecute() {
        if (photoView.getVisibility() == View.VISIBLE) {
            photoView.setVisibility(View.GONE);
            return false ;
        }
        return super.backExecute();
    }

    @Override
    public void onBackPressed() {
        this.clickActionHandler(findViewById(R.id.common_back));
    }
}

