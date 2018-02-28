package com.version.smec.wms.module.BorrowingForm.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.databinding.ListItemBorrowFormMatnrsBinding;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormGoodModule;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/22.
 */
public class BorrowFormMatnrAdapter  extends RecyclerView.Adapter<WmsBaseRecycleViewHolder>  {

    private Context mcontext;
    private ArrayList<BorrowingFormGoodModule> MatnrsList;
    private boolean todoTrueBillFalse;

    public BorrowFormMatnrAdapter(Context mcontext,
                                  ArrayList<BorrowingFormGoodModule> MatnrsList,
                                  boolean todoTrueBillFalse){
        this.mcontext = mcontext;
        this.MatnrsList = MatnrsList;
        this.todoTrueBillFalse = todoTrueBillFalse;
    }

    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemBorrowFormMatnrsBinding bfmListItem =
                DataBindingUtil.inflate(LayoutInflater.from(mcontext),
                        R.layout.list_item_borrow_form_matnrs,parent,false);

        WmsBaseRecycleViewHolder sbrvHolder = new WmsBaseRecycleViewHolder(bfmListItem.getRoot());
        sbrvHolder.setViewType(viewType);
        sbrvHolder.setBinding(bfmListItem);
        return sbrvHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, int position) {
        final ListItemBorrowFormMatnrsBinding bfmListItem  = (ListItemBorrowFormMatnrsBinding) holder.getBinding();
        bfmListItem.setBorrowMatnrsInfo(MatnrsList.get(position));


        final boolean[] isSpreadPackup = {false};

        bfmListItem.tvTextSpreadPackup.setText("查看详情");
        bfmListItem.ivSrcSpreadPackup.setImageResource(R.mipmap.wms_requirement_spread);


        bfmListItem.llQtyHdBranch.setVisibility(View.GONE);
        bfmListItem.llStandPrice.setVisibility(View.GONE);
        bfmListItem.llDesc.setVisibility(View.GONE);



        bfmListItem.llSpreadPackupBorrowFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSpreadPackup[0]){
                    isSpreadPackup[0] = true;
                    bfmListItem.tvTextSpreadPackup.setText("收起");
                    bfmListItem.ivSrcSpreadPackup.setImageResource(R.mipmap.wms_requirement_packup);

                    if (todoTrueBillFalse){
                        bfmListItem.llQtyHdBranch.setVisibility(View.VISIBLE);
                        bfmListItem.llStandPrice.setVisibility(View.VISIBLE);
                    }else{
                        bfmListItem.llDesc.setVisibility(View.VISIBLE);
                    }
                }else{
                    isSpreadPackup[0] = false;
                    bfmListItem.tvTextSpreadPackup.setText("查看详情");
                    bfmListItem.ivSrcSpreadPackup.setImageResource(R.mipmap.wms_requirement_spread);

                    if (todoTrueBillFalse){
                        bfmListItem.llQtyHdBranch.setVisibility(View.GONE);
                        bfmListItem.llStandPrice.setVisibility(View.GONE);
                    }else{
                        bfmListItem.llDesc.setVisibility(View.GONE);
                    }
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return this.MatnrsList.size();
    }
}
