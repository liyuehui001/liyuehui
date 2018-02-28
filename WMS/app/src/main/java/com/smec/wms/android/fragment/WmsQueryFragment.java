package com.smec.wms.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.android.activity.CPZXActivity;
import com.smec.wms.android.activity.ImageQueryActivity;
import com.smec.wms.android.activity.MatnrQueryActivity;
import com.smec.wms.android.activity.MatnrStockQueryActivity;
import com.smec.wms.android.activity.ShelfQueryActivity;
import com.smec.wms.android.activity.StockQueryActivity;
import com.smec.wms.android.adapter.ListRouterAdapter;
import com.smec.wms.android.bean.ListRouterBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WmsQueryFragment extends Fragment {

    private RecyclerView recyclerView ;

    public WmsQueryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wms_query, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        initData();
        return view ;
    }

    public void initData() {
        ArrayList<ListRouterBean> routerBeenList = new ArrayList<>();
        ListRouterBean routerBean1 = new ListRouterBean(R.mipmap.kuchunqingdan_icon,"库存清单查询", StockQueryActivity.class);
        ListRouterBean routerBean2 = new ListRouterBean(R.mipmap.wuliaojiage_icon,"物料价格及信息查询", MatnrQueryActivity.class);
        ListRouterBean routerBean3 = new ListRouterBean(R.mipmap.huoweikuchun_icon,"货位库存查询", ShelfQueryActivity.class);
        ListRouterBean routerBean4 = new ListRouterBean(R.mipmap.wuliaokuchun_icon,"物料库存查询", MatnrStockQueryActivity.class);
        ListRouterBean routerBean5 = new ListRouterBean(R.mipmap.tizhongtupian_icon,"常用备件图片查询", ImageQueryActivity.class);
        ListRouterBean routerBean6 = new ListRouterBean(R.mipmap.chengpinzhuangxiangqingdan_icon,"成品装箱清单查询", CPZXActivity.class);
//        ListRouterBean routerBean7 = new ListRouterBean(R.mipmap.wuliaochaxun_icon,"物料查询",OtherQueryMatnrActivity.class);

        routerBeenList.add(routerBean1);
        routerBeenList.add(routerBean2);
        routerBeenList.add(routerBean3);
        routerBeenList.add(routerBean4);
        routerBeenList.add(routerBean5);
        routerBeenList.add(routerBean6);
//        routerBeenList.add(routerBean7);

        recyclerView.setAdapter(new ListRouterAdapter(this.getActivity(),routerBeenList));
    }

}
