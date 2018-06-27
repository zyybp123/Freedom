package com.bpz.commonlibrary.net;

import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import com.bpz.commonlibrary.manager.ACache;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.SPUtil;
import com.bpz.commonlibrary.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
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
    private SimpleArrayMap<String, String> cookieMap;
    private Gson gson;
    private SPUtil spUtil;

    private MyCookieJar() {
        cookieMap = new SimpleArrayMap<>();
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
        //从响应体里取cookie保存到内存，和本地
        String cookiesJson = gson.toJson(cookies);
        if (!StringUtil.isSpace(cookiesJson)) {
            cookieMap.put(url.toString(), cookiesJson);
            spUtil.put(url.toString(), cookiesJson);
        }
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        //取cookie返回,注意，返回的不能为null
        String json = cookieMap.get(url.toString());
        if (TextUtils.isEmpty(json)) {
            //说明内存中没有，取本地cookie
            json = spUtil.get(url.toString(), "");
        }
        LogUtil.e(TAG, "tempCookies: " + json);
        //内存中有，直接返回
        List<Cookie> cookies = gson.fromJson(json, new TypeToken<List<Cookie>>() {
        }.getType());
        if (cookies == null) {
            //没有，也不能为null
            cookies = new ArrayList<>();
        }
        return cookies;
    }
}
