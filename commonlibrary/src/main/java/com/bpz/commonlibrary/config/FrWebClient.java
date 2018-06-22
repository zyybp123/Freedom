package com.bpz.commonlibrary.config;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bpz.commonlibrary.util.LogUtil;

/**
 * webClient实现类
 */
public class FrWebClient extends WebViewClient {
    private static final String TAG = "FrWebClient";
    private IWebListener listener;
    private Activity mActivity;

    public FrWebClient(Activity activity, IWebListener listener) {
        mActivity = activity;
        this.listener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.e(TAG, "shouldOverrideUrlLoading");
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith("sms:")
                || url.startsWith(WebView.SCHEME_MAILTO)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (Exception e) {
                LogUtil.e(TAG, "activity start error: " + e);
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
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogUtil.e(TAG, "onPageStarted, url : " + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LogUtil.e(TAG, "onPageFinished");
        if (listener != null) {
            listener.onPageFinished(view, url);
        }
        super.onPageFinished(view, url);
    }

    /**
     * webView请求拦截
     *
     * @param request 资源请求
     */
    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description,
                                String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        LogUtil.e(TAG, "onReceivedError : " + "errorCode: " + errorCode
                + ",description: " + description + ",failingUrl: " + failingUrl);
        if (listener != null) {
            listener.onPageError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                    WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
    }
}
