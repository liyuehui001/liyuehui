package com.version.smec.wms.module.LogisticsList.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.smec.wms.R;
import com.smec.wms.databinding.ListItemLogisiticsBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.LogisticsDetail.LogisiticsDetailActivity;
import com.version.smec.wms.module.LogisticsList.bean.LogisticsModule;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/23.
 */
public class LogisiticsItemAdapter  extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {
    private ArrayList<LogisticsModule> llist;
    private Context mcontext;

    public LogisiticsItemAdapter(ArrayList<LogisticsModule> lclist,Context ctx){
        llist = new ArrayList<>();
        this.llist = lclist;
        this.mcontext = ctx;
    }

    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemLogisiticsBinding lilbind = DataBindingUtil
                .inflate(LayoutInflater.from(mcontext),
                        R.layout.list_item_logisitics,parent,false);
        WmsBaseRecycleViewHolder wbrvHolder = new WmsBaseRecycleViewHolder(lilbind.getRoot());
        wbrvHolder.setViewType(viewType);
        wbrvHolder.setBinding(lilbind);
        return wbrvHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, final int position) {
        ListItemLogisiticsBinding lilbind = (ListItemLogisiticsBinding) holder.getBinding();
        lilbind.setLogisiticsItemInfo(llist.get(position));

        lilbind.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent = new Intent(mcontext, LogisiticsDetailActivity.class);
                mintent.putExtra("outboundOrderNo",llist.get(position).getOutboundOrderNo());
                mintent.putExtra("boxNo",llist.get(position).getBoxNo());
                mcontext.startActivity(mintent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return llist.size();
    }
}
