package com.bpz.commonlibrary.mvp;

import io.reactivex.disposables.Disposable;

public interface BaseView {

    void onSubscribe(String methodTag, Disposable d);

    void onLoading();

    /**
     * 发起请求时出现的各种异常
     * 如没有网络，连接超时，解析异常等导致的错误
     *
     * @param methodTag 请求方法标识
     * @param describe  错误描述
     */
    void onError(String methodTag, String describe);

    /**
     * 空页面
     * @param methodTag 请求方法标识
     */
    void onEmpty(String methodTag);

    void noNet();
}
