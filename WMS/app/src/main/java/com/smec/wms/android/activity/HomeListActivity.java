package com.smec.wms.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smec.wms.R;
import com.smec.wms.android.adapter.ListRouterAdapter;
import com.smec.wms.android.bean.ListRouterBean;

import java.util.ArrayList;

public class HomeListActivity extends BasePageActivity {

    public static String HOME_LIST_TITLE_PARAM = "HOME_LIST_TITLE_PARAM" ;
    public static String HOME_LIST_PARAM = "HOME_LIST_PARAM" ;

    private String homeListTitle ;

    private ArrayList<ListRouterBean> routerBeanList ;

    private RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        this.enableBackAction();
        Intent intent = this.getIntent() ;
        this.homeListTitle = intent.getStringExtra(HOME_LIST_TITLE_PARAM);
        if (homeListTitle == null || homeListTitle.equals("")){
            homeListTitle = "功能列表";
        }

        this.routerBeanList = (ArrayList<ListRouterBean>) intent.getSerializableExtra(HOME_LIST_PARAM);
        if (this.routerBeanList == null){
            this.routerBeanList = new ArrayList<>();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.setTitle(homeListTitle);

        recyclerView.setAdapter(new ListRouterAdapter(this,routerBeanList));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home_list;
    }


}
