package com.smec.wms.android.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.CommonResponse;
import com.smec.wms.android.bean.DeliveryData;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adobe on 2016/7/15.
 */
public class DeliveryActivity extends BasePageActivity implements ScanSuccessListener {
    private EditText etBoxNo;       //箱号
    private TextView tvOutboundNo;  //出库单
    private TextView tvAddress;     //发运地址
    private Spinner spTransferType; //发运方式
    private Spinner spTransferVendor;   //运输单位
    private EditText etPrice;       //报价
    private EditText etDeliveryCompany; //物流公司
    private EditText etDeliveryNo;  //物流单号
    private Button btCommit;        //提交
    private Button btGetPrice;      //获取报价

    private static String[]DELIVERY_TYPE=new String[]{"","汽运","快递","空运","铁路"};
    private static String[]DELIVERY_TYPE_CODE=new String[]{"","CAR","EXPRESS","AIR","TRAIN"};
    private static String[]DELIVERY_VENDOR=new String[]{"","闵昆运输","江鸣运输"};
    private static String[]DELIVERY_VENDOR_CODE=new String[]{"","MK001","JM001"};


    @Override
    protected int getContentViewId() {
        return R.layout.activity_delivery_query;
    }

    private void initView(){
        etBoxNo=(EditText)this.findViewById(R.id.delivery_boxno);
        tvOutboundNo=(TextView)this.findViewById(R.id.delivery_outboundno);
        tvAddress=(TextView)this.findViewById(R.id.delivery_address);
        spTransferType=(Spinner)this.findViewById(R.id.delivery_transfertype);
        spTransferVendor=(Spinner)this.findViewById(R.id.delivery_transfervendor);
        etPrice=(EditText)this.findViewById(R.id.delivery_price);
        etDeliveryCompany=(EditText)this.findViewById(R.id.delivery_company);
        etDeliveryNo=(EditText)this.findViewById(R.id.delivery_no);
        btCommit=(Button)this.findViewById(R.id.delivery_commit);
        btGetPrice=(Button)this.findViewById(R.id.delivery_getprice);
        this.etBoxNo.setOnEditorActionListener(keyListener);
        btCommit.setOnClickListener(this);
        btGetPrice.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DELIVERY_TYPE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTransferType.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DELIVERY_VENDOR);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTransferVendor.setAdapter(adapter2);
        this.getWmsApplication().setCurrentScanActivity(this);
    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        String iface=response.getIface();
        if(IFace.DELIVERY_GET_DELIVERY_DATA.equals(iface)){
            //查询出库单信息
            DeliveryData data=(DeliveryData)response.getData();
            this.tvOutboundNo.setText(data.getOutboundOrderNo());
            this.tvAddress.setText(data.getDeliveryAddress());
            this.etPrice.requestFocus();
        }else if(IFace.DELIVERY_COMMIT.equals(iface)){
            //提交信息
            this.reset();
            CommonResponse res=(CommonResponse)response.getData();
            if(res.isCompleted()){
                this.showCompleteDialog("所有箱号操作完成");
            }else{
                this.showMessage("提交成功");
            }
        }else if(IFace.DELIVERY_QUERY_PRICE.equals(iface)){
            //查询报价
            DeliveryData data=(DeliveryData)response.getData();
            this.etPrice.setText(data.getQuotedPrice());
            this.etDeliveryCompany.requestFocus();
        }
    }

    private void deliveryCommit(){
        String boxNo=this.etBoxNo.getText().toString();
        String transferType=DELIVERY_TYPE_CODE[this.spTransferType.getSelectedItemPosition()];
        String transferVendor=DELIVERY_VENDOR_CODE[this.spTransferVendor.getSelectedItemPosition()];
        String price=this.etPrice.getText().toString();
        String company=this.etDeliveryCompany.getText().toString();
        String deliveryNo=this.etDeliveryNo.getText().toString();
        if(AppUtil.strNull(boxNo)){
            this.showMessage("箱号不能为空");
            return;
        }
        if(AppUtil.strNull(transferType)){
            this.showMessage("发运方式不能为空");
            return;
        }
        if(AppUtil.strNull(price)){
            this.showMessage("报价不能为空");
            return;
        }
        if(AppUtil.strNull(company)){
            this.showMessage("物流公司不能为空");
            return;
        }
        if(AppUtil.strNull(deliveryNo)){
            this.showMessage("物流单号不能为空");
            return;
        }
        Map<String,String>params=new HashMap<String,String>();
        params.put("BoxNo",boxNo);
        params.put("UserName",this.getUserProfile().getUid());
        params.put("ShippingWayCode",transferType);
        params.put("QuotedPrice",price);
        params.put("LogisticsCompany",company);
        params.put("LogisticsNo",deliveryNo);

//        params.put("UserName","s90002270");
        params.put("Remark",transferVendor);
        this.request(IFace.DELIVERY_COMMIT,params, CommonResponse.class,"正在提交发运信息....");
    }

    private void deliveryGetData(){
        String boxNo=this.etBoxNo.getText().toString();
        Map<String,String> params=new HashMap<String,String>();
        params.put("BoxNo",boxNo);
        this.request(IFace.DELIVERY_GET_DELIVERY_DATA,params,DeliveryData.class,"正在查询出库单信息...");
    }

    private void deliveryQueryPrice(){
        String boxNo=this.etBoxNo.getText().toString();
        Map<String,String> params=new HashMap<String,String>();
        params.put("input",boxNo);
        this.request(IFace.DELIVERY_QUERY_PRICE,params,DeliveryData.class,"正在查询报价...");
    }

    @Override
    public void clickActionHandler(View v) {
        super.clickActionHandler(v);
        if(v.getId()==R.id.delivery_commit){
            deliveryCommit();
        }else if(v.getId()==R.id.delivery_getprice){
            deliveryQueryPrice();
        }
    }

    private void reset(){
        etBoxNo.setText(null);
        tvOutboundNo.setText(null);
        tvAddress.setText(null);
        spTransferType.setSelection(0);
        etPrice.setText(null);
        etDeliveryCompany.setText(null);
        etDeliveryNo.setText(null);
        this.etBoxNo.requestFocus();
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState){
        this.initView();
        this.setTitle("物流管理");
        this.enableBackAction();
    };

    private TextView.OnEditorActionListener keyListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                deliveryGetData();
                return true;
            }
            return false;
        }
    };

    @Override
    public void scanBarcodeSuccess(String text) {
        if(this.etBoxNo.isFocused()){
            this.etBoxNo.setText(text);
            this.deliveryGetData();
        }else if(this.etPrice.isFocused()){
            this.etPrice.setText(text);
        }else if(this.etDeliveryCompany.isFocused()){
            this.etDeliveryCompany.setText(text);
            this.etDeliveryNo.requestFocus();
        }else if(this.etDeliveryNo.isFocused()){
            this.etDeliveryNo.setText(text);
        }
    }

    @Override
    public void afterErrorHandle(String iface,String errorMsg){
        if(this.etBoxNo.isFocused()){
            this.etBoxNo.setText(null);
        }
    }
}
