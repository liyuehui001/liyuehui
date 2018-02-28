package com.version.smec.wms.module.LogisticsDetail.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.databinding.ListItemBorrowFormMatnrsBinding;
import com.smec.wms.databinding.ListItemLogisiticsMatnrsBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.LogisticsDetail.bean.LogisitiicsDetailMatnrModule;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/25.
 */
public class LogisticsDetailMatnrsAdapter extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {
    private ArrayList<LogisitiicsDetailMatnrModule> LDMMLists;
    private Context mcontext;
    public LogisticsDetailMatnrsAdapter(ArrayList<LogisitiicsDetailMatnrModule> list,
                                        Context ctx){
        this.LDMMLists = list;
        this.mcontext = ctx;
    }


    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemLogisiticsMatnrsBinding llMbind = DataBindingUtil
                .inflate(LayoutInflater.from(mcontext),
                R.layout.list_item_logisitics_matnrs,parent,false);
        WmsBaseRecycleViewHolder sbrvholder = new WmsBaseRecycleViewHolder(llMbind.getRoot());
        sbrvholder.setBinding(llMbind);
        sbrvholder.setViewType(viewType);
        return sbrvholder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, int position) {
        ListItemLogisiticsMatnrsBinding llMbind =
                (ListItemLogisiticsMatnrsBinding) holder.getBinding();
        llMbind.setMatrnsInfo(LDMMLists.get(position));

    }

    @Override
    public int getItemCount() {
        return LDMMLists.size();
    }
}
