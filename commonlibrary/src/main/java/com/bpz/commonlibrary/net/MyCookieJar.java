package com.bpz.commonlibrary.net;

import android.support.annotation.NonNull;

import com.bpz.commonlibrary.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
    private static final String TAG = "MyCookieJar";
    /**
     * 非持久化的map
     */
    private Map<String, String> cookieMap = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        //从响应体里取cookie保存到本地
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            sb.append(cookie);
            sb.append("/n");
        }
        String size = "size: " + cookies.size();
        sb.append(size);
        LogUtil.e(sb.toString());
        cookieMap.put(url.toString(), gson.toJson(cookies));
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        //从本地取cookie返回
        String json = cookieMap.get(url.toString());
        LogUtil.e(TAG, json);
        return gson.fromJson(json,
                new TypeToken<List<Cookie>>() {
                }.getType());
    }
}
