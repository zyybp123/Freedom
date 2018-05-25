package com.bpz.commonlibrary.net.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.util.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/2.
 * 专门处理多baseUrl的情况，和统一配置请求头的拦截器
 */

public class HeaderInterceptor implements Interceptor {
    private static final String TAG = "HeaderInterceptor";
    private Map<String, String> baseUrlMap;
    private String baseUrl;

    HeaderInterceptor(Map<String, String> baseUrlMap, String baseUrl) {
        this.baseUrlMap = baseUrlMap;
        this.baseUrl = baseUrl;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request newRequest = chain.request();
        Request.Builder builder = chain.request().newBuilder();
        //builder.addHeader("","");为所有请求配上统一的请求头,在此设置
        //假如token获取失败可在此处处理，重新获取
        //从request中获取headers，通过给定的键
        List<String> headerValues = newRequest.headers(SomeFields.URL_FLAG);
        if (headerValues != null && headerValues.size() > 0) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader(SomeFields.URL_FLAG);
            //匹配获得新的BaseUrl
            String headerValue = headerValues.get(0);
            LogUtil.e(TAG, "headerValue: " + headerValue);
            HttpUrl newBaseUrl = null;
            if (TextUtils.isEmpty(headerValue)) {
                //为空值，取默认URL
                newBaseUrl = HttpUrl.parse(baseUrl);
            } else {
                //在此处筛选url，并配置独立的header

                //不为空值，从map里取值
                String url = baseUrlMap.get(headerValue);
                if (TextUtils.isEmpty(url)) {
                    //取出来空值，取默认URL
                    newBaseUrl = HttpUrl.parse(baseUrl);
                } else {
                    newBaseUrl = HttpUrl.parse(url);
                }
            }
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
                return chain.proceed(builder.url(newFullUrl).build());
            } else {
                return chain.proceed(newRequest);
            }
        } else {
            return chain.proceed(newRequest);
        }
    }
}
