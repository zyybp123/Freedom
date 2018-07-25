package com.bpz.commonlibrary.ui.fragment;

public class BaseRefreshFragment extends BaseFragment {
    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return 0;
    }

    @Override
    public void viewHasBind() {

    }

    @Override
    public void initialRequest() {

    }
}
