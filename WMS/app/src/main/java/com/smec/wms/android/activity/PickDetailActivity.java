package com.smec.wms.android.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.PickDetailAdapter;
import com.smec.wms.android.bean.PickDetail;

import java.util.List;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PickDetailActivity extends BasePageActivity {
    private List<PickDetail> detailList;
    private ListView lvDetail;
    private PickDetailAdapter pickAdapter;
    private TextView tvRk;
    private String pickNo;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_pick_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        tvRk=(TextView)this.findViewById(R.id.list_detail_title_1);
        this.setTitle("拣货操作明细");
        detailList=(List<PickDetail>)this.getIntent().getExtras().getSerializable("data");
        pickAdapter=new PickDetailAdapter(this,detailList);
        lvDetail=(ListView)this.findViewById(R.id.pick_detail_list);
        lvDetail.setAdapter(pickAdapter);

        this.pickNo=this.getIntent().getExtras().getString("pickNo");
        this.tvRk.setText(this.pickNo);
        this.enableBackAction();
    }
}
