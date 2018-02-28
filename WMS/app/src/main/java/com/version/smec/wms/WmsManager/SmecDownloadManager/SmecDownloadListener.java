package com.version.smec.wms.WmsManager.SmecDownloadManager;

/**
 * Created by xupeizuo on 2017/9/7.
 */

public interface SmecDownloadListener{

    /**
     * 附件下载完成
     * @param savePath 文件保存地址
     * @param fileName 文件名
     * @param code 文件类型
     */
    void smecDownloadCompleted(String fileName,String savePath, int code);

    /**
     * 下载失败
     */
    void smecDownloadError();
}
