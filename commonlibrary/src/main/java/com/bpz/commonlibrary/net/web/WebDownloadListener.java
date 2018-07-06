package com.bpz.commonlibrary.net.web;

import android.os.Environment;
import android.webkit.DownloadListener;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SDCardUtil;
import com.bpz.commonlibrary.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;

public class WebDownloadListener implements DownloadListener {
    public static final String WEB_VIEW_DOWNLOAD_PATH = "/tzq/web/downloads";
    private static final String TAG = "WebDownloadListener";
    private String webDownloadPath;
    private OkHttpClient okHttpClient;
    private ExecutorService executorService;

    public WebDownloadListener() {
        if (SDCardUtil.hasSDCard()) {
            //有SD卡，下载至SD卡
            webDownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + WEB_VIEW_DOWNLOAD_PATH;
        } else {
            //没有SD卡，下载到应用内部
            webDownloadPath = LibApp.mContext.getFilesDir().getAbsolutePath()
                    + WEB_VIEW_DOWNLOAD_PATH;
        }
        //okHttpClient = AppCenterPresenter.getInstance().getOkHttpClient();
        //executorService = AppCenterPresenter.getInstance().getExecutorService();
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        //webView监听下载，此处可以实现下载逻辑
        executorService.execute(new DownloadTask(url));
    }

    private void downLoadFail() {

    }

    private void downLoadSuccess(File file) {
        //文件下载成功
    }

    public class DownloadTask implements Runnable {

        private String url;

        public DownloadTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            Request.Builder request = new Request.Builder().url(url);
            Call call = okHttpClient.newCall(request.build());
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //失败
                    LogUtil.e(TAG, "download zip fail !" + e);
                    downLoadFail();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //拿到响应体，写入文件
                    if (response.isSuccessful()) {
                        File file = new File(WEB_VIEW_DOWNLOAD_PATH, StringUtil.getFileName(url));
                        if (!file.exists()) {
                            boolean newFile = file.createNewFile();
                        } else {
                            boolean delete = file.delete();
                        }
                        Okio.buffer(Okio.sink(file))
                                .write(response.body().bytes())
                                .flush();
                        //下载完成
                        downLoadSuccess(file);
                    } else {
                        LogUtil.e(TAG, "response is not successful !");
                    }
                }
            });
        }
    }
}
