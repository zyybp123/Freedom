package com.bpz.commonlibrary.net.download;

import com.bpz.commonlibrary.entity.ResInfo;

public interface MyDownloadListener {

    void onDownloadSuccess(ResInfo resInfo);

    void onDownloadFail(String url, Throwable e);

    void onDownloading(String url, int progress);

    void onDownloadStart(String url);
}
