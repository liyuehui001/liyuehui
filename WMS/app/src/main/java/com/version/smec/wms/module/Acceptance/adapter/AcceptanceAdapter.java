package com.version.smec.wms.module.Acceptance.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.databinding.ActivityPendingAcceptanceBinding;
import com.smec.wms.databinding.LayoutAcceptanceItemBinding;
import com.smec.wms.databinding.LayoutAccptanceIncludeBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.Acceptance.AcceptanceActivity;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceMatnrsModel;
import com.version.smec.wms.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/21.
 */
public class AcceptanceAdapter extends RecyclerView.Adapter<WmsBaseRecycleViewHolder>  {

    private Context mcontext;
    private boolean todo_true_order_false;
    private ArrayList<AcceptanceMatnrsModel> acceptanceMatnrslist=new ArrayList<>();
    public AcceptanceAdapter(Context context,boolean ttof){
        this.mcontext=context;
        this.todo_true_order_false = ttof;
    };
    public void setList( ArrayList<AcceptanceMatnrsModel> list){
        if(CommonUtils.notEmpty(list)) {
            this.acceptanceMatnrslist = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutAcceptanceItemBinding layoutAcceptanceItemBinding=DataBindingUtil.inflate
                (LayoutInflater.from(mcontext), R.layout.layout_acceptance_item,parent,false);
        WmsBaseRecycleViewHolder wmsBaseRecycleViewHolder =
                new WmsBaseRecycleViewHolder(layoutAcceptanceItemBinding.getRoot());
        wmsBaseRecycleViewHolder.setBinding(layoutAcceptanceItemBinding);
        wmsBaseRecycleViewHolder.setViewType(viewType);
        return wmsBaseRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, int position) {
        final LayoutAcceptanceItemBinding layoutAcceptanceItemBinding= (LayoutAcceptanceItemBinding) holder.getBinding();
        layoutAcceptanceItemBinding.setVariable(BR.matnrs,acceptanceMatnrslist.get(position));

        holder.getBinding().executePendingBindings();//刷新界面
        if(todo_true_order_false){
            layoutAcceptanceItemBinding.llPackupApread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(layoutAcceptanceItemBinding.tvChakan.getText().equals("收起")){
                        layoutAcceptanceItemBinding.lyL.setVisibility(View.GONE);
                        layoutAcceptanceItemBinding.lyY.setVisibility(View.GONE);
                        layoutAcceptanceItemBinding.tvChakan.setText("查看详情");
                        layoutAcceptanceItemBinding.ivSpread.setImageResource(R.mipmap.wms_requirement_spread);
                    }else {
                        layoutAcceptanceItemBinding.lyL.setVisibility(View.VISIBLE);
                        layoutAcceptanceItemBinding.lyY.setVisibility(View.VISIBLE);
                        layoutAcceptanceItemBinding.tvChakan.setText("收起");
                        layoutAcceptanceItemBinding.ivSpread.setImageResource(R.mipmap.wms_requirement_packup);
                    }
                }
            });
        }else {
            layoutAcceptanceItemBinding.lyL.setVisibility(View.GONE);
            layoutAcceptanceItemBinding.lyY.setVisibility(View.GONE);
            layoutAcceptanceItemBinding.lyLingyong.setVisibility(View.GONE);
            layoutAcceptanceItemBinding.lyShijichuku.setVisibility(View.VISIBLE);
            layoutAcceptanceItemBinding.llPackupApread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(layoutAcceptanceItemBinding.tvChakan.getText().equals("收起")){
                        layoutAcceptanceItemBinding.lyDanwei.setVisibility(View.GONE);
                        layoutAcceptanceItemBinding.lyShuoming.setVisibility(View.GONE);
                        layoutAcceptanceItemBinding.tvChakan.setText("查看详情");
                        layoutAcceptanceItemBinding.ivSpread.setImageResource(R.mipmap.wms_requirement_spread);
                    }else {
                        layoutAcceptanceItemBinding.lyDanwei.setVisibility(View.VISIBLE);
                        layoutAcceptanceItemBinding.lyShuoming.setVisibility(View.VISIBLE);
                        layoutAcceptanceItemBinding.tvChakan.setText("收起");
                        layoutAcceptanceItemBinding.ivSpread.setImageResource(R.mipmap.wms_requirement_packup);
                    }
                }
            });
        }
        layoutAcceptanceItemBinding.tvChakan.setTag(position);


    }

    @Override
    public int getItemCount() {
        return acceptanceMatnrslist.size();
    }
}
