package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.MatnrQueryActivity;
import com.smec.wms.android.bean.Matnr;
import com.smec.wms.android.bean.MatnrListResponse;
import com.smec.wms.android.util.AppUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Adobe on 2016/3/11.
 */
public class MatnrQueryAdapter extends BaseAdapter {
    private MatnrListResponse matnrList = new MatnrListResponse();
    private LayoutInflater inflater;
    private MatnrQueryActivity matnrqueryActivity;

    private MatnrQueryAdapterListener matnrQueryAdapterListener ;

    private View lastListItemView;
    private int linkColor = 0;
    private int normalColor=0;

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    private boolean isEnd = false;

    public MatnrQueryAdapter(MatnrQueryActivity context , MatnrQueryAdapterListener matnrQueryAdapterListener) {
        matnrqueryActivity = context;
        inflater = LayoutInflater.from(context);
        linkColor=context.getResources().getColor(R.color.blue);
        normalColor=context.getResources().getColor(R.color.black);
        this.matnrQueryAdapterListener = matnrQueryAdapterListener ;
    }

    public void clear() {
        this.isEnd = false;
        matnrList.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(MatnrListResponse data) {
        matnrList.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = matnrList.getMatnrs() == null ? 0 : matnrList.getMatnrs().size();
        return size + 1;
    }

    @Override
    public Object getItem(int position) {
        return matnrList.getMatnrs().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (position == this.getCount() - 1) {
            //最后一个
            TextView tvLast;
            if (lastListItemView == null) {
                lastListItemView = this.inflater.inflate(R.layout.list_item_lastitem, null);
                tvLast = (TextView) lastListItemView.findViewById(R.id.list_item_last_text);
                lastListItemView.setTag(tvLast);
            }
            tvLast = (TextView) lastListItemView.getTag();
            if (this.getCount() == 1) {
                tvLast.setText("没有可显示数据");
            } else {
                tvLast.setText(isEnd ? "数据已经全部加载完毕" : "点击加载下一页");
            }
            lastListItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queryNexttPage();
                }
            });
            return lastListItemView;

        }
        MatnrQueryViewHolder vh = null;
        final Matnr matnr = matnrList.getMatnrs().get(position);
        if (view == null || !(view.getTag() instanceof MatnrQueryViewHolder)) {
            view = this.inflater.inflate(R.layout.list_item_matnr_query, null);
            MatnrQueryViewHolder holder = new MatnrQueryViewHolder();
            holder.tvMatnr = (TextView) view.findViewById(R.id.matnr_query_matnr);
            holder.tvMatnrName = (TextView) view.findViewById(R.id.matnr_query_matnrname);
            holder.tvPkgQty = (TextView) view.findViewById(R.id.matnr_query_qty);
            holder.tvPkgUnit = (TextView) view.findViewById(R.id.matnr_query_unit);
            holder.tvPrice=(TextView)view.findViewById(R.id.matnr_query_price);
            holder.tvChange=(TextView)view.findViewById(R.id.matnr_query_change);
            holder.tvRemark=(TextView)view.findViewById(R.id.matnr_query_remark);
            holder.changeLayout=(View)view.findViewById(R.id.matnr_query_change_layout);
            holder.remarkLayout=(View)view.findViewById(R.id.matnr_query_remark_layout);
            holder.tvPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPrice((int)v.getTag());
                }
            });
            holder.imageButton = (ImageView) view.findViewById(R.id.lookupImage);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:
                    if (matnrQueryAdapterListener != null){
                        matnrQueryAdapterListener.imageButtonClick(matnr);
                    }
                }
            });
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (MatnrQueryViewHolder) view.getTag();
        }
        vh.tvMatnr.setText(matnr.getMatnr());
        vh.tvMatnrName.setText(matnr.getMatnrName());
        vh.tvPkgQty.setText(matnr.getPkgQty());
        vh.tvPkgUnit.setText(matnr.getPkgUnit());
        vh.tvPrice.setTag(position);
        if(matnr.getPrice()==null){
            vh.tvPrice.setText("点击获取价格");
            vh.tvPrice.setTextColor(linkColor);
        }else{
            vh.tvPrice.setTextColor(normalColor);
            vh.tvPrice.setText(matnr.getPrice());
        }
        if(AppUtil.strNull(matnr.getRemark())){
            vh.remarkLayout.setVisibility(View.GONE);
        }else{
            vh.remarkLayout.setVisibility(View.VISIBLE);
            vh.tvRemark.setText(matnr.getRemark());
        }

        if(AppUtil.strNull(matnr.getChangeMsg())){
            vh.changeLayout.setVisibility(View.GONE);
        }else{
            vh.changeLayout.setVisibility(View.VISIBLE);
            vh.tvChange.setText(matnr.getChangeMsg());
        }
        return view;
    }

    private void queryNexttPage() {
        if (!this.isEnd) {
            matnrqueryActivity.queryNextPage();
        }
    }

    private void requestPrice(int postition){
        String matnr=this.matnrList.getMatnrs().get(postition).getMatnr();
        String price=this.matnrList.getMatnrs().get(postition).getPrice();
        if(price==null) {
            this.matnrqueryActivity.requestPrice(matnr, postition);
        }
    }

    public void setPrice(int position,String price){
        this.matnrList.getMatnrs().get(position).setPrice(price);
        this.notifyDataSetChanged();
    }

    public interface MatnrQueryAdapterListener {
        void imageButtonClick(Matnr matnr);
    }
}

class MatnrQueryViewHolder {
    public TextView tvMatnr;
    public TextView tvMatnrName;
    public TextView tvPkgUnit;
    public TextView tvPkgQty;
    public TextView tvPrice;
    public TextView tvChange;
    public TextView tvRemark;
    public View changeLayout;
    public View remarkLayout;

    public ImageView imageButton ;
}
