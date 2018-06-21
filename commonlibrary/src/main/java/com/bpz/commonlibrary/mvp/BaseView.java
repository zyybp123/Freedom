package com.bpz.commonlibrary.mvp;

public interface BaseView {
    void onLoading();

    void onSuccess();

    void onEmpty();

    void onFailure();

    void noNet();
}
