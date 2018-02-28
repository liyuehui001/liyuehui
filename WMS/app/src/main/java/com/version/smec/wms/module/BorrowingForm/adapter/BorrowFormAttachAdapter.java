package com.version.smec.wms.module.BorrowingForm.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smec.wms.R;
import com.smec.wms.databinding.ListItemBorrowFormAttachBinding;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadListener;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadManager;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.BorrowingForm.bean.BorrowingFormAppendix;
import com.version.smec.wms.module.PreviewFile.PreviewFileActivity;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by 小黑 on 2017/8/22.
 */
public class BorrowFormAttachAdapter  extends RecyclerView.Adapter<WmsBaseRecycleViewHolder>  {

    private ArrayList<BorrowingFormAppendix> attachList;
    private boolean todoTrueBillFalse;
    private Context mcontext;

    public BorrowFormAttachAdapter(Context ctx,ArrayList<BorrowingFormAppendix> alist,boolean ttbf){
        this.mcontext = ctx;
        this.todoTrueBillFalse = ttbf;
        this.attachList = alist;

    }

    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBorrowFormAttachBinding bfaListItem =
                DataBindingUtil.inflate(LayoutInflater.from(mcontext), R.layout.list_item_borrow_form_attach,parent,false);
        WmsBaseRecycleViewHolder wbrvHolder = new WmsBaseRecycleViewHolder(bfaListItem.getRoot());
        wbrvHolder.setBinding(bfaListItem);
        wbrvHolder.setViewType(viewType);
        return wbrvHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, final int position) {
        final ListItemBorrowFormAttachBinding bfaListItem = (ListItemBorrowFormAttachBinding) holder.getBinding();
        bfaListItem.setBorrowFormInfo(attachList.get(position));
        bfaListItem.ivPreview.setClickable(true);
        bfaListItem.ivPreview.setSelected(true);
        bfaListItem.ivDownload.setClickable(true);
        bfaListItem.ivDownload.setSelected(true);

        final int fileTypeInt = CommonUtils.getCodeByFileType(attachList.get(position).getAttachType());

        bfaListItem.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bfaListItem.ivDownload.isClickable()){
                    Log.e("BorrowFormAttachAdapter",attachList.get(position).getUrl());

                    attachList.get(position).setCode(fileTypeInt);
                    SmecDownloadManager.getInstance().queueTarget(attachList.get(position).getUrl(),
                        attachList.get(position).getAttachName(),
                        attachList.get(position).getAttachName(),
                        fileTypeInt,
                            new SmecDownloadListener() {
                        @Override
                        public void smecDownloadCompleted(final String fileName,
                                                          final String savePath,
                                                          final int code) {
                            attachList.get(position).setCode(code);
                            attachList.get(position).setLocalPath(savePath);
                            ToastUtils.showToast("下载完成:文件存储在"+savePath);
                            _setImageViewDownLoadPreViewClickable(bfaListItem.ivDownload);
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


        bfaListItem.ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attachList.get(position).getLocalPath() == null || attachList.get(position).getLocalPath().equals("")){

                    SmecDownloadManager.getInstance().queueTarget(attachList.get(position).getUrl(),
                            attachList.get(position).getAttachName(),
                            attachList.get(position).getAttachName(),
                            fileTypeInt,
                            new SmecDownloadListener() {
                                @Override
                                public void smecDownloadCompleted(final String fileName,
                                                                  final String savePath,
                                                                  final int code) {
                                    attachList.get(position).setCode(code);
                                    attachList.get(position).setLocalPath(savePath);
                                    preview(mcontext,fileName,savePath,code);
                                    Log.e("download","success");
                                }

                                @Override
                                public void smecDownloadError() {
                                    Log.e("download","error");
                                }
                            });
                }else {
                    preview(mcontext,attachList.get(position).getAttachName(),
                            attachList.get(position).getLocalPath(),
                            fileTypeInt);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.attachList.size();
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
