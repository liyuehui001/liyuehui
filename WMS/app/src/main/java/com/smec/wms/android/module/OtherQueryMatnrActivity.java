package com.smec.wms.android.module;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.BasePageActivity;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adobe on 2016/3/10.
 */
public class OtherQueryMatnrActivity extends BasePageActivity {
    private EditText etMatnr;

    private EditText etAdMatnr;
    private EditText etAdMatnrName;
    private EditText etAdWarehouse;
    private EditText etAdWarehouseName;

    private Dialog advanceDialog ;
    private TextView normalQueryText ;
    private TextView advanceQueryText ;

    private ListView listView ;

    private List<OtherQueryMatnrResponse.OhterQueryMatnrEntity> ohterQueryMatnrEntities ;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_other_query_matnr;
    }

    private void initView() {
        etMatnr = (EditText) findViewById(R.id.query_matnr_dwgno);
        normalQueryText = (TextView) findViewById(R.id.matnr_query_query);
        advanceQueryText = (TextView) findViewById(R.id.matnr_query_advance);

        etMatnr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals(editable.toString().toUpperCase()) && editable.toString().equals(editable.toString().trim())) {
                    return;
                }
                String text = editable.toString();
                text = text.toUpperCase();
                text = text.trim();
                etMatnr.setText(text);
                etMatnr.setSelection(text.length());
            }
        });

        listView = (ListView) findViewById(R.id.stock_query_lv);
//        etMatnr.setOnEditorActionListener(keyListener);
        normalQueryText.setOnClickListener(this);
        advanceQueryText.setOnClickListener(this);
        this.enableBackAction();
        this.setTitle("物料查询");
        //   Camera.open();

        listView.setAdapter(otherQueryAdapter);
    }

    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        int id = v.getId();
        if (R.id.matnr_query_query == id) {
            //查询物料
            this.queryStockHandle();
        } else if (R.id.matnr_query_advance == id) {
//            //其他条件查询
            View view = this.getLayoutInflater().inflate(R.layout.layout_other_matnr_query, null);
            etAdMatnr = (EditText) view.findViewById(R.id.matnr_query_dwgno);
            etAdMatnrName = (EditText) view.findViewById(R.id.matnr_query_gcode);
            etAdWarehouse = (EditText) view.findViewById(R.id.matnr_query_lcode);
            etAdWarehouseName = (EditText) view.findViewById(R.id.matnr_query_remark);
            etAdMatnr.setText(this.etMatnr.getText().toString());

            etAdMatnr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().equals(editable.toString().toUpperCase()) && editable.toString().equals(editable.toString().trim())) {
                        return;
                    }
                    String text = editable.toString();
                    text = text.toUpperCase();
                    text = text.trim();
                    etAdMatnr.setText(text);
                    etAdMatnr.setSelection(text.length());
                }
            });

            etAdWarehouse.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().equals(editable.toString().toUpperCase()) && editable.toString().equals(editable.toString().trim())) {
                        return;
                    }
                    String text = editable.toString();
                    text = text.toUpperCase();
                    text = text.trim();
                    etAdWarehouse.setText(text);
                    etAdWarehouse.setSelection(text.length());
                }
            });

            etAdWarehouseName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().equals(editable.toString().toUpperCase()) && editable.toString().equals(editable.toString().trim())) {
                        return;
                    }
                    String text = editable.toString();
                    text = text.toUpperCase();
                    text = text.trim();
                    etAdWarehouseName.setText(text);
                    etAdWarehouseName.setSelection(text.length());
                }
            });

            etAdMatnrName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().equals(editable.toString().toUpperCase()) && editable.toString().equals(editable.toString().trim())) {
                        return;
                    }
                    String text = editable.toString();
                    text = text.toUpperCase();
                    text = text.trim();
                    etAdMatnrName.setText(text);
                    etAdMatnrName.setSelection(text.length());
                }
            });

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
        Map<String,String> map = new HashMap<>();
        map.put("DWGNO",etMatnr.getText().toString());
        queryStock(map);
    }

    private void queryStock(Map<String,String> params) {
        if (TextUtils.isEmpty(params.get("DWGNO"))) {
            ToastUtil.show(this,"查询必须输入图号");
            return;
        }
        this.request(IFace.MATNR_OTHER_QUERY_SERVICE, params, OtherQueryMatnrResponse.class, "正在请求数据....");
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        String iface = response.getIface();
        Map<String, String> params = response.getRequestParams();
        if (IFace.MATNR_OTHER_QUERY_SERVICE.equals(iface)) {
            OtherQueryMatnrResponse otherQueryMatnrResponse = (OtherQueryMatnrResponse) response.getData();
            if ("SUCCESS".equals(otherQueryMatnrResponse.getCode())) {
                AppUtil.hideIMM(OtherQueryMatnrActivity.this,etMatnr);
                if (otherQueryMatnrResponse.getData() == null || otherQueryMatnrResponse.getData().size() == 0) {
                    ToastUtil.show(OtherQueryMatnrActivity.this,"没有查询到数据");
                }
                this.ohterQueryMatnrEntities = otherQueryMatnrResponse.getData();
                otherQueryAdapter.notifyDataSetChanged();
            }
        }
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
        Map<String,String> map = new HashMap<>();
        map.put("DWGNO",etAdMatnr.getText().toString());
        map.put("GCODE",etAdMatnrName.getText().toString());
        map.put("LCODE",etAdWarehouse.getText().toString());
        map.put("REMARK",etAdWarehouseName.getText().toString());

        this.queryStock(map);
    }

    private void resetQuery() {

        try {
            etAdMatnr.setText("");
            etAdMatnrName.setText("");
            etAdWarehouse.setText("");
            etAdWarehouseName.setText("");
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
//                    queryStock();
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        this.clickActionHandler(findViewById(R.id.common_back));
    }

    public static class OtherMatnrViewHolder {
        private TextView matnrTextView ;
        private TextView dwgnoTextView ;
        private TextView gcodeTextView ;
        private TextView lcodeTextView ;
        private TextView remarkTextView ;
    }

    private BaseAdapter otherQueryAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return ohterQueryMatnrEntities == null ? 0 : ohterQueryMatnrEntities.size();
        }

        @Override
        public OtherQueryMatnrResponse.OhterQueryMatnrEntity getItem(int i) {
            return ohterQueryMatnrEntities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OtherMatnrViewHolder otherMatnrViewHolder ;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.list_item_other_matnr_query, null);
                OtherMatnrViewHolder holder = new OtherMatnrViewHolder();
                holder.matnrTextView = (TextView) view.findViewById(R.id.matnr_query_matnr);
                holder.gcodeTextView = (TextView) view.findViewById(R.id.matnr_query_gcode);
                holder.lcodeTextView = (TextView) view.findViewById(R.id.matnr_query_lcode);
                holder.remarkTextView = (TextView) view.findViewById(R.id.matnr_query_remark);

                otherMatnrViewHolder = holder;
                view.setTag(otherMatnrViewHolder);
            } else {
                otherMatnrViewHolder = (OtherMatnrViewHolder) view.getTag();
            }

            otherMatnrViewHolder.matnrTextView.setText(getItem(i).getMatnr());
            otherMatnrViewHolder.gcodeTextView.setText(getItem(i).getGcode());
            otherMatnrViewHolder.lcodeTextView.setText(getItem(i).getLcode());
            otherMatnrViewHolder.remarkTextView.setText(getItem(i).getRemark());

            return view;
        }
    };

}

