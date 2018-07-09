package com.bpz.commonlibrary.net.web;

import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import java.io.File;

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

    void onPageFinished(WebView view, String url);

    void onReceiveSslError(WebView view, SslErrorHandler handler, SslError error);

    void showVideoFullView();

    void fullViewAddView(View view);

    void hideVideoFullView();

}
