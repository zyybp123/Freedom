package com.bpz.freedom;

import com.bpz.freedom.entity.ResultEntity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseObserver<T> implements Observer<ResultEntity<T>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ResultEntity<T> tResultEntity) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
