package com.bpz.commonlibrary.net.interceptor;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.interf.ConfigFields;
import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.manager.NetCacheManager;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.PackageUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.http.POST;

/**
 * 缓存控制拦截器
 */
public class CacheInterceptor implements Interceptor {
    private static final String TAG = "CacheInterceptor";
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        return chain.proceed(chain.request());
    }
}
