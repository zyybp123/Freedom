package com.bpz.commonlibrary.net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BaseObserver<T> implements Observer<Response<T>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response<T> tResponse) {
    }


    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
