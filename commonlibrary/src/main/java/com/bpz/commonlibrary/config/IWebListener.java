package com.bpz.commonlibrary.config;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * 监听webView的一系列操作
 */
public interface IWebListener {
    void hideProgressBar();

    void showWebView();

    void hideWebView();

    void startProgress();

    void progressChanged(int newProgress);

    void addImageClickListener();

    void setTitle(String title);
    void setIcon(Bitmap icon);

    void onPageFinished(WebView view, String url);

    void onPageError(WebView view, int errorCode, String description, String failingUrl);
}
