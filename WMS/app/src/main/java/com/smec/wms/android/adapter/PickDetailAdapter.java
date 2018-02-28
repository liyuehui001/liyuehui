package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.PickDetail;

import java.util.List;

/**
 * Created by Adobe on 2016/1/29.
 */
public class PickDetailAdapter extends BaseAdapter {
    private List<PickDetail> detailList;
    private LayoutInflater inflater;
    private Context context;

    public PickDetailAdapter(Context context, List<PickDetail> detailList) {
        this.detailList = detailList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.detailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PickDetailViewHolder listHolder = null;
        if (view == null) {
            listHolder = new PickDetailViewHolder();
            view = inflater.inflate(R.layout.list_item_pick_detail, null);
            listHolder.tvMatnr = (TextView) view.findViewById(R.id.pick_detail_matnr);
            listHolder.tvMantrName = (TextView) view.findViewById(R.id.pick_detail_matnrname);
            listHolder.tvShelf = (TextView) view.findViewById(R.id.pick_detail_shelf);
            listHolder.tvShouldQty = (TextView) view.findViewById(R.id.pick_detail_shouldqty);
            listHolder.tvWaitingQty = (TextView) view.findViewById(R.id.pick_detail_waitqty);
            view.setTag(listHolder);
        } else {
            listHolder = (PickDetailViewHolder) view.getTag();
        }
        PickDetail item = this.detailList.get(position);
        listHolder.tvMatnr.setText(item.getMatnr());
        listHolder.tvMantrName.setText(item.getMatnrName());
        listHolder.tvShelf.setText(item.getShelfSpaceNo());
        listHolder.tvShouldQty.setText(item.getShouldQty());
        listHolder.tvWaitingQty.setText(item.getWaitingQty());
        return view;
    }
}

class PickDetailViewHolder {
    public TextView tvMatnr;
    public TextView tvMantrName;
    public TextView tvShelf;
    public TextView tvShouldQty;
    public TextView tvWaitingQty;
}
