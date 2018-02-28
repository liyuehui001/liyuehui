package com.smec.wms.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.smec.wms.R;
import com.smec.wms.android.bean.ClassType;
import com.smec.wms.android.bean.Matnr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/21.
 */

public class MatnrTypeAdapter  extends RecyclerView.Adapter<MatnrTypeAdapter.MatnrTypeViewHolder> {

    private Context context;
    private List<Matnr> matnrList = new ArrayList<>();

    private MatterAdapterItemListener listener ;

    public MatnrTypeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatnrTypeAdapter.MatnrTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_matnr_type, parent, false);
        return new MatnrTypeAdapter.MatnrTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MatnrTypeAdapter.MatnrTypeViewHolder holder, final int position) {
//        holder.titleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener == null) {
//                    listener.itemClick(matnrList.get(position),R.layout.layout_class_type);
//                }
//            }
//        });
        holder.cardGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.itemClickWithViewHolder(matnrList.get(position), R.layout.layout_matnr_type,holder);
                }
            }
        });

        holder.matnrNameLabel.setText(matnrList.get(position).getMatnrName());
        holder.matnrNumLabel.setText(matnrList.get(position).getMatnr());

        Glide.with(context)
                .load(Uri.parse(matnrList.get(position).getDownloadUrl()))
                .crossFade()
                .centerCrop()
                .into(holder.matnrImage);
    }

    @Override
    public int getItemCount() {
        return matnrList.size();
    }

    public class MatnrTypeViewHolder extends RecyclerView.ViewHolder {

        public ViewGroup cardGroup ;
        public TextView matnrNumLabel ;
        public TextView matnrNameLabel ;
        public PhotoView matnrImage ;

        public MatnrTypeViewHolder(View view) {
            super(view);
            cardGroup = (ViewGroup) view.findViewById(R.id.matnrCard);
            matnrNumLabel = (TextView) view.findViewById(R.id.matnrNumValue);
            matnrNameLabel = (TextView) view.findViewById(R.id.matnrNameValue);
            matnrImage = (PhotoView) view.findViewById(R.id.matnrImage);
            matnrImage.disenable();
        }
    }

    public void setMatnrList(List<Matnr> matnrList) {
        if(matnrList == null) {
            return;
        }
        this.matnrList = matnrList;
        this.notifyDataSetChanged();
    }

    public void addMatnrList(List<Matnr> matnrList) {
        if(this.matnrList == null || matnrList == null) {
            return;
        }
        this.matnrList.addAll(matnrList);
        this.notifyDataSetChanged();
    }


    public MatterAdapterItemListener getListener() {
        return listener;
    }

    public void setListener(MatterAdapterItemListener listener) {
        this.listener = listener;
    }
}

