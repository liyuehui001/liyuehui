package com.version.smec.wms.module.PreviewFile;

import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.smec.wms.R;
import com.smec.wms.databinding.ActivityPreviewFileBinding;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadManager;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.utils.ToastUtils;
import com.version.smec.wms.widget.TopBarLayout;

import java.io.File;

/**
 * Created by xupeizuo on 2017/9/6.
 */

public class PreviewFileActivity extends WmsBaseActivity {

    private int fileType=0;
    private String fileName="";
    private String filePath="";
    private ActivityPreviewFileBinding activityPreviewFileBinding;
    private Handler mHandler=new Handler();

    @Override
    public Object getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPreviewFileBinding= DataBindingUtil.setContentView(this, R.layout.activity_preview_file);
        getDataFromIntent();
        _initView();
        activityPreviewFileBinding.setCode(fileType);
    }

    /**
     * 获取文件类型，文件名，文件保存路径
     */
    public void getDataFromIntent(){

        fileType=getIntent().getIntExtra(SmecDownloadManager.FILE_TYPE,0);
        fileName=getIntent().getStringExtra(SmecDownloadManager.FILE_NAME);
        filePath=getIntent().getStringExtra(SmecDownloadManager.FILE_PATH);
    }

    private void _initView(){
        if(fileName!=null){
            activityPreviewFileBinding.topBar.getTvTextTitle().setText(fileName);
        }
        activityPreviewFileBinding.topBar.setTopBarListener(new TopBarLayout.TopBarListener() {
            @Override
            public void setOnLeftClickListener() {
                finish();
            }

            @Override
            public void setOnRight1ClickListener() {

            }
            @Override
            public void setOnRight2ClickListener() {

            }
        });

        if(filePath!=null && !filePath.equals("")){

            if(fileType == SmecDownloadManager.FILE_TYPE_IMAGE){

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(PreviewFileActivity.this).load(filePath)
                                .asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(activityPreviewFileBinding.photoZoom);
                    }
                },500);


            } else if(fileType == SmecDownloadManager.FILE_TYPE_PDF){

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activityPreviewFileBinding.pdfView
                                .fromFile(new File(filePath))
                                .defaultPage(1)
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {

                                    }
                                }).onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {

                            }
                        }).onDraw(new OnDrawListener() {
                            @Override
                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                            }
                        }).swipeHorizontal(false).enableSwipe(true).load();
                    }
                },400);
            }else {
                ToastUtils.showToast("无法打开该格式文件");
            }

        }else {
            ToastUtils.showToast("未找到文件");
        }

    }
}
