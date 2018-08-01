package com.bpz.commonlibrary.net.web;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bpz.commonlibrary.entity.WebParamEntity;
import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.util.LogUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
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
        //HttpUrl httpUrl = HttpUrl.parse(url);
        if (!URLUtil.isNetworkUrl(url)) {
            //对于非HTTP的链接，也可以跳转到浏览器进行处理
            Intent intent = new Intent(Intent.ACTION_VIEW);
            try {
                if ((null != url) && url.startsWith("intent:")) {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Uri data = intent.getData();
                    if (data != null) {
                        String params = data.getQueryParameter("params");
                        LogUtil.e(TAG, "params: " + params);
                    }
                } else {
                    intent.setData(Uri.parse(url));
                }
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
                LogUtil.e(TAG, "there is no this activity: " + ignored);
                Uri data = intent.getData();
                if (data != null) {
                    String params = data.getQueryParameter("params");
                    WebParamEntity entity = new Gson().fromJson(params,
                            WebParamEntity.class);
                    if (entity != null){
                        String apk = entity.getApk();
                        if (URLUtil.isNetworkUrl(apk)){
                            //是一个apk下载链接
                            view.loadUrl(apk);
                            return false;
                        }
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
                LogUtil.e(TAG, "URI: parse fail!");
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
