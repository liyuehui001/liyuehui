package com.smec.wms.android.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.SimpleListItemAdapter;
import com.smec.wms.android.bean.UnbindBox;

import java.util.List;

/**
 * Created by Adobe on 2016/2/18.
 */
public class BoxBindUnbindListActivity extends BasePageActivity {
    private List<UnbindBox> detailList;
    private SimpleListItemAdapter detailAdapter;
    private ListView detailListView;
    private TextView tvBoxNo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_box_bind_detail;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        detailList = (List<UnbindBox>) this.getIntent().getExtras().getSerializable("data");
        detailListView = (ListView) this.findViewById(R.id.bind_detail_listview);
        tvBoxNo = (TextView) findViewById(R.id.list_detail_title_1);
        detailAdapter = new SimpleListItemAdapter(this);
        for (int i = 0; i < detailList.size(); ++i) {
            detailAdapter.add(String.format("%d %s", i + 1, detailList.get(i).getBoxNo()));
        }
        detailListView.setAdapter(detailAdapter);
        this.enableBackAction();
        this.setTitle("未绑定明细");
        String boxNo = this.getIntent().getExtras().getString("boxNo");
        this.tvBoxNo.setText(boxNo);
    }
}
