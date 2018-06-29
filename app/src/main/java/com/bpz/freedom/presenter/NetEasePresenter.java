package com.bpz.freedom.presenter;

import android.support.annotation.NonNull;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.mvp.BasePresenterImpl;
import com.bpz.commonlibrary.net.RetrofitTool;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.BaseObserver;
import com.bpz.freedom.entity.NetEaseEntity;
import com.bpz.freedom.net.NetEaseHost;
import com.bpz.freedom.service.NetEaseService;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NetEasePresenter extends BasePresenterImpl {
    private static final String TAG = "NetEasePresenter";
    private NetEaseService service;

    public NetEasePresenter() {
        service = RetrofitTool
                .getInstance(NetEaseHost.BASE_URL_NET_EASE)
                .getRetrofit()
                .create(NetEaseService.class);
    }

    public void preload() {
        service.preload()
                .compose(RetrofitTool.<NetEaseEntity>setMainThread())
                .subscribe(new Observer<NetEaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NetEaseEntity s) {
                        LogUtil.e(TAG, "result: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "e: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
