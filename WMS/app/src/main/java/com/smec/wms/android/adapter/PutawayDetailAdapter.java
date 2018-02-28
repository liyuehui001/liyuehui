package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.PutawayDetail;

import java.util.List;

/**
 * Created by Adobe on 2016/1/19.
 */
public class PutawayDetailAdapter extends BaseAdapter {
    private List<PutawayDetail> putawayDetailList;
    private LayoutInflater inflater;
    private Context context;

    public PutawayDetailAdapter(Context context, List<PutawayDetail> data) {
        this.context = context;
        this.putawayDetailList = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.putawayDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.putawayDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PutawayDetailViewHolder listHolder = null;
        if (view == null) {
            listHolder = new PutawayDetailViewHolder();
            view = inflater.inflate(R.layout.list_item_putaway_detail, null);
            listHolder.tvMatnr = (TextView) view.findViewById(R.id.putaway_detail_matnr);
            listHolder.tvMantrName=(TextView)view.findViewById(R.id.putaway_detail_matnrname);
            listHolder.tvOrderQty=(TextView)view.findViewById(R.id.putaway_detail_orderqty);
            listHolder.tvPutawayQty=(TextView)view.findViewById(R.id.putaway_detail_putawayqty);
            listHolder.tvUnit=(TextView)view.findViewById(R.id.putaway_detail_unit);
            listHolder.tvSuggest=(TextView)view.findViewById(R.id.putaway_detail_shelf);
            view.setTag(listHolder);
        } else {
            listHolder = (PutawayDetailViewHolder) view.getTag();
        }
        PutawayDetail item = this.putawayDetailList.get(position);
        listHolder.tvMatnr.setText(item.getMatnr());
        listHolder.tvMantrName.setText(item.getMatnrName());
        listHolder.tvOrderQty.setText(item.getOrderQty());
        listHolder.tvPutawayQty.setText(item.getPutawayQty());
        listHolder.tvUnit.setText(item.getPkgUnit());
        listHolder.tvSuggest.setText(item.getSuggestShelfSpace());
        return view;
    }
}
class PutawayDetailViewHolder{
    public TextView tvMatnr;
    public TextView tvMantrName;
    public TextView tvOrderQty;
    public TextView tvPutawayQty;
    public TextView tvUnit;
    public TextView tvSuggest;
}
