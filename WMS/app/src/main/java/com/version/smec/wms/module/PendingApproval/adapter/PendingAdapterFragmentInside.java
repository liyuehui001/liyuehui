package com.version.smec.wms.module.PendingApproval.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.databinding.FragmentPendingOneItemBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.Acceptance.AcceptanceActivity;
import com.version.smec.wms.module.BorrowingForm.BorrowingFormActivity;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModel;
import com.version.smec.wms.module.SalesSlipment.SalesSlipmentActivity;
import com.version.smec.wms.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/8/18.
 */
public class PendingAdapterFragmentInside extends RecyclerView.Adapter<WmsBaseRecycleViewHolder>  {

    private Context mcontext;

    private boolean todo_true_order_false=false;
    private ArrayList<RequirementModel> pendinglist=new ArrayList<>();
    public PendingAdapterFragmentInside(Context context,boolean ttof){
        this.mcontext=context;
        this.todo_true_order_false = ttof;
    }
    public void setPendinglist(ArrayList<RequirementModel> arrayList){
        if(CommonUtils.notEmpty(arrayList)){
            this.pendinglist=arrayList;
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
        if (!pendinglist.get(position).getReceiptType().equals("WMS_XS")){//非销售单


            fragmentPendingOneItemBinding.ivBillTitle.setImageResource(R.mipmap.pendinggongsi);
            if(!TextUtils.isEmpty(pendinglist.get(position).getUrgentCode())){//有是否是急件的字段，
                fragmentPendingOneItemBinding.imgAgent.setVisibility(View.VISIBLE);
                if(pendinglist.get(position).getUrgentCode().equals("Y")){
                    fragmentPendingOneItemBinding.imgAgent.setImageResource(R.mipmap.wms_requirement_agent);
                }else {
                    fragmentPendingOneItemBinding.imgAgent.setImageResource(R.mipmap.wms_requirement_unagent);
                }
            }else {//没有是否是急件的字段，
                fragmentPendingOneItemBinding.imgAgent.setVisibility(View.GONE);

            }

            //设置项目图标和字段
            fragmentPendingOneItemBinding.ivBillTitle.setImageResource(R.mipmap.pendinggongsi);
            fragmentPendingOneItemBinding.tvBillTitle.setText(pendinglist.get(position).getProject());


        }else{

            //设置项目图标和字段
            fragmentPendingOneItemBinding.ivBillTitle.setImageResource(R.mipmap.wms_fragment_inside_bill_title);
            fragmentPendingOneItemBinding.imgAgent.setVisibility(View.GONE);
            fragmentPendingOneItemBinding.tvBillTitle.setText(pendinglist.get(position).getBillTitle());
        }

        holder.getBinding().executePendingBindings();//刷新界面
        fragmentPendingOneItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpIntent(fragmentPendingOneItemBinding,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendinglist.size();
    }
    public void jumpIntent(FragmentPendingOneItemBinding fragmentPendingOneItemBinding,int i){
        if(fragmentPendingOneItemBinding.tvPendingtitle.getText().equals("领用单")){
            Intent intentAcceptance=new Intent(mcontext,AcceptanceActivity.class);
            intentAcceptance.putExtra("UrgentCode",pendinglist.get(i).getUrgentCode());
            intentAcceptance = this.intentPutExtra(intentAcceptance,i);
            mcontext.startActivity(intentAcceptance);
        }else if(fragmentPendingOneItemBinding.tvPendingtitle.getText().equals("销售单")){
            Intent mintentSale = new Intent(mcontext, SalesSlipmentActivity.class);
            mintentSale = this.intentPutExtra(mintentSale,i);
            mcontext.startActivity(mintentSale);
        }else if(fragmentPendingOneItemBinding.tvPendingtitle.getText().equals("借用单")){
            Intent mintentBorrow = new Intent(mcontext, BorrowingFormActivity.class);

            mintentBorrow.putExtra("UrgentCode",pendinglist.get(i).getUrgentCode());

            mintentBorrow = this.intentPutExtra(mintentBorrow,i);

            mcontext.startActivity(mintentBorrow);

        }
    }

    public Intent intentPutExtra(Intent intent,int i){
        intent.putExtra("receiptType",pendinglist.get(i).getReceiptType());
        intent.putExtra("receiptNo",pendinglist.get(i).getReceiptNo());
        intent.putExtra("operation",pendinglist.get(i).getOperation());
        intent.putExtra("todoTrueBillFalse",todo_true_order_false);
        return intent;
    }




    public void  setTitle(int position,FragmentPendingOneItemBinding fragmentPendingOneItemBinding){
        if(pendinglist.get(position).getReceiptType().equals("WMS_XS")){
            fragmentPendingOneItemBinding.tvPendingtitle.setText("销售单");
            fragmentPendingOneItemBinding.tvPendingtitle
                    .setTextColor(WmsApplication.getContext().getResources().getColor(R.color.wms_xs_title_color));
        }else if(pendinglist.get(position).getReceiptType().equals("WMS_JY")){
            fragmentPendingOneItemBinding.tvPendingtitle.setText("借用单");
            fragmentPendingOneItemBinding.tvPendingtitle
                    .setTextColor(WmsApplication.getContext().getResources().getColor(R.color.wms_jy_title_color));
        }else if(pendinglist.get(position).getReceiptType().equals("WMS_LY")){
            fragmentPendingOneItemBinding.tvPendingtitle.setText("领用单");
            fragmentPendingOneItemBinding.tvPendingtitle
                    .setTextColor(WmsApplication.getContext().getResources().getColor(R.color.wms_ly_title_color));
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
