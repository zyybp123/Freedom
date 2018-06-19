package com.bpz.commonlibrary.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bpz.commonlibrary.manager.ACache;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
    private static final String TAG = "MyCookieJar";
    private static final String COOKIE_FILE_NAME = "spCookie";
    private static volatile MyCookieJar mInstance;
    /**
     * 非持久化的map
     */
    private Map<String, String> cookieMap;
    private Gson gson;
    private SPUtil spUtil;

    private MyCookieJar() {
        cookieMap = new HashMap<>();
        gson = new Gson();
        spUtil = SPUtil.getInstance(COOKIE_FILE_NAME);
    }

    public static MyCookieJar getInstance() {
        if (mInstance == null) {
            synchronized (MyCookieJar.class) {
                if (mInstance == null) {
                    mInstance = new MyCookieJar();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        //从响应体里取cookie保存到内存
        /*StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            sb.append(cookie);
            sb.append("/n");
        }
        String size = "size: " + cookies.size();
        sb.append(size);
        LogUtil.e(TAG, "url: " + url.toString() + "\ncookies: " + sb.toString());*/
        cookieMap.put(url.toString(), gson.toJson(cookies));
        spUtil.put(url.toString(), gson.toJson(cookies));
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        //取cookie返回
        String json = cookieMap.get(url.toString());
        LogUtil.e(TAG, "tempCookies: " + json);
        if (TextUtils.isEmpty(json)) {
            //说明内存中没有，取本地cookie
            json = spUtil.get(url.toString(), "");
        }
        //内存中有，直接返回
        return gson.fromJson(json, new TypeToken<List<Cookie>>() {
        }.getType());
    }
}
