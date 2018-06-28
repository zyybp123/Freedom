package com.bpz.commonlibrary.net.interceptor;

import android.support.annotation.NonNull;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.interf.SomeFields;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.POST;

/**
 * 缓存控制拦截器
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (LibApp.needCache) {
            //拦截请求
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            //获取当前请求方法
            String method = request.method();
            if (SomeFields.GET.equals(method)) {
                //get请求

            } else if (SomeFields.POST.equals(method)) {
                //post请求
            } else {
                return chain.proceed(request);
            }
            List<String> headerValues = request.headers(SomeFields.FR_CACHE_CONTROL);
        }
        return chain.proceed(chain.request());
    }
}
