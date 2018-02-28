package com.version.smec.wms.module.LogisticsDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.smec.wms.R;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.databinding.ActivityLogisiticsDetailBinding;
import com.smec.wms.databinding.ListItemLogisiticsMatnrsBinding;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormDto;
import com.version.smec.wms.module.LogisticsDetail.adapter.LogisticsDetailMatnrsAdapter;
import com.version.smec.wms.module.LogisticsDetail.bean.LogisiticsDetailDto;
import com.version.smec.wms.module.LogisticsDetail.bean.LogisitiicsDetailMatnrModule;
import com.version.smec.wms.module.LogisticsDetail.bean.LogisitiicsDetailModule;
import com.version.smec.wms.module.LogisticsList.LogisticsContract;
import com.version.smec.wms.utils.FullyLinearLayoutManager;
import com.version.smec.wms.utils.ToastUtils;
import com.version.smec.wms.widget.TopBarLayout;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by 小黑 on 2017/8/24.
 */
public class LogisiticsDetailActivity extends WmsBaseActivity<LogisiticsDetailPresenter> implements LogisiticsDetailContract{
    public final static String GETLOGISITICS_FAIL="GETLOGISITICS_FAIL";
    public final static String GETLOGISITICS_SUCCESS="GETLOGISITICS_SUCCESS";
    private LogisitiicsDetailModule ldModule;
    private ActivityLogisiticsDetailBinding aldBinding;
    private ArrayList<LogisitiicsDetailMatnrModule> LDMMLists;
    private String outboundOrderNo;
    private String boxNo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        aldBinding = DataBindingUtil.setContentView(this, R.layout.activity_logisitics_detail);

        _init();

    }

    private void _init() {

        outboundOrderNo = getIntent().getStringExtra("outboundOrderNo");
        boxNo = getIntent().getStringExtra("boxNo");
        ldModule = new LogisitiicsDetailModule();
        LDMMLists = new ArrayList<>();

        aldBinding.topLogisitics.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                LogisiticsDetailActivity.this.finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }

            @Override
            public void setOnRight2ClickListener() {

            }
        });

        WeakHashMap<String,String> map = new WeakHashMap<>();

        this.getLogisicsDetail(map);
    }

    @Override
    public LogisiticsDetailPresenter getPresenter() {
        return new LogisiticsDetailPresenter(this);
    }

    @Override
    public void getLogisicsDetail(WeakHashMap<String, String> map) {
        map.put("outboundOrderNo",outboundOrderNo);
        map.put("boxNo",boxNo);
        mPresenter.getLogisicsDetail(map);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETLOGISITICS_SUCCESS)
            }
    )
    public void getSuccess(LogisiticsDetailDto response){
        ldModule = response.getData();
        for (int i = 0; i < response.getData().getMatnr().size(); i++) {
            LDMMLists.add(response.getData().getMatnr().get(i));
        }

        _initView();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(GETLOGISITICS_FAIL)
            }
    )
    public void getFail(LogisiticsDetailDto response){
        ToastUtils.showToast(response.getUserMsg());
    }

    private void _initView() {

        aldBinding.setLogisiticsdetailInfo(ldModule);
        aldBinding.rcvMatnrs.setLayoutManager(
                new FullyLinearLayoutManager(LogisiticsDetailActivity.this));
        aldBinding.rcvMatnrs.setAdapter(
                new LogisticsDetailMatnrsAdapter(LDMMLists,this));

        aldBinding.topLogisitics.getTvTextLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogisiticsDetailActivity.this.finish();
            }
        });


    }
}
