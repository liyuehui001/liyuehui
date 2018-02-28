package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.StockQueryActivity;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.Matnr;
import com.smec.wms.android.bean.Stock;
import com.smec.wms.android.bean.StockListResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Adobe on 2016/3/11.
 */
public class StockQueryAdapter extends BaseAdapter {
    private StockListResponse stockList = new StockListResponse();
    private LayoutInflater inflater;
    private StockQueryActivity stockqueryActivity;
    private View lastListItemView;

    private StockQueryAdapterListener listener ;

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    private boolean isEnd = false;

    public StockQueryAdapter(StockQueryActivity context, StockQueryAdapterListener listener) {
        stockqueryActivity = context;
        this.listener = listener ;
        inflater = LayoutInflater.from(context);
    }

    public void clear() {
        this.isEnd = false;
        stockList.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(StockListResponse data) {
        stockList.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = stockList.getStocks() == null ? 0 : stockList.getStocks().size();
        return size + 1;
    }

    @Override
    public Object getItem(int position) {
        return stockList.getStocks().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
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
        StockQueryViewHolder vh = null;
        final Stock stock = stockList.getStocks().get(position);
        if (view == null || !(view.getTag() instanceof StockQueryViewHolder)) {
            view = this.inflater.inflate(R.layout.list_item_stock_query, null);
            StockQueryViewHolder holder = new StockQueryViewHolder();
            holder.tvMatnr = (TextView) view.findViewById(R.id.stock_query_matnr);
            holder.tvMatnrName = (TextView) view.findViewById(R.id.stock_query_matnrname);
            holder.tvWareHouseNo = (TextView) view.findViewById(R.id.stock_query_warehouse);
            holder.tvWareHouseName = (TextView) view.findViewById(R.id.stock_query_warehousename);
            holder.tvStockQty = (TextView) view.findViewById(R.id.stock_query_stockqty);
            holder.tvLockQty = (TextView) view.findViewById(R.id.stock_query_lockqty);
            holder.imageButton = (ImageView) view.findViewById(R.id.lookupImage);
            holder.tvStockPrice = (TextView) view.findViewById(R.id.stock_query_price);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.imageButtonClick(stock);
                    }
                }
            });
            vh = holder;
            view.setTag(vh);
        } else {
            vh = (StockQueryViewHolder) view.getTag();
        }
        vh.tvMatnr.setText(stock.getMatnr());
        vh.tvMatnrName.setText(stock.getMatnrName());
        vh.tvLockQty.setText(stock.getLockQty());
        vh.tvStockQty.setText(stock.getStockQty());
        vh.tvWareHouseNo.setText(stock.getWarehouseNo());
        vh.tvWareHouseName.setText(stock.getWarehouseName());
        if (stock.getPrice() == null || "".equals(stock.getPrice())){
            vh.tvStockPrice.setText("点击获取价格");
            vh.tvStockPrice.setTextColor(WmsApplication.getInstance().getResources().getColor(R.color.blue));
            vh.tvStockPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.lookPriceClick(position,stock.getMatnr());
                }
            });
        } else {
            vh.tvStockPrice.setText(stock.getPrice());
            vh.tvStockPrice.setTextColor(WmsApplication.getInstance().getResources().getColor(R.color.black));
            vh.tvStockPrice.setOnClickListener(null);
        }
        return view;
    }

    private void queryNexttPage() {
        if (!this.isEnd) {
            stockqueryActivity.queryNextPage();
        }
    }

    public interface StockQueryAdapterListener {
        void imageButtonClick(Stock stock);
        void lookPriceClick(int position,String matnr);
    }

    public void setPrice(int position,String price){
        this.stockList.getStocks().get(position).setPrice(price);
        this.notifyDataSetChanged();
    }
}

class StockQueryViewHolder {
    public TextView tvMatnr;
    public TextView tvMatnrName;
    public TextView tvWareHouseNo;
    public TextView tvWareHouseName;
    public TextView tvStockQty;
    public TextView tvLockQty;

    public ImageView imageButton ;
    public TextView tvStockPrice ;
}
