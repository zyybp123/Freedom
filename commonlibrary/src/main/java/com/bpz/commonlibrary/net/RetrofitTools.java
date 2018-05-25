package com.bpz.commonlibrary.net;

import android.support.annotation.NonNull;

import com.bpz.commonlibrary.interf.AppEnvironment;
import com.bpz.commonlibrary.net.interceptor.Interceptors;
import com.bpz.commonlibrary.net.interceptor.ProgressInterceptor;
import com.google.gson.Gson;


import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/1/4.
 * retrofit请求方法统一管理类
 */

public class RetrofitTools {

    /**
     * 本类实例
     */
    private static RetrofitTools mInstance = null;
    private OkHttpClient okClient;
    private Retrofit retrofit;

    /**
     * 构造 初始化相应参数
     *
     * @param baseUrl 基础URL
     */
    private RetrofitTools(String baseUrl, Map<String, String> baseUrlMap) {
        //设置OkHttpClitent;
        okClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.MINUTES)        //读取超时 60min
                .connectTimeout(2, TimeUnit.MINUTES)      //连接超时 2min
                .writeTimeout(60, TimeUnit.MINUTES)       //写超时   60min
                .retryOnConnectionFailure(true)                   //是否自动重连
                //.sslSocketFactory(sslContext.getSocketFactory())//证书配置
                .addInterceptor(Interceptors.getHeaderInterceptor(baseUrlMap, baseUrl))//添加header拦截器
                .addInterceptor(new ProgressInterceptor())//添加进度拦截器
                //.addNetworkInterceptor(Interceptors.getLogInterceptor())//添加日志拦截器,大文件下载会产生OOM
                //.cookieJar(new MyCookieJar())//添加cookie的处理
                .build();
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)// Baseurl 必须以/结尾
                .addConverterFactory(GsonConverterFactory.create())// 添加json转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxJava
                .client(okClient)
                .build();
    }

    /**
     * 单例模式获取实例
     *
     * @param baseUrl 基础URL
     * @return 返回本类实例
     */
    public static RetrofitTools getInstance(String baseUrl, Map<String, String> baseUrlMap) {
        if (mInstance == null) {
            synchronized (RetrofitTools.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitTools(baseUrl, baseUrlMap);
                }
            }
        }
        return mInstance;
    }

    /**
     * 定义的线程转换方法
     *
     * @param <T> 泛型
     * @return 订阅在io线程，观察者在计算线程（非io，cpu密集计算，例如图形计算）
     */
    @NonNull
    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> setComputationThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation());
            }
        };
    }

    /**
     * 定义的线程转换方法
     *
     * @param <T> 泛型
     * @return 订阅在io线程，观察者在主线程
     */

    @NonNull
    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> setMainThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 获取retrofit，可以将服务接口独立出来
     *
     * @return 返回retrofit 对象
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 获取okClient
     *
     * @return 返回RetrofitClient
     */
    public OkHttpClient getOkClient() {
        return okClient;
    }

}
