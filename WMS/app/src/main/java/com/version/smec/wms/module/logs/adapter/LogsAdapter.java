package com.version.smec.wms.module.logs.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.databinding.ListItemLogsListBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.logs.bean.LogsModule;
import com.version.smec.wms.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/28.
 */
public class LogsAdapter extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {
    private ArrayList<LogsModule> logsList;
    private Context mcontext;
    public LogsAdapter(ArrayList<LogsModule> logsList,Context ctx){
        this.logsList = logsList;
        this.mcontext = ctx;
    }


    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemLogsListBinding lilBind =  DataBindingUtil.inflate(LayoutInflater.from(mcontext),
                R.layout.list_item_logs_list,parent,false);
        WmsBaseRecycleViewHolder wbrvHolder = new WmsBaseRecycleViewHolder(lilBind.getRoot());
        wbrvHolder.setBinding(lilBind);
        wbrvHolder.setViewType(viewType);
        return wbrvHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, int position) {
        ListItemLogsListBinding lilBind = (ListItemLogsListBinding) holder.getBinding();
        lilBind.setLogsInfo(logsList.get(position));
        if (CommonUtils.notEmptyStr(logsList.get(position).getOperation())){
            if (logsList.get(position).getOperation().equals(WmsApi.logsOperation.REFUSE) ){
                lilBind.ivNode.setBackgroundResource(R.mipmap.wms_logs_refuse_node);
            }else{
                lilBind.ivNode.setBackgroundResource(R.mipmap.wms_logs_node_point);
            }
        }


        if (position == logsList.size()-1){
            lilBind.llViewLineBelow.setVisibility(View.GONE);
        }else{
            lilBind.llViewLineBelow.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }
}
