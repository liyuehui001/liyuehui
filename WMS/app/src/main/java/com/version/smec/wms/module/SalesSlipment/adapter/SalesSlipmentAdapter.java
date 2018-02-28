package com.version.smec.wms.module.SalesSlipment.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.databinding.ItemSalesSlipGoodsBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.SalesSlipment.bean.SalesSlipGoodsModule;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/18.
 */
public class SalesSlipmentAdapter extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {
    private ArrayList<SalesSlipGoodsModule> ssmList;
    private Context context;
    private boolean todoTrueBillFalse;

    public SalesSlipmentAdapter(ArrayList<SalesSlipGoodsModule> ssmListCon,Context ctx,boolean todoTrueBillFalse){
        this.ssmList = ssmListCon;
        this.context = ctx;
        this.todoTrueBillFalse = todoTrueBillFalse;
    }


    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSalesSlipGoodsBinding issgBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.item_sales_slip_goods,parent,false);
        WmsBaseRecycleViewHolder wbrViewHolder = new WmsBaseRecycleViewHolder(issgBinding.getRoot());
        wbrViewHolder.setBinding(issgBinding);
        wbrViewHolder.setViewType(viewType);
        return wbrViewHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, int position) {
        final ItemSalesSlipGoodsBinding issgBinding = (ItemSalesSlipGoodsBinding) holder.getBinding();
        issgBinding.setVariable(BR.item_good_detail,ssmList.get(position));


        final boolean[] is_visible = {false};
        issgBinding.ivSpreadPackup.setImageResource(R.mipmap.wms_requirement_spread);
        issgBinding.tvTextSpreadPackup.setText("查看详情");

        if (todoTrueBillFalse){//待办详情
            issgBinding.llDesc.setVisibility(View.GONE);
            issgBinding.rlSaleRepertoryAllPart.setVisibility(View.GONE);
            issgBinding.llItemSaleSlipStandardSellingPrice.setVisibility(View.GONE);

        }else{//单据详情
            issgBinding.llDesc.setVisibility(View.GONE);
            issgBinding.rlSaleRepertoryAllPart.setVisibility(View.GONE);
            issgBinding.llItemSaleSlipStandardSellingPrice.setVisibility(View.GONE);

        }


        issgBinding.llSpreadPackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_visible[0]){


                    issgBinding.tvTextSpreadPackup.setText("收起");
                    issgBinding.ivSpreadPackup.setImageResource(R.mipmap.wms_requirement_packup);

                    if (todoTrueBillFalse){
                        issgBinding.llDesc.setVisibility(View.GONE);
                        issgBinding.rlSaleRepertoryAllPart.setVisibility(View.VISIBLE);
                        issgBinding.llItemSaleSlipStandardSellingPrice.setVisibility(View.VISIBLE);
                    }else{
                        issgBinding.llDesc.setVisibility(View.VISIBLE);
                        issgBinding.rlSaleRepertoryAllPart.setVisibility(View.GONE);
                        issgBinding.llItemSaleSlipStandardSellingPrice.setVisibility(View.GONE);

                    }
                    is_visible[0] = true;
                }else{
                    issgBinding.ivSpreadPackup.setImageResource(R.mipmap.wms_requirement_spread);
                    issgBinding.tvTextSpreadPackup.setText("查看详情");

                    if (todoTrueBillFalse){
                        issgBinding.llDesc.setVisibility(View.GONE);
                        issgBinding.rlSaleRepertoryAllPart.setVisibility(View.GONE);
                        issgBinding.llItemSaleSlipStandardSellingPrice.setVisibility(View.GONE);
                    }else{
                        issgBinding.llDesc.setVisibility(View.GONE);
                        issgBinding.rlSaleRepertoryAllPart.setVisibility(View.GONE);
                        issgBinding.llItemSaleSlipStandardSellingPrice.setVisibility(View.GONE);

                    }

                    is_visible[0] = false;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return ssmList.size();
    }
}
