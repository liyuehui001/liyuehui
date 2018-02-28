package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.CpzxItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adobe on 2016/7/15.
 */
public class CPZXAdapter extends BaseAdapter  {
    private List<CpzxItem>completeData;
    private List<CpzxItem> data;
    private LayoutInflater inflater;
    private Context context;

    public CPZXAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        data=new ArrayList<>();
    }

    public void setData(List<CpzxItem>data){
        this.completeData=data;
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void filter(CpzxItem filter){
        this.data.clear();
        for(CpzxItem item:this.completeData){
            if(item.filter(filter)){
                this.data.add(item);
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CPZXAdapterHolder vh = null;
        CpzxItem item = this.data.get(position);
        if (view == null ) {
            view = this.inflater.inflate(R.layout.list_item_cpzx, null);
            CPZXAdapterHolder holder = new CPZXAdapterHolder();
            holder.tvBoxNo=(TextView)view.findViewById(R.id.cpzx_boxno);
            holder.tvMatnr = (TextView) view.findViewById(R.id.cpzx_matnr);
            holder.tvName = (TextView) view.findViewById(R.id.cpzx_name);
            holder.tvClass = (TextView) view.findViewById(R.id.cpzx_class);
            holder.tvPicNo = (TextView) view.findViewById(R.id.cpzx_picno);
            holder.tvAmount = (TextView) view.findViewById(R.id.cpzx_amount);
            holder.tvRemark = (TextView) view.findViewById(R.id.cpzx_remark);
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (CPZXAdapterHolder) view.getTag();
        }
//        vh.tvMatnr.setText(item.getGwgno());
//        vh.tvPicNo.setText(item.getIdnrk());
        vh.tvMatnr.setText(item.getIdnrk());
        vh.tvPicNo.setText(item.getGwgno());
        vh.tvBoxNo.setText(item.getPackingNo());
        vh.tvName.setText(item.getTitle());
        vh.tvClass.setText(item.getClassName());
        vh.tvAmount.setText(item.getQuantity());
        vh.tvRemark.setText(item.getRemark());
        return view;
    }
}

class CPZXAdapterHolder{
    public TextView tvBoxNo;
    public TextView tvName;
    public TextView tvClass;
    public TextView tvMatnr;
    public TextView tvPicNo;
    public TextView tvAmount;
    public TextView tvRemark;
}
