package com.smec.wms.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.CPZXAdapter;
import com.smec.wms.android.bean.CpzxItem;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adobe on 2016/7/15.
 */
public class CPZXActivity extends BasePageActivity implements ScanSuccessListener {
    private EditText etContract;
    private EditText etEle;
    private ListView dataList;
    private Button btQuery;
    private Button btReset;
    private Button btFilter;

    private CPZXAdapter adapter;
    private AlertDialog advanceDialog;

    private EditText etBoxNo;
    private EditText etName;
    private EditText etClass;
    private EditText etMatnr;
    private EditText etPic;

    private String fBoxNo;
    private String fName;
    private String fClass;
    private String fMatnr;
    private String fPic;

    @Override
    protected void requestSuccess(ServerResponse response) {
        String iface = response.getIface();
        if (IFace.CPZX_QUERY.equals(iface)) {
            CpzxItem[] items = (CpzxItem[]) response.getData();
            this.adapter.setData(Arrays.asList(items));
            this.resetFilter();
        }
    }


    private void query() {
        String contract = this.etContract.getText().toString();
        String ele = this.etEle.getText().toString();
        if (AppUtil.strNull(contract)) {
            this.showMessage("合同号不能为空");
            return;
        }
        contract=contract.trim().toUpperCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("arg0", contract);
        params.put("arg1", ele);
        this.request(IFace.CPZX_QUERY, params, CpzxItem[].class, "正在查询....");
    }

    private void reset() {
        this.etContract.setText(null);
        this.etEle.setText(null);
        this.etContract.requestFocus();
    }

    private void doFilter() {
        CpzxItem filter = new CpzxItem();
        this.fBoxNo=etBoxNo.getText().toString().trim().toUpperCase();
        this.fClass=etClass.getText().toString().trim().toUpperCase();
        this.fMatnr=etMatnr.getText().toString().trim().toUpperCase();
        this.fName=etName.getText().toString().trim().toUpperCase();
        this.fPic=etPic.getText().toString().trim().toUpperCase();
        filter.setPackingNo(fBoxNo);
        filter.setClassName(fClass);
        filter.setForeFather(fMatnr);
        filter.setTitle(fName);
        filter.setGwgno(fPic);
        filter.setIdnrk(fMatnr);
        this.adapter.filter(filter);
    }

    private void filter() {
        View view = this.getLayoutInflater().inflate(R.layout.layout_cpzx_filter, null);

        etBoxNo = (EditText) view.findViewById(R.id.cpzx_filter_box);
        etName = (EditText) view.findViewById(R.id.cpzx_filter_name);
        etClass = (EditText) view.findViewById(R.id.cpzx_filter_class);
        etMatnr = (EditText) view.findViewById(R.id.cpzx_filter_matnr);
        etPic = (EditText) view.findViewById(R.id.cpzx_filter_pic);
        this.etBoxNo.setText(this.fBoxNo);
        this.etName.setText(this.fName);
        this.etClass.setText(this.fClass);
        this.etMatnr.setText(this.fMatnr);
        this.etPic.setText(this.fPic);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("过滤");
        builder.setPositiveButton("确定", handAdvanceDialogListener);
        builder.setNeutralButton("取消", handAdvanceDialogListener);
      //  builder.setNegativeButton("重置", handAdvanceDialogListener);
        builder.setView(view);
        builder.setCancelable(false);
        this.advanceDialog = builder.create();
        this.advanceDialog.show();
    }

    private void resetFilter() {
        this.fBoxNo=null;
        this.fName=null;
        this.fMatnr=null;
        this.fClass=null;
        this.fPic=null;
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

    private DialogInterface.OnClickListener handAdvanceDialogListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    doFilter();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    resetFilter();
                    break;
                default:
                    closeDialog();
                    break;
            }
        }
    };

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        if (v.getId() == R.id.cpzx_query) {
            query();
        } else if (v.getId() == R.id.cpzx_reset) {
            reset();
        } else if (v.getId() == R.id.cpzx_filter) {
            filter();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_cpzx;
    }


    private void initView() {
        this.etContract = (EditText) this.findViewById(R.id.cpzx_contract);
        this.etEle = (EditText) this.findViewById(R.id.cpzx_ele);
        this.btQuery = (Button) this.findViewById(R.id.cpzx_query);
        this.btReset = (Button) this.findViewById(R.id.cpzx_reset);
        this.btFilter = (Button) this.findViewById(R.id.cpzx_filter);
        this.dataList = (ListView) this.findViewById(R.id.cpzx_list);
        this.btQuery.setOnClickListener(this);
        this.btFilter.setOnClickListener(this);
        this.btReset.setOnClickListener(this);
        adapter = new CPZXAdapter(this);
        this.dataList.setAdapter(adapter);
        // this.etContract.setText("15N4V10-O47");
        // this.etEle.setText("01");
        this.getWmsApplication().setCurrentScanActivity(this);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        this.initView();
        setTitle("成品装箱清单查询");
        enableBackAction();
    }

    @Override
    public void scanBarcodeSuccess(String text) {
        if (advanceDialog != null && advanceDialog.isShowing()) {
            return;
        }
        if (this.etContract.isFocused()) {
            this.etContract.setText(text);
            this.etEle.requestFocus();
        } else if (this.etEle.isFocused()) {
            this.etEle.setText(text);
            this.query();
        }
    }
}
