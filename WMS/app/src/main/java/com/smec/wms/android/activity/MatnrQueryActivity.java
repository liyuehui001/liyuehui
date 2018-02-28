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
import com.smec.wms.android.adapter.MatnrQueryAdapter;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.Matnr;
import com.smec.wms.android.bean.MatnrListResponse;
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
public class MatnrQueryActivity extends BasePageActivity implements ScanSuccessListener,MatnrQueryAdapter.MatnrQueryAdapterListener {
    private EditText etMatnr;
    private TextView tvQuery;
    private TextView tvAdvanced;
    private ListView lvMatnr;
    private String matnr;
    private String matnrName;
    private int curPageNum = 1;
    private final static int PAGE_SIZE = 5;

    private AlertDialog advanceDialog;

    private MatnrQueryAdapter matnrQueryAdapter;
    private EditText etAdMatnr;
    private EditText etAdMatnrName;


    private PhotoView photoImage ;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        matnrQueryAdapter = new MatnrQueryAdapter(this,this);
        this.initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_matnr_query;
    }

    private void initView() {
        etMatnr = (EditText) findViewById(R.id.matnr_query_matnr);
        tvQuery = (TextView) findViewById(R.id.matnr_query_query);
        tvAdvanced = (TextView) findViewById(R.id.matnr_query_advance);
        lvMatnr = (ListView) findViewById(R.id.matnr_query_lv);

        photoImage = (PhotoView) findViewById(R.id.zoomBigImage);


        tvQuery.setOnClickListener(this);
        tvAdvanced.setOnClickListener(this);
        lvMatnr.setAdapter(this.matnrQueryAdapter);
     //   etMatnr.setOnEditorActionListener(keyListener);
        this.enableBackAction();
        this.setTitle("物料价格及信息查询");
        WmsApplication.getInstance().setCurrentScanActivity(this);
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        int id = v.getId();
        if (R.id.matnr_query_query == id) {
            //查询物料
            queryMatnrHandle();
        } else if (R.id.matnr_query_advance == id) {
            //其他条件查询
            View view = this.getLayoutInflater().inflate(R.layout.layout_matnr_query_advance, null);
            etAdMatnr = (EditText) view.findViewById(R.id.matnr_query_matnr);
            etAdMatnrName = (EditText) view.findViewById(R.id.matnr_query_matnrname);
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

    private void queryMatnrHandle() {
        this.matnr = this.etMatnr.getText().toString().trim();
        this.matnrName = "";
        matnrQueryAdapter.clear();
        this.curPageNum = 1;
        queryMatnr();
    }

    private void queryMatnr() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Matnr", this.matnr);
        params.put("MatnrName", this.matnrName);
        params.put("UserName", this.getWmsApplication().getUserProfile().getUid());
        params.put("PageSize", String.valueOf(PAGE_SIZE));
        params.put("PageNum", String.valueOf(curPageNum));
        this.request(IFace.MATNR_QUERY_SERVICE, params, MatnrListResponse.class, "正在请求数据....");
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        String iface = response.getIface();
        Map<String, String> params = response.getRequestParams();
        if (IFace.MATNR_QUERY_SERVICE.equals(iface)) {
            AppUtil.hideIMM(this, etMatnr);
            this.matnr = params.get("Matnr");
            this.matnrName = params.get("MatnrName");
            MatnrListResponse matnrList = (MatnrListResponse) response.getData();
            this.etMatnr.setText("");
            if (this.curPageNum == 1 && (matnrList.getMatnrs() == null || matnrList.getMatnrs().size() == 0)) {
                this.showMessage("没有查到物料号" + matnr + "数据");
                return;
            }
            matnrQueryAdapter.addAll(matnrList);
            if (matnrList.getMatnrs() == null || matnrList.getMatnrs().size() < PAGE_SIZE) {
                matnrQueryAdapter.setIsEnd(true);
            }
            //this.curPageNum++;
        } else if (IFace.MATNR_QUERY_PRICE.equals(iface)) {
            String position = params.get("postion");
            CommonResponse res = (CommonResponse) response.getData();
            String price = res.getPrice();
            this.matnrQueryAdapter.setPrice(Integer.parseInt(position), price);
        }
    }


    /**
     * 请求下一页
     */
    public void queryNextPage() {
        ++curPageNum;
        this.queryMatnr();
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

    private TextView.OnEditorActionListener keyListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    matnr = etMatnr.getText().toString().trim();
                    matnrQueryAdapter.clear();
                    curPageNum = 1;
                    queryMatnr();
                }
                return true;
            }
            return false;
        }
    };

    private void advancedQuery() {
        this.curPageNum = 1;
        this.matnrQueryAdapter.clear();
        AppUtil.hideIMM(this, etAdMatnr);
        AppUtil.hideIMM(this, etAdMatnrName);
        this.matnr = this.etAdMatnr.getText().toString();
        this.matnrName = this.etAdMatnrName.getText().toString();
        this.etMatnr.setText(this.matnr);
        this.queryMatnr();
    }

    private void resetQuery() {
        etAdMatnr.setText("");
        etAdMatnrName.setText("");
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

    public void requestPrice(String matnr, int position) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("arg0", matnr);
        params.put("postion", String.valueOf(position));
        this.request(IFace.MATNR_QUERY_PRICE, params, CommonResponse.class, String.format("正在请求物料%s价格...", matnr));
    }

    @Override
    public void scanBarcodeSuccess(String text) {
        this.etMatnr.setText(text);
        this.queryMatnrHandle();
    }

    @Override
    public void imageButtonClick(Matnr matnr) {
        if (matnr.getDownloadUrl() == null || matnr.getDownloadUrl().equals("")){
            ToastUtil.show(this,"该物料没有图片");
            return;
        }else {
            Glide.with(this)
                    .load(Uri.parse(matnr.getDownloadUrl()))
                    .fitCenter()
                    .crossFade()
                    .dontAnimate()
                    .into(photoImage);
            photoImage.setVisibility(View.VISIBLE);
            photoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoImage.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean backExecute() {
        if (photoImage.getVisibility() == View.VISIBLE) {
            photoImage.setVisibility(View.GONE);
            return false ;
        }
        return super.backExecute();
    }

    @Override
    public void onBackPressed() {
        this.clickActionHandler(findViewById(R.id.common_back));
    }
}

