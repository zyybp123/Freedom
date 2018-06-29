package com.bpz.commonlibrary.net.interceptor;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.interf.ConfigFields;
import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.interf.listener.OnHeaderOptionListener;
import com.bpz.commonlibrary.manager.NetCacheManager;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.PackageUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 基本拦截器
 * 处理：1.请求头的添加
 * 2.缓存控制
 */
public class BaseInterceptor implements Interceptor {
    private static final String TAG = "BaseInterceptor";
    private String baseUrl;
    private OnHeaderOptionListener listener;

    public BaseInterceptor(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request newRequest = chain.request();
        Request.Builder builder = chain.request().newBuilder();
        //从request中获取headers，通过给定的键
        return controlUrlAndHeader(chain, newRequest, builder);
    }

    private Response controlUrlAndHeader(@NonNull Chain chain,
                                         @NonNull @NotNull Request newRequest,
                                         @NonNull Request.Builder builder) throws IOException {
        Response response;
        List<String> headerValues = newRequest.headers(SomeFields.URL_FLAG);
        List<String> cacheHeader = newRequest.headers(SomeFields.FR_CACHE_CONTROL);
        boolean needCache = false;
        if (cacheHeader != null && cacheHeader.size() > 0) {
            builder.removeHeader(SomeFields.FR_CACHE_CONTROL);
            needCache = true;
        }
        String newUrl = baseUrl;
        if (headerValues != null && headerValues.size() > 0) {
            //如果有这个header，先将配置的header删除，此header仅用作app和okhttp之间使用
            builder.removeHeader(SomeFields.URL_FLAG);
            //拿到BaseUrl在map中的key
            String headerValue = headerValues.get(0);
            HttpUrl newBaseUrl;
            if (!TextUtils.isEmpty(headerValue)) {
                //key不为空值，从map里取值
                String url = LibApp.mBaseUrlMap.get(headerValue);
                if (!TextUtils.isEmpty(url)) {
                    //取出来不为空值
                    newUrl = url;
                }
            }
            if (listener != null) {
                listener.onHeaderOption(builder, newRequest, headerValue);
            }
            newBaseUrl = HttpUrl.parse(newUrl);
            LogUtil.e(TAG, "newBaseUrl: " + newBaseUrl);
            if (newBaseUrl != null) {
                //从request中获取原有的HttpUrl实例oldHttpUrl
                HttpUrl oldHttpUrl = newRequest.url();
                //重建新的HttpUrl，修改需要修改的url部分
                HttpUrl newFullUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(newBaseUrl.scheme())
                        .host(newBaseUrl.host())
                        .port(newBaseUrl.port())
                        .build();
                //重建request，返回一个response至此结束修改
                response = chain.proceed(builder.url(newFullUrl).build());
            } else {
                response = chain.proceed(newRequest);
            }
        } else {
            response = chain.proceed(newRequest);
        }
        if (needCache) {
            makeCache(newUrl, response);
        }
        return response;
    }

    private void makeCache(String url, Response response) throws IOException {
        String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + PackageUtil.getPackageName(LibApp.mContext)
                + ConfigFields.DEFAULT_CACHE_NAME;
        //缓存响应体到内存和硬盘中
        NetCacheManager
                .getInstance(cachePath)
                .saveInMem(url, new Gson().toJson(response));
        NetCacheManager
                .getInstance(cachePath)
                .saveInDisk(url, response.body());
    }
}
