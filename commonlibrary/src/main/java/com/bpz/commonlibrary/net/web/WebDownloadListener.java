package com.bpz.commonlibrary.net.web;

import android.os.Environment;
import android.webkit.DownloadListener;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.entity.ResInfo;
import com.bpz.commonlibrary.net.download.DownloadManager;
import com.bpz.commonlibrary.net.download.DownloadObserver;
import com.bpz.commonlibrary.net.download.MyDownloadListener;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SDCardUtil;

import org.jetbrains.annotations.Contract;


public class WebDownloadListener implements DownloadListener {
    private static final String WEB_VIEW_DOWNLOAD_PATH = "/tzq/web/downloads";
    private static final String TAG = "WebDownloadListener";
    private String webDownloadPath;
    private MyDownloadListener downloadListener;

    WebDownloadListener(MyDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        if (SDCardUtil.hasSDCard()) {
            //有SD卡，下载至SD卡
            webDownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + WEB_VIEW_DOWNLOAD_PATH;
        } else {
            //没有SD卡，下载到应用内部
            webDownloadPath = LibApp.mContext.getFilesDir().getAbsolutePath()
                    + WEB_VIEW_DOWNLOAD_PATH;
        }
    }

    @Contract(pure = true)
    public static String getWebViewDownloadPath() {
        return WEB_VIEW_DOWNLOAD_PATH;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        LogUtil.e(TAG, "start download: " + url);
        if (downloadListener != null){
            downloadListener.onDownloadStart(url);
        }
        //webView监听下载，此处可以实现下载逻辑
        ResInfo resInfo = new ResInfo();
        resInfo.setUrl(url);
        resInfo.setFileDir(webDownloadPath);
        DownloadManager
                .getInstance(3)
                .download(resInfo, new DownloadObserver(resInfo, downloadListener));
    }

}
