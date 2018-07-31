package com.bpz.commonlibrary.net.interceptor;

import android.support.annotation.NonNull;

import com.bpz.commonlibrary.interf.SomeFields;
import com.bpz.commonlibrary.net.body.FileResponseBody;
import com.bpz.commonlibrary.util.LogUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/25.
 * 下载进度监听拦截器
 */

public class ProgressInterceptor implements Interceptor {
    private static final String TAG = "ProgressInterceptor";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        String url = chain.request().url().toString();
        List<String> headers = originalResponse.headers(SomeFields.CONTENT_DISPOSITION);
        String content_disposition = null;
        if (headers != null && headers.size() > 0) {
            content_disposition = headers.get(0);
        }
        LogUtil.e(TAG, "Content_Disposition：" + content_disposition);
        //返回写入了监听器的响应体
        return originalResponse.newBuilder()
                .body(new FileResponseBody(url, originalResponse.body(),content_disposition))
                .build();
    }
}
