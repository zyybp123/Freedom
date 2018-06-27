package com.bpz.commonlibrary.net.interceptor;

import android.support.annotation.NonNull;

import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ZYY
 * on 2018/1/8 21:39.
 */

public class Interceptors {
    private static final String TAG = "Interceptors";

    /**
     * http日志拦截器会影响上传下载功能，测试相关功能要关闭
     */
    @NonNull
    public static HttpLoggingInterceptor getLogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                //LogUtil.e(TAG, "log: " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    @NonNull
    public static Interceptor getHeaderInterceptor(String baseUrl) {
        return new HeaderInterceptor(baseUrl);
    }
}
