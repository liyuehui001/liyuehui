package com.smec.wms.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.ShelfQueryActivity;
import com.smec.wms.android.bean.Shelf;
import com.smec.wms.android.bean.ShelfListResponse;

/**
 * Created by Adobe on 2016/3/11.
 */
public class ShelfQueryAdapter extends BaseAdapter {
    private ShelfListResponse shelfList = new ShelfListResponse();
    private LayoutInflater inflater;
    private ShelfQueryActivity shelfqueryActivity;

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    private boolean isEnd = false;

    public ShelfQueryAdapter(ShelfQueryActivity context) {
        shelfqueryActivity = context;
        inflater = LayoutInflater.from(context);
    }

    public void clear() {
        this.isEnd = false;
        shelfList.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(ShelfListResponse data) {
        shelfList.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = shelfList.getStockDetail() == null ? 0 : shelfList.getStockDetail().size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return shelfList.getStockDetail().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ShelfQueryViewHolder vh = null;
        Shelf shelf = shelfList.getStockDetail().get(position);
        if (view == null || !(view.getTag() instanceof ShelfQueryViewHolder)) {
            view = this.inflater.inflate(R.layout.list_item_shelf_query, null);
            ShelfQueryViewHolder holder = new ShelfQueryViewHolder();
            holder.tvMatnr = (TextView) view.findViewById(R.id.shelf_query_matnr);
            holder.tvStockQty = (TextView) view.findViewById(R.id.shelf_query_shelfqty);
            holder.tvLockQty = (TextView) view.findViewById(R.id.shelf_query_lockqty);
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (ShelfQueryViewHolder) view.getTag();
        }
        vh.tvMatnr.setText(shelf.getMatnr());
        vh.tvLockQty.setText(shelf.getLockQty());
        vh.tvStockQty.setText(shelf.getStockQty());
        return view;
    }

}

class ShelfQueryViewHolder {
    public TextView tvMatnr;
    public TextView tvStockQty;
    public TextView tvLockQty;
}
