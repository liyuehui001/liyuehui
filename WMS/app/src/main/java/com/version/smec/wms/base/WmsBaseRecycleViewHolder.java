package com.version.smec.wms.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xupeizuo on 2017/8/7.
 */

public class WmsBaseRecycleViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;
    private int viewType ;


    public WmsBaseRecycleViewHolder(View itemView) {
        super(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
