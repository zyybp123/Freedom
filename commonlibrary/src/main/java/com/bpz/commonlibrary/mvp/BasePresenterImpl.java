package com.bpz.commonlibrary.mvp;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {
    public V mView;


    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
