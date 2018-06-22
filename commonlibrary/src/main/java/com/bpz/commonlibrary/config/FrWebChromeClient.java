package com.bpz.commonlibrary.config;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.bpz.commonlibrary.util.LogUtil;

import okhttp3.HttpUrl;

/**
 * webChromeClient
 * 处理：进度显示，标题的动态加载，js对话框，网站图标
 */
public class FrWebChromeClient extends WebChromeClient {
    private static final String TAG = "FrWebChromeClient";
    private IWebListener listener;

    public FrWebChromeClient(IWebListener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        LogUtil.e(TAG, "current progress: " + newProgress);
        if (listener != null) {
            listener.progressChanged(newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        LogUtil.e(TAG, "title: " + title);
        // 监听器和标题都不为空时，触发回调
        if (listener != null && !TextUtils.isEmpty(title)) {
            //如果title不是个url且不包含".html"字样就展示
            HttpUrl httpUrl = HttpUrl.parse(title);
            if (httpUrl == null && !title.contains(".html")) {
                listener.setTitle(title);
            }
        }
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        LogUtil.e(TAG, "icon: " + icon);
        if (listener != null && icon != null) {
            //图标设置
            listener.setIcon(icon);
        }
    }


}
