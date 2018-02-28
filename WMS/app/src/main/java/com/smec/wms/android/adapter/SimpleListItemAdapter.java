package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/1/31.
 */
public class SimpleListItemAdapter extends BaseAdapter {
    private List<String> listData;
    private LayoutInflater inflater;

    public SimpleListItemAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        listData = new ArrayList<>();
    }

    public SimpleListItemAdapter(Context context,List<String> listData) {
        inflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
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
        SimpleListitemAdapterHolder vh = null;
        if (view == null) {
            view = this.inflater.inflate(R.layout.list_item_simple_adapter, null);
            SimpleListitemAdapterHolder holder = new SimpleListitemAdapterHolder();
            holder.tvText1 = (TextView) view.findViewById(R.id.text1);
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (SimpleListitemAdapterHolder) view.getTag();
        }
        vh.tvText1.setText(listData.get(position));
        return view;
    }

    public void add(String text) {
        this.listData.add(text);
        this.notifyDataSetChanged();
    }

    public void clear(){
        this.listData.clear();
        this.notifyDataSetChanged();
    }
}

class SimpleListitemAdapterHolder {
    public TextView tvText1;
}