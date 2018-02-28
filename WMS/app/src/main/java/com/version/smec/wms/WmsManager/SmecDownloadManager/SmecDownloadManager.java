package com.version.smec.wms.WmsManager.SmecDownloadManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.smec.wms.R;
import com.version.smec.wms.utils.ToastUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xupeizuo on 2017/9/6.
 * @author xupeizuo
 * @param 'com.liulishuo.filedownloader:library:1.6.4'
 */

public final class SmecDownloadManager {

    private static SmecDownloadManager smecDownloadManager;
    private static byte[] syncObj=new byte[0];
    private static String downloadTitle="附件下载";

    public static final String FILE_TYPE="FILE_TYPE";
    public static final String FILE_NAME="FILE_NAME";
    public static final String FILE_PATH="FILE_PATH";
    public static final int FILE_TYPE_IMAGE=10001;//图片
    public static final int FILE_TYPE_PDF=10003;//pdf
    public static final int FILE_TYPE_ERROR = -1;
    private static final String base_path="/sdcard/Smec_WMS/";


    public static SmecDownloadManager getInstance(){
        if(smecDownloadManager == null){
            synchronized (syncObj){
                if(smecDownloadManager == null){
                    smecDownloadManager=new SmecDownloadManager();
                }
            }
        }
        return smecDownloadManager;
    }

    public void queueTarget(String requestUrl,final String fileName, final String savePath,
                                         final int fileTypeCode, final SmecDownloadListener smecDownloadListener){

        if(fileTypeCode == FILE_TYPE_ERROR){
            ToastUtils.showToast("不支持下载该文件");
            return;
        }

        FileDownloader.getImpl()
                .create(requestUrl)
                .setPath(base_path+savePath)
                .setListener(new FileDownloadNotificationListener(new FileDownloadNotificationHelper()) {
                    @Override
                    protected BaseNotificationItem create(BaseDownloadTask task) {
                        return new NotificationItem(task.getId(),downloadTitle,"");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        smecDownloadListener.smecDownloadCompleted(fileName,base_path+savePath,fileTypeCode);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        e.printStackTrace();
                        smecDownloadListener.smecDownloadError();
                    }
                }).start();
    }


    public static class NotificationItem extends BaseNotificationItem {

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        private NotificationItem(int id, String title, String desc) {
            super(id, title, desc);
            builder = new NotificationCompat.
                    Builder(FileDownloadHelper.getAppContext());

            builder.setDefaults(Notification.DEFAULT_LIGHTS)
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setContentTitle(getTitle())
                    .setContentText(desc)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.smec);

        }

        @Override
        public void show(boolean statusChanged, int status, boolean isShowProgress) {

            String desc = getDesc();
            builder.setContentTitle(getTitle())
                    .setContentText(desc);
            if (statusChanged) {
                builder.setTicker(desc);
            }
            builder.setProgress(getTotal(), getSofar(), !isShowProgress);
            getManager().notify(getId(), builder.build());
        }

    }
}
