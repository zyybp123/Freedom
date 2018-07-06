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

/**
 * webClient实现类
 */
public class WebClientImpl extends WebViewClient {
    private IWebListener listener;
    private Activity mActivity;

    public WebClientImpl(Activity activity, IWebListener listener) {
        mActivity = activity;
        this.listener = listener;

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // todo 特殊的url scheme,单独处理
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith("sms:")
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith("baidu")
                || url.startsWith("weixin:")
                || url.startsWith("alipays:")
                || url.startsWith("dianping:")
                ) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
            }
            return true;
        }
        if (listener != null) {
            listener.startProgress();
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
        if (listener != null){
            listener.onPageFinished(view,url);
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (error.getPrimaryError() == SslError.SSL_DATE_INVALID  // 日期不正确
                || error.getPrimaryError() == SslError.SSL_EXPIRED // 日期不正确
                || error.getPrimaryError() == SslError.SSL_INVALID // webView BUG
                || error.getPrimaryError() == SslError.SSL_UNTRUSTED) { // 根证书丢失
            if (listener != null){
                listener.onReceiveSslError(view,handler,error);
            }
        }
    }
}
