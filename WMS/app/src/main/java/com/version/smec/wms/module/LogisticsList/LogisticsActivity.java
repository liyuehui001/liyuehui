package com.version.smec.wms.module.LogisticsList;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.smec.wms.R;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.databinding.ActivityLogisticsBinding;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.module.LogisticsList.adapter.LogisiticsItemAdapter;
import com.version.smec.wms.module.LogisticsList.bean.LogisticsDto;
import com.version.smec.wms.module.LogisticsList.bean.LogisticsModule;
import com.version.smec.wms.module.Requirements.bean.RequirementDataModuleDto;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/23.
 */
public class LogisticsActivity extends WmsBaseActivity<LogisticsPresenter> implements LogisticsContract{

    private LogisticsDto logisticsDto;
    private ArrayList<LogisticsModule> arrayList;
    public static final String GETLOGISTICS_FAIL="GETLOGISTICS_FAIL";
    public static final String GETLOGISTICS_SUCCESS="GETLOGISTICS_SUCCESS";
    private ActivityLogisticsBinding albBind;
    private String receiptNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albBind = DataBindingUtil.setContentView(this, R.layout.activity_logistics);

        _init();

        WeakHashMap<String,String> map = new WeakHashMap<>();
        this.getLogisticsList(map);
    }

    private void _init() {
        arrayList = new ArrayList<>();
        logisticsDto = new LogisticsDto();
        receiptNo = getIntent().getStringExtra("receiptNo");

        albBind.cvTodoListDetailTitle.getTvTextLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogisticsActivity.this.finish();
            }
        });
    }

    @Override
    public LogisticsPresenter getPresenter() {
        return new LogisticsPresenter(this);
    }

    @Override
    public void getLogisticsList(WeakHashMap<String, String> map) {
        map.put("receiptNo",receiptNo);
        mPresenter.getLogisticsList(map);
    }
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETLOGISTICS_FAIL)
            }
    )
    public void searchFail(LogisticsDto response){
        ToastUtil.show(LogisticsActivity.this,response.getUserMsg(), Toast.LENGTH_LONG);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETLOGISTICS_SUCCESS)
            }
    )
    public void searchSuccess(LogisticsDto response){
        logisticsDto = response;
        for (int i = 0; i < response.getData().size(); i++) {
            arrayList.add(response.getData().get(i));
        }
        _initView();
        albBind.invalidateAll();
    }

    private void _initView() {

        LinearLayoutManager manage = new LinearLayoutManager(this);
        albBind.rcvLogisticsList.setLayoutManager(manage);
        albBind.rcvLogisticsList.setAdapter(new LogisiticsItemAdapter(arrayList,this));


    }


}
