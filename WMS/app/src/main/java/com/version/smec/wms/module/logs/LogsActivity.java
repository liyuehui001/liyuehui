package com.version.smec.wms.module.logs;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.smec.wms.R;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.databinding.ActivityLogsRequirmentBinding;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.module.Requirements.bean.RequirementDataModuleDto;
import com.version.smec.wms.module.logs.adapter.LogsAdapter;
import com.version.smec.wms.module.logs.bean.LogsDto;
import com.version.smec.wms.module.logs.bean.LogsModule;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.widget.TopBarLayout;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/28.
 */
public class LogsActivity extends WmsBaseActivity<LogsPresenter> implements LogsContract{
    public static final String GETLOGS_SUCCESS="get_logs_success";
    public static final String GETLOGS_FAIL = "get_logs_fail";

    private ArrayList<LogsModule> logsList;
    private ActivityLogsRequirmentBinding alrbind;
    private String receiptNo,receiptType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alrbind = DataBindingUtil.setContentView(this, R.layout.activity_logs_requirment);

        _initArrayList();
    }

    private void _initArrayList() {
        logsList = new ArrayList<>();
        Intent mintent = getIntent();
        receiptNo = mintent.getStringExtra("receiptNo");
        receiptType = mintent.getStringExtra("receiptType");

        alrbind.logsTop.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                LogsActivity.this.finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {

            }
        });

        WeakHashMap<String,String> map = new WeakHashMap<>();
        this.getLogs(map);
    }

    @Override
    public void getLogs(WeakHashMap<String, String> map) {

        map.put("receiptType",receiptType);
        map.put("receiptNo",receiptNo);

        mPresenter.getLogs(map);
    }

    @Override
    public LogsPresenter getPresenter() {
        return new LogsPresenter(LogsActivity.this);
    }


    /**
     * 接收日志成功
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETLOGS_SUCCESS)
            }
    )
    public void searchSuccess(LogsDto response){
        if (CommonUtils.notEmpty(response.getData())) {
            for (int i = 0; i < response.getData().size(); i++) {
                logsList.add(response.getData().get(i));
            }
            _initView();
        }
    }

    /**
     * 接收日志成功
     * @param response
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETLOGS_FAIL)
            }
    )
    public void getLogFail(LogsDto response){
        ToastUtil.show(LogsActivity.this,response.getUserMsg(), Toast.LENGTH_SHORT);
    }


    private void _initView() {
        LinearLayoutManager llmanager = new LinearLayoutManager(this);
        alrbind.rcvLogs.setLayoutManager(llmanager);
        alrbind.rcvLogs.setAdapter(new LogsAdapter(logsList,this));

    }


}
