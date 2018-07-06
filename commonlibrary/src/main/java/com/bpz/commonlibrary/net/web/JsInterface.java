package com.bpz.commonlibrary.net.web;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.entity.HpmFileBean;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * js通信接口类
 */
public class JsInterface {
    private static final String TAG = "JsInterface";
    public static String WEB_DATA = "webData";
    private static SharedPreferences sp;
    private Context mContext;
    private WebViewFragmentN webViewFragmentN;
    private Gson mGson;
    private boolean hasAuth = false;

    JsInterface(Context context, WebViewFragmentN webViewFragmentN, boolean hasAuth) {
        mContext = context;
        this.webViewFragmentN = webViewFragmentN;
        this.hasAuth = hasAuth;
        if (mContext != null) {
            sp = mContext.getSharedPreferences(WEB_DATA, Context.MODE_PRIVATE);
        }
        mGson = new Gson();
    }

    public static boolean saveData(String key, String value) {
        if (sp == null) {
            sp = LibApp.mContext.getSharedPreferences(WEB_DATA, Context.MODE_PRIVATE);
        }
        return sp.edit().putString(key, value).commit();
    }

    public static String getData(String key, String defaultValue) {
        if (sp == null) {
            sp = LibApp.mContext.getSharedPreferences(WEB_DATA, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defaultValue);
    }

    @JavascriptInterface
    public void imageClick(String imgUrl) {
        LogUtil.e(TAG, "click img, imgUrl: ");
        //, int width, int height
        //LogUtil.e(TAG, "width: " + width + ", height: " + height);
    }

    @JavascriptInterface
    public void textClick(String type, String url) {
        LogUtil.e(TAG, "click text, text type: " + type + ", url: " + url);
    }

    /**
     * 将网页的数据保存到本地
     *
     * @param key   键
     * @param value 值
     * @return 返回是否保存成功
     */
    @JavascriptInterface
    public boolean saveDataLocal(String key, String value) {
        if (sp == null) {
            sp = LibApp.mContext.getSharedPreferences(WEB_DATA, Context.MODE_PRIVATE);
        }
        return sp.edit().putString(key, value).commit();
    }

    /**
     * 从本地获取保存的值
     *
     * @param key          键
     * @param defaultValue 值
     * @return 返回获取到的值
     */
    @JavascriptInterface
    public String getLocalData(String key, String defaultValue) {
        if (sp == null) {
            sp = LibApp.mContext.getSharedPreferences(WEB_DATA, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defaultValue);
    }

    /**
     * 退出activity
     */
    @JavascriptInterface
    public void exitPage() {
        if (mContext != null && mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
    }

    /**
     * 一些关于页面的控制
     *
     * @param config 控制参数
     */
    @JavascriptInterface
    public void someConfig(String config) {
        LogUtil.e(TAG, "config: " + config + ",fragment: " + webViewFragmentN);
        if (webViewFragmentN != null && !StringUtil.isSpace(config)) {
            LogUtil.e(TAG, "parse params!");
            //解析参数，更换样式
            try {
                final HpmFileBean hpmFileBean = mGson.fromJson(config, HpmFileBean.class);
                LogUtil.e(TAG, "hpmFileBean: " + hpmFileBean);
                webViewFragmentN.mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewFragmentN.setDefaultReactiveSettings(hpmFileBean);
                    }
                });
            } catch (Exception e) {
                LogUtil.e(TAG, "parse error: " + e);
            }
        }
    }

    @JavascriptInterface
    public void reLogin() {
        LogUtil.e(TAG, "token失效，重新登录");
    }
}
