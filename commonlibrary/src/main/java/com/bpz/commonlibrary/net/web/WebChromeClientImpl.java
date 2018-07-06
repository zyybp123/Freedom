package com.bpz.commonlibrary.net.web;

import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import okhttp3.HttpUrl;

public class WebChromeClientImpl extends WebChromeClient {
    private IWebListener listener;
    private static final String TAG = "WebChromeClientImpl";
    private View mXCustomView;
    private View mXProgressVideo;
    private CustomViewCallback mXCustomViewCallback;
    private WebViewFragmentN viewFragmentN;
    public WebChromeClientImpl(IWebListener listener,WebViewFragmentN viewFragmentN) {
        this.listener = listener;
        this.viewFragmentN = viewFragmentN;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (listener != null) {
            listener.progressChanged(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        // 监听器和标题都不为空时，触发回调
        if (listener != null && !TextUtils.isEmpty(title)) {
            //如果title不是个url且不包含".html"字样就展示
            HttpUrl httpUrl = HttpUrl.parse(title);
            if (httpUrl == null && !title.contains(".html")) {
                listener.setTitle(title);
            }
        }
    }

    /**
     * 播放网络视频时全屏会被调用的方法
     * @param view 添加全屏播放的view
     * @param callback 回调
     */
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (viewFragmentN == null){
            callback.onCustomViewHidden();
            return;
        }
        if (listener != null) {
            listener.hideWebView();
        }
        // 如果一个视图已经存在，那么立刻终止并新建一个
        if (mXCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        viewFragmentN.fullViewAddView(view);
        //添加完成后，再横屏
        viewFragmentN.mActivity
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mXCustomView = view;
        mXCustomViewCallback = callback;
        if (listener != null) {
            listener.showVideoFullView();
        }
    }

    /**
     * 视频播放退出全屏会被调用的
     */
    @Override
    public void onHideCustomView() {
        if (mXCustomView == null) {// 不是全屏播放状态
            return;
        }
        if (viewFragmentN == null){
            return;
        }
        viewFragmentN.mActivity
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mXCustomView.setVisibility(View.GONE);
        if (viewFragmentN.getVideoFullView() != null) {
            viewFragmentN.getVideoFullView().removeView(mXCustomView);
        }
        mXCustomView = null;
        if (listener != null) {
            listener.hideVideoFullView();
        }
        mXCustomViewCallback.onCustomViewHidden();
        if (listener != null){
            listener.showWebView();
        }
    }

    /*// 视频加载时进程loading
    @Override
    public View getVideoLoadingProgressView() {
        if (mXProgressVideo == null && viewFragmentN != null) {
            LayoutInflater inflater = LayoutInflater.from(viewFragmentN.mActivity);
            mXProgressVideo = inflater.inflate(R.layout.video_loading_progress,
                    null);
        }
        return mXProgressVideo;
    }*/

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return (mXCustomView != null);
    }
}
