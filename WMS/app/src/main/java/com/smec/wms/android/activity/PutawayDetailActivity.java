package com.smec.wms.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.PutawayDetailAdapter;
import com.smec.wms.android.bean.PutawayDetail;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Adobe on 2016/1/19.
 */
public class PutawayDetailActivity extends BasePageActivity implements View.OnClickListener {
    private List<PutawayDetail> detailList;
    private PutawayDetailAdapter detailAdapter;
    private ListView detailListView;
    private TextView tvRkNo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_putaway_detail;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        detailList = (List<PutawayDetail>) this.getIntent().getExtras().getSerializable("data");
        detailListView = (ListView) this.findViewById(R.id.putaway_detail_listview);
        tvRkNo=(TextView)findViewById(R.id.list_detail_title_1);
        detailAdapter = new PutawayDetailAdapter(this, detailList);
        detailListView.setAdapter(detailAdapter);
        this.enableBackAction();
        this.setTitle("未上架明细");
        String rk=this.getIntent().getExtras().getString("rkNo");
        this.tvRkNo.setText(rk);
    }
}
