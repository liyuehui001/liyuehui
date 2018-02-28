package com.smec.wms.android.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.PackDetailAdapter;
import com.smec.wms.android.adapter.PickDetailAdapter;
import com.smec.wms.android.bean.PackBoxDetail;
import com.smec.wms.android.bean.PickDetail;

import java.util.List;

/**
 * Created by Adobe on 2016/1/31.
 */
public class PackDetailActivity extends BasePageActivity {

    private List<PackBoxDetail> detailList;
    private ListView lvDetail;
    private PackDetailAdapter packAdapter;
    private TextView tvRk;
    private String pickNo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pack_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        tvRk = (TextView) this.findViewById(R.id.list_detail_title_1);
        this.setTitle("装箱");
        detailList = (List<PackBoxDetail>) this.getIntent().getExtras().getSerializable("data");
        packAdapter = new PackDetailAdapter(this, detailList);
        lvDetail = (ListView) this.findViewById(R.id.pack_detail_list);
        lvDetail.setAdapter(packAdapter);

        this.pickNo = this.getIntent().getExtras().getString("pickNo");
        this.tvRk.setText(this.pickNo);
        this.enableBackAction();
    }

}
