package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smec.wms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/2/17.
 */
public class PackPlatAdapter extends BaseAdapter implements View.OnClickListener {
    private List<String> listData;
    private LayoutInflater inflater;

    public PackPlatAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        listData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    public List<String> getPlatData() {
        return listData;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PackPlatAdapterAdapterHolder vh = null;
        if (view == null) {
            view = this.inflater.inflate(R.layout.list_item_pack_plat, null);
            PackPlatAdapterAdapterHolder holder = new PackPlatAdapterAdapterHolder();
            holder.tvText1 = (TextView) view.findViewById(R.id.list_item_pack_text);
            holder.btDelete = (Button) view.findViewById(R.id.list_item_pack_plat_delete);
            holder.btDelete.setOnClickListener(this);
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (PackPlatAdapterAdapterHolder) view.getTag();
        }
        vh.tvText1.setText(String.format("%d %s", position + 1, listData.get(position)));
        vh.btDelete.setTag(position);
        return view;
    }

    public void add(String text) {
        this.listData.add(text);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.listData.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        listData.remove(position);
        this.notifyDataSetChanged();
    }
}

class PackPlatAdapterAdapterHolder {
    public TextView tvText1;
    public Button btDelete;
}
