package com.bpz.commonlibrary.config;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.bpz.commonlibrary.util.LogUtil;


/**
 * js通信接口类
 */
public class JsInterface {
    private static final String TAG = "JsInterface";
    private Context mContext;

    public JsInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void imageClick(String imgUrl, String linkUrl) {
        LogUtil.e(TAG, "click img, imgUrl: " + imgUrl);
    }

    @JavascriptInterface
    public void textClick(String type, String item_pk) {
        LogUtil.e(TAG, "click text, text type: " + type);
    }


}
