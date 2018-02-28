package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.PackBoxDetail;

import java.util.List;

/**
 * Created by Adobe on 2016/1/31.
 */
public class PackDetailAdapter extends BaseAdapter {
    private List<PackBoxDetail> packBoxDetails;
    private LayoutInflater inflater;

    public PackDetailAdapter(Context context, List<PackBoxDetail> data) {
        inflater = LayoutInflater.from(context);
        this.packBoxDetails = data;
    }

    @Override
    public int getCount() {
        return packBoxDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return packBoxDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PackDetailViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_pack_detail, null);
            holder = new PackDetailViewHolder();
            holder.tvMatnr = (TextView) view.findViewById(R.id.pack_detail_matnr);
            holder.tvMatnrName = (TextView) view.findViewById(R.id.pack_detail_matnrname);
            holder.tvUnit = (TextView) view.findViewById(R.id.pack_detail_unit);
            holder.tvBoxqty = (TextView) view.findViewById(R.id.pack_detail_boxqty);
            view.setTag(holder);
        } else {
            holder = (PackDetailViewHolder) view.getTag();
        }
        PackBoxDetail item = packBoxDetails.get(position);
        holder.tvMatnr.setText(item.getMatnr());
        holder.tvMatnrName.setText(item.getMatnrName());
        holder.tvUnit.setText(item.getPkgUnit());
        holder.tvBoxqty.setText(item.getBoxQty());
        return view;
    }
}

class PackDetailViewHolder {
    public TextView tvMatnr;
    public TextView tvMatnrName;
    public TextView tvUnit;
    public TextView tvBoxqty;
}
