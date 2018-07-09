package com.bpz.commonlibrary.net.download;

import com.bpz.commonlibrary.entity.ResInfo;

public interface MyDownloadListener {

    void onDownloadSuccess(ResInfo resInfo);

    void onDownloadFail(Throwable e);

    void onDownloading(int progress);

    void onDownloadStart();
}
