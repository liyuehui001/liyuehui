package com.smec.wms.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.ElevatorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/21.
 */

public class ElevatorTypeAdapter extends RecyclerView.Adapter<ElevatorTypeAdapter.ElevatorTypeViewHolder> {

    private Context context;
    private List<ElevatorType> elevatorTypeList = new ArrayList<>();

    private MatterAdapterItemListener listener ;

    public ElevatorTypeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ElevatorTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_elevator_type, parent, false);
        return new ElevatorTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElevatorTypeViewHolder holder, final int position) {
        holder.titleView.setText(elevatorTypeList.get(position).getEleTypeMeaning());
        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.itemClick(elevatorTypeList.get(position),R.layout.layout_elevator_type);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return elevatorTypeList.size();
    }

    public class ElevatorTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;

        public ElevatorTypeViewHolder(View view) {
            super(view);
            titleView = (TextView)view.findViewById(R.id.elevatorTypeName);
        }
    }

    public void setElevatorTypeList(List<ElevatorType> elevatorTypeList) {
        if(elevatorTypeList == null) {
            return;
        }
        this.elevatorTypeList = elevatorTypeList;
        this.notifyDataSetChanged();
    }

    public void setListener(MatterAdapterItemListener listener) {
        this.listener = listener;
    }
}
