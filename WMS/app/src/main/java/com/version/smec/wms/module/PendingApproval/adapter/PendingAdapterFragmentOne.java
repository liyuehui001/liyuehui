package com.version.smec.wms.module.PendingApproval.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.databinding.FragmentPendingOneItemBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModel;
import com.version.smec.wms.module.Requirements.RequirementsActivity;

import com.version.smec.wms.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/8/14.
 */
public class PendingAdapterFragmentOne extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {

    private Context mcontext;
    private String title;
    private ArrayList<RequirementModel> pendinglist=new ArrayList<>();
    private boolean todo_true_order_false;
    public PendingAdapterFragmentOne(Context context,boolean ttof){
        this.mcontext=context;
        this.todo_true_order_false = ttof;
    }

    /**
     * 刷新数据
     * @param arrayList
     */
    public void setPendinglist(ArrayList<RequirementModel> arrayList){
        if(CommonUtils.notEmpty(arrayList)){
            pendinglist=arrayList;
        }else {
            pendinglist=new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    /**
     * 上啦加载更多在底部增加数据
     * @param arrayList
     */
    public void addFooter(ArrayList<RequirementModel> arrayList){
        if(CommonUtils.notEmpty(arrayList)){
            for(RequirementModel requirementModel : arrayList){
                pendinglist.add(pendinglist.size(),requirementModel);
                notifyItemInserted(pendinglist.size()-1);
            }
        }
    }

    public void setTitle(String title){
        this.title=title;
    }
    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentPendingOneItemBinding fragmentPendingOneItemBinding=
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.fragment_pending_one_item,parent,false);
        WmsBaseRecycleViewHolder wmsBaseRecycleViewHolder =
                new WmsBaseRecycleViewHolder(fragmentPendingOneItemBinding.getRoot());
        wmsBaseRecycleViewHolder.setBinding(fragmentPendingOneItemBinding);
        wmsBaseRecycleViewHolder.setViewType(viewType);
        return wmsBaseRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, final int position) {
        final FragmentPendingOneItemBinding fragmentPendingOneItemBinding= (FragmentPendingOneItemBinding) holder.getBinding();
        fragmentPendingOneItemBinding.setVariable(BR.requiremen,pendinglist.get(position));
        setTitle(position,fragmentPendingOneItemBinding);
        if(!TextUtils.isEmpty(pendinglist.get(position).getUrgentCode())){
            if (pendinglist.get(position).getUrgentCode().equals("Y")) {
                fragmentPendingOneItemBinding.imgAgent.setImageResource(R.mipmap.wms_requirement_agent);
            } else {
                fragmentPendingOneItemBinding.imgAgent.setImageResource(R.mipmap.wms_requirement_unagent);
            }
        }
        if(fragmentPendingOneItemBinding.tvPendingtitle.getText().equals("需求单")){
            fragmentPendingOneItemBinding.ivBranch.setImageResource(R.mipmap.pengdingbranch);
        }
        holder.getBinding().executePendingBindings();//刷新界面
        fragmentPendingOneItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragmentPendingOneItemBinding.tvPendingtitle.getText().equals("需求单")){
                    Intent mintent = new Intent(mcontext, RequirementsActivity.class);
                    mintent.putExtra("receiptNo",pendinglist.get(position).getReceiptNo());
                    mintent.putExtra("receiptType",pendinglist.get(position).getReceiptType());
                    mintent.putExtra("mintentBorrow",pendinglist.get(position).getOperation());
                    mintent.putExtra("isUrgent",pendinglist.get(position).getUrgentCode());
                    mintent.putExtra("operation",pendinglist.get(position).getOperation());
                    mintent.putExtra("todoTrueBillFalse",todo_true_order_false);
                    mcontext.startActivity(mintent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendinglist.size();
    }

    public void  setTitle(int position,FragmentPendingOneItemBinding fragmentPendingOneItemBinding){
        if(pendinglist.get(position).getReceiptType().equals("WMS_XQ")){
            fragmentPendingOneItemBinding.tvPendingtitle.setText("需求单");
            fragmentPendingOneItemBinding.tvPendingtitle.setTextColor(mcontext.getResources().getColor(R.color.wms_xq_title_color));
        }
    }

    public ArrayList<RequirementModel> getPendinglist() {
        return pendinglist;
    }

    /**
     * 删除列表数据
     * @param receiptNo
     */
    public void removeNo(String receiptNo){
        if(receiptNo == null || receiptNo.equals("")){
            return;
        }
        Iterator iterator=pendinglist.iterator();
        while (iterator.hasNext()){
            RequirementModel requirementModel=(RequirementModel)iterator.next();
            if(requirementModel.getReceiptNo() !=null && receiptNo.equals(receiptNo)){
                iterator.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }
}
