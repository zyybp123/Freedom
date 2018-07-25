package com.bpz.commonlibrary.net.web;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.HttpUrl;

/**
 * webClient实现类
 */
public class WebClientImpl extends WebViewClient {
    private static final String TAG = "WebClientImpl";
    private IWebListener listener;
    private Activity mActivity;

    public WebClientImpl(Activity activity, IWebListener listener) {
        mActivity = activity;
        this.listener = listener;

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            //对于非HTTP的链接，也可以跳转到浏览器进行处理
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                LogUtil.e(TAG, "there is no this activity: " + ignored);
                //应用未安装，得下载
                Uri parse = Uri.parse(url);
                LogUtil.e(TAG, "URI: " + parse);
            }
            return true;
        }
        view.loadUrl(url);
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (listener != null) {
            listener.onPageFinished(view, url);
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (error.getPrimaryError() == SslError.SSL_DATE_INVALID  // 日期不正确
                || error.getPrimaryError() == SslError.SSL_EXPIRED // 日期不正确
                || error.getPrimaryError() == SslError.SSL_INVALID // webView BUG
                || error.getPrimaryError() == SslError.SSL_UNTRUSTED) { // 根证书丢失
            if (listener != null) {
                listener.onReceiveSslError(view, handler, error);
            }
        }
    }
}
