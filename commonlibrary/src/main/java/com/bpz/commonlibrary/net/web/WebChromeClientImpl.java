package com.bpz.commonlibrary.net.web;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import okhttp3.HttpUrl;

import static android.app.Activity.RESULT_OK;

public class WebChromeClientImpl extends WebChromeClient {
    private static final String TAG = "WebChromeClientImpl";
    public static int FILE_CHOOSER_RESULT_CODE = 501;
    public static int FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 502;
    private IWebListener listener;
    private View mXCustomView;
    private View mXProgressVideo;
    private CustomViewCallback mXCustomViewCallback;
    private WebViewFragmentN viewFragmentN;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public WebChromeClientImpl(IWebListener listener, WebViewFragmentN viewFragmentN) {
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
     *
     * @param view     添加全屏播放的view
     * @param callback 回调
     */
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (viewFragmentN == null) {
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
        if (viewFragmentN == null) {
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
        if (listener != null) {
            listener.showWebView();
        }
    }

    /**
     * 配置权限（同样在WebChromeClient中实现）
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        if (listener != null) {
            listener.locationPermission(origin, callback);
        }
        //callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
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

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
        if (viewFragmentN != null) {
            viewFragmentN.mActivity.startActivityForResult(
                    chooserIntent, FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5
            );
        }
    }

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return (mXCustomView != null);
    }

    //扩展浏览器上传文件
    //3.0++版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooserImpl(uploadMsg);
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        if (viewFragmentN != null) {
            viewFragmentN.mActivity.startActivityForResult(
                    Intent.createChooser(i, "文件选择"), FILE_CHOOSER_RESULT_CODE
            );
        }
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooserImpl(uploadMsg);
    }

    /**
     * 5.0以下 上传图片成功后的回调
     */
    public void mUploadMessage(Intent intent, int resultCode) {
        if (null == mUploadMessage) {
            return;
        }
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * 5.0以上 上传图片成功后的回调
     */
    public void mUploadMessageForAndroid5(Intent intent, int resultCode) {
        if (null == mUploadMessageForAndroid5) {
            return;
        }
        Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
        if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
        } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
        }
        mUploadMessageForAndroid5 = null;
    }
}
