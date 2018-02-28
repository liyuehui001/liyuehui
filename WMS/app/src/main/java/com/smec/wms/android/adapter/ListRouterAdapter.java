package com.smec.wms.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.ListRouterBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/26.
 */

public class ListRouterAdapter extends RecyclerView.Adapter<ListRouterAdapter.ListRouterViewHolder>{

    private Activity activity ;
    private List<ListRouterBean> routerBeanList ;

    public ListRouterAdapter(Activity activity , List<ListRouterBean> routerBeanList) {
        this.activity = activity ;
        if (routerBeanList == null) {
            routerBeanList = new ArrayList<>();
        }
        this.routerBeanList = routerBeanList ;
    }

    @Override
    public ListRouterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_item_router, parent, false);
        return new ListRouterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListRouterViewHolder holder, int position) {
        final ListRouterBean routerBean = routerBeanList.get(position);
        holder.itemIcon.setImageResource(routerBean.getItemIcon());
        holder.itemText.setText(routerBean.getItemString());
        holder.itemRouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,routerBean.getRouterActivity());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routerBeanList.size();
    }

    public class ListRouterViewHolder extends RecyclerView.ViewHolder {

        ImageView itemIcon ;
        TextView itemText ;
        ViewGroup itemRouter ;

        public ListRouterViewHolder(View itemView) {
            super(itemView);
            itemRouter = (ViewGroup) itemView.findViewById(R.id.item_router);
            itemText = (TextView) itemView.findViewById(R.id.item_text);
            itemIcon = (ImageView) itemView.findViewById(R.id.item_icon);
        }
    }
}
