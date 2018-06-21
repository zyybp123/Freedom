package com.bpz.commonlibrary.mvp;

public interface BasePresenter<V extends BaseView> {
    /**
     * 关联界面
     */
    void attachView(V view);

    /**
     * 解除界面关联
     */
    void detachView();
}
