package com.version.smec.wms.module.Acceptance.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.databinding.AcceptanceEnclosureBinding;
import com.smec.wms.databinding.LayoutAcceptanceItemBinding;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadListener;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadManager;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceEnclosureModel;
import com.version.smec.wms.module.Acceptance.bean.AcceptanceMatnrsModel;
import com.version.smec.wms.module.PreviewFile.PreviewFileActivity;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/23.
 */
public class EnclosureAdapter extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {

    private Context mcontext;
    private boolean todo_true_order_false;
    private ArrayList<AcceptanceEnclosureModel> acceptanceMatnrslist=new ArrayList<>();
    public EnclosureAdapter(Context context,boolean ttof){
        this.mcontext=context;
        this.todo_true_order_false = ttof;
    }
    public void setList( ArrayList<AcceptanceEnclosureModel> list){
        if(CommonUtils.notEmpty(list)) {
            this.acceptanceMatnrslist = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AcceptanceEnclosureBinding acceptanceEnclosureBinding= DataBindingUtil.inflate
                (LayoutInflater.from(mcontext), R.layout.acceptance_enclosure,parent,false);
        WmsBaseRecycleViewHolder wmsBaseRecycleViewHolder =
                new WmsBaseRecycleViewHolder(acceptanceEnclosureBinding.getRoot());
        wmsBaseRecycleViewHolder.setBinding(acceptanceEnclosureBinding);
        wmsBaseRecycleViewHolder.setViewType(viewType);
        return wmsBaseRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, final int position) {
        final  AcceptanceEnclosureBinding acceptanceEnclosureBinding= (AcceptanceEnclosureBinding) holder.getBinding();
        acceptanceEnclosureBinding.setVariable(BR.enclosure,acceptanceMatnrslist.get(position));

        acceptanceEnclosureBinding.ivPreview.setClickable(true);
        acceptanceEnclosureBinding.ivPreview.setSelected(true);
        acceptanceEnclosureBinding.ivDownload.setClickable(true);
        acceptanceEnclosureBinding.ivDownload.setSelected(true);

        final int fileTypeInt = CommonUtils.getCodeByFileType(acceptanceMatnrslist.get(position).getAttachType());
        acceptanceEnclosureBinding.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (acceptanceEnclosureBinding.ivDownload.isClickable()){

                    SmecDownloadManager.getInstance().queueTarget(acceptanceMatnrslist.get(position).getUrl(),
                        acceptanceMatnrslist.get(position).getAttachName(),
                        acceptanceMatnrslist.get(position).getAttachName(),
                        fileTypeInt,
                            new SmecDownloadListener() {
                        @Override
                        public void smecDownloadCompleted(final String fileName,
                                                          final String savePath,
                                                          final int code) {
                            acceptanceMatnrslist.get(position).setCode(code);
                            acceptanceMatnrslist.get(position).setLocalPath(savePath);
                            ToastUtils.showToast("下载完成:文件存储在"+savePath);
                            _setImageViewDownLoadPreViewClickable(acceptanceEnclosureBinding.ivDownload);
                            Log.e("download","success");
                        }
                        @Override
                        public void smecDownloadError() {
                            ToastUtils.showToast("下载失败");
                            Log.e("download","error");
                        }
                    });
                }
            }
        });


        acceptanceEnclosureBinding.ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acceptanceMatnrslist.get(position).getLocalPath() == null || acceptanceMatnrslist.get(position).getLocalPath().equals("")){

                    SmecDownloadManager.getInstance().queueTarget(acceptanceMatnrslist.get(position).getUrl(),
                            acceptanceMatnrslist.get(position).getAttachName(),
                            acceptanceMatnrslist.get(position).getAttachName(),
                            fileTypeInt,
                            new SmecDownloadListener() {
                                @Override
                                public void smecDownloadCompleted(final String fileName,
                                                                  final String savePath,
                                                                  final int code) {
                                    acceptanceMatnrslist.get(position).setCode(code);
                                    acceptanceMatnrslist.get(position).setLocalPath(savePath);

                                    preview(mcontext,fileName,savePath,code);
                                    Log.e("download","success");
                                }

                                @Override
                                public void smecDownloadError() {
                                    Log.e("download","error");
                                }
                            });

                }else {
                    preview(mcontext,acceptanceMatnrslist.get(position).getAttachName()
                            ,acceptanceMatnrslist.get(position).getLocalPath(),fileTypeInt);
                }
            }
        });

        holder.getBinding().executePendingBindings();//刷新界面


    }

    @Override
    public int getItemCount() {
        return acceptanceMatnrslist.size();
    }

    private void _setImageViewDownLoadPreViewClickable(ImageView ivCannotClick){
        ivCannotClick.setClickable(false);
        ivCannotClick.setSelected(false);
    }

    /**
     * 预览文件
     * @param context
     * @param fileName
     * @param filePath
     * @param fileType
     */
    public void preview(Context context,String fileName,String filePath,int fileType){
        Intent intent=new Intent(mcontext, PreviewFileActivity.class);
        intent.putExtra(SmecDownloadManager.FILE_NAME,fileName);
        intent.putExtra(SmecDownloadManager.FILE_PATH,filePath);
        intent.putExtra(SmecDownloadManager.FILE_TYPE,fileType);
        context.startActivity(intent);
    }
}
