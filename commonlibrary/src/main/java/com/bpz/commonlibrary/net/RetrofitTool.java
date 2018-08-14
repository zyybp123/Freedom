package com.bpz.commonlibrary.net;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bpz.commonlibrary.interf.ConfigFields;
import com.bpz.commonlibrary.net.interceptor.BaseInterceptor;
import com.bpz.commonlibrary.net.interceptor.ProgressInterceptor;


import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/1/4.
 * retrofit请求方法统一管理类
 */

public class RetrofitTool {

    /**
     * 本类实例
     */
    private static RetrofitTool mInstance = null;
    private OkHttpClient okClient;
    private OkHttpClient.Builder okBuilder;
    private Retrofit retrofit;

    private RetrofitTool(String baseUrl) {
        //okBuilder = new OkHttpClient.Builder();
        //设置OkHttpClitent;
        okClient = new OkHttpClient.Builder()
                //协议配置
                //.protocols(Collections.unmodifiableList(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2)))
                .readTimeout(ConfigFields.READ_TIME_OUT, TimeUnit.MINUTES)
                .connectTimeout(ConfigFields.CONNECT_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(ConfigFields.WRITE_TIME_OUT, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                //.sslSocketFactory(sslContext.getSocketFactory())//证书配置
                .addInterceptor(new BaseInterceptor(baseUrl))
                .addInterceptor(new ProgressInterceptor())
                //.addNetworkInterceptor(Interceptors.getLogInterceptor())
                .cookieJar(MyCookieJar.getInstance())//添加cookie的处理
                //.cache(NetCacheManager.getCache(null))
                .build();
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())// 添加json转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxJava
                .client(okClient)
                .build();
    }

    public static RetrofitTool getInstance(String baseUrl) {
        if (mInstance == null) {
            synchronized (RetrofitTool.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitTool(baseUrl);
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
