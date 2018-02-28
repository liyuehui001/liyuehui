package com.smec.wms.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.MatnrStockQueryActivity;
import com.smec.wms.android.activity.ShelfQueryActivity;
import com.smec.wms.android.bean.MatnrStock;
import com.smec.wms.android.bean.MatnrStockResponse;
import com.smec.wms.android.bean.Shelf;
import com.smec.wms.android.bean.ShelfListResponse;

/**
 * Created by Adobe on 2016/3/11.
 */
public class MatnrStockQueryAdapter extends BaseAdapter {
    private MatnrStockResponse shelfList = new MatnrStockResponse();
    private LayoutInflater inflater;
    private MatnrStockQueryActivity matnrStockqueryActivity;

    public MatnrStockQueryAdapter(MatnrStockQueryActivity context) {
        matnrStockqueryActivity = context;
        inflater = LayoutInflater.from(context);
    }

    public void clear() {
        shelfList.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(MatnrStockResponse data) {
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
        MatnrStockQueryViewHolder vh = null;
        MatnrStock matnr = shelfList.getStockDetail().get(position);
        if (view == null || !(view.getTag() instanceof ShelfQueryViewHolder)) {
            view = this.inflater.inflate(R.layout.list_item_matnr_stock_query, null);
            MatnrStockQueryViewHolder holder = new MatnrStockQueryViewHolder();
            holder.tvShelf=(TextView)view.findViewById(R.id.matnr_query_shelf);
            holder.tvMatnr = (TextView) view.findViewById(R.id.matnr_query_matnr);
            holder.tvStockQty = (TextView) view.findViewById(R.id.matnr_query_matnrqty);
            holder.tvLockQty = (TextView) view.findViewById(R.id.matnr_query_lockqty);
            holder.tvMatnrName = (TextView) view.findViewById(R.id.matnr_query_matnrname);
            holder.tvUnit = (TextView) view.findViewById(R.id.matnr_query_unit);
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (MatnrStockQueryViewHolder) view.getTag();
        }
        vh.tvMatnr.setText(matnr.getMatnr());
        vh.tvLockQty.setText(matnr.getLockQty());
        vh.tvStockQty.setText(matnr.getStockQty());
        vh.tvShelf.setText(matnr.getShelfSpaceNo());
        vh.tvMatnrName.setText(matnr.getMatnrName());
        vh.tvUnit.setText(matnr.getPkgUnit());
        return view;
    }

}

class MatnrStockQueryViewHolder {
    public TextView tvMatnr;
    public TextView tvMatnrName;
    public TextView tvShelf;
    public TextView tvUnit;
    public TextView tvStockQty;
    public TextView tvLockQty;
}
