package com.smec.wms.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.bean.ClassType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/21.
 */

public class ClassTypeAdapter extends RecyclerView.Adapter<ClassTypeAdapter.ClassTypeViewHolder> {

    private Context context;
    private List<ClassType> classTypeList = new ArrayList<>();

    private MatterAdapterItemListener listener ;

    public ClassTypeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ClassTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_class_type, parent, false);
        return new ClassTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassTypeViewHolder holder, final int position) {
        if(classTypeList.get(position).getClassMeaning() == null) {
            holder.titleView.setText("");
        }else{
            holder.titleView.setText(classTypeList.get(position).getClassType() + classTypeList.get(position).getClassMeaning() + "部件");
        }
        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.itemClick(classTypeList.get(position),R.layout.layout_class_type);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classTypeList.size();
    }

    public class ClassTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;

        public ClassTypeViewHolder(View view) {
            super(view);
            titleView = (TextView)view.findViewById(R.id.classTypeName);
        }
    }

    public void setClassTypeList(List<ClassType> classTypeList) {
        if(classTypeList == null) {
            return;
        }
        this.classTypeList = classTypeList;
        this.notifyDataSetChanged();
    }

    public MatterAdapterItemListener getListener() {
        return listener;
    }

    public void setListener(MatterAdapterItemListener listener) {
        this.listener = listener;
    }
}
