package com.smec.wms.android.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

/**
 * Created by apple on 16/9/21.
 */

public interface MatterAdapterItemListener {
    public void itemClick(Object object, @LayoutRes int itemResId);
    public void itemClickWithViewHolder(Object object, @LayoutRes int itemResId , RecyclerView.ViewHolder viewHolder);
}
