package com.smec.wms.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.BaseActivity;
import com.smec.wms.android.bean.SimpleBoxItem;
import com.smec.wms.android.bean.Status;
import com.smec.wms.android.bean.StockInDetailsResponse;
import com.smec.wms.android.bean.StockInOrderResponse;
import com.smec.wms.android.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/15.
 */
public class InboundBoxDetailAdapter extends ServerRequestAdapter {
    private Context context;
    private LayoutInflater inflater;
    private StockInOrderResponse boxDetail;

    public InboundBoxDetailAdapter(BaseActivity context) {
        this(context, null);
    }

    public InboundBoxDetailAdapter(BaseActivity context, String iface) {
        super(context, iface);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void requestBoxDetail(String orderNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("OrderNo", orderNo);
        params.put("detail", "0");
        this.request(StockInDetailsResponse.class, params);
    }

    @Override
    public int getCount() {
        if (boxDetail == null) {
            return 0;
        }
        return boxDetail.getBoxNoList().size();
    }

    @Override
    public Object getItem(int i) {
        return boxDetail.getBoxNoList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder listHolder = null;
        if (view == null) {
            listHolder = new ViewHolder();
            view = inflater.inflate(R.layout.scan_inbound_list_item, null);
            listHolder.textView = (TextView) view.findViewById(R.id.inboud_box_detail_item_boxno);
            listHolder.scanTimeView = (TextView) view.findViewById(R.id.inbound_box_scan_time);
            view.setTag(listHolder);
        } else {
            listHolder = (ViewHolder) view.getTag();
        }
        SimpleBoxItem item = boxDetail.getBoxNoList().get(i);
        listHolder.textView.setText(item.getBoxNo()+" 未扫描");
        if (item.getSacnTime() != null) {
            listHolder.scanTimeView.setText(item.getSacnTime()+" 已扫描");
            int highLightColor = context.getResources().getColor(R.color.list_item_highlight);
            listHolder.textView.setTextColor(highLightColor);
            listHolder.scanTimeView.setTextColor(highLightColor);
        }
        return view;
    }

    /**
     * 数据更新
     *
     * @param data
     */
    @Override
    protected void updateData(Object data) {
        this.boxDetail = (StockInOrderResponse) data;
        this.notifyDataSetChanged();
    }

    public void setBoxItemList(List<SimpleBoxItem> data) {
        this.boxDetail = new StockInOrderResponse();
        this.boxDetail.setBoxNoList(data);
        this.notifyDataSetChanged();
    }

    public boolean boxChecked(String boxNo) {
        List<SimpleBoxItem> lst = this.boxDetail.getBoxNoList();
        for (int i = 0; i < lst.size(); ++i) {
            SimpleBoxItem item = lst.get(i);
            if (item.getBoxNo().equals(boxNo)) {
                item.setStatus(Status.COMPLETE);
                item.setSacnTime(AppUtil.getCurrentTime());
                //将最新扫描的放到第一个
                lst.remove(i);
                lst.add(0, item);
                this.notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public boolean isAllChecked(){
        List<SimpleBoxItem> lst = this.boxDetail.getBoxNoList();
        for(SimpleBoxItem item:lst){
            if(!Status.COMPLETE.equals(item.getStatus())){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取已经扫描箱子列表
     * @return
     */
    public List<String>getCheckedBoxList(){
        List<String>boxList=new ArrayList<>();
        List<SimpleBoxItem> lst = this.boxDetail.getBoxNoList();
        for(SimpleBoxItem item:lst){
            if(!Status.COMPLETE.equals(item.getStatus())){
                boxList.add(item.getBoxNo());
            }
        }
        return boxList;
    }

    public StockInOrderResponse getBoxDetail() {
        return boxDetail;
    }

    public void setBoxDetail(StockInOrderResponse boxDetail) {
        this.boxDetail = boxDetail;
        this.notifyDataSetChanged();
    }
}

class ViewHolder {
    public TextView textView;
    public TextView scanTimeView;
}
