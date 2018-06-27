package com.bpz.freedom.ui.fragment;

import android.graphics.Color;
import android.widget.TextView;

import com.bpz.commonlibrary.ui.fragment.BaseFragment;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.ui.LoginPresenter;
import com.bpz.freedom.ui.LoginView;

import io.reactivex.disposables.Disposable;

public class TestFragment2 extends BaseFragment implements LoginView {
    StateLayout mStateLayout;
    TextView textView;
    LoginPresenter presenter;
    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return R.layout.test;
    }

    @Override
    public void viewHasBind() {
        mStateLayout = mRootView.findViewById(R.id.state_layout);
        textView = new TextView(mActivity);
        textView.setTextSize(16);
        textView.setTextColor(Color.RED);
        mStateLayout.setStatePage(StateLayout.State.ON_SUCCESS, textView);
    }

    @Override
    public void initialRequest() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
        presenter.doLogin("17600108092","a123456");
        mStateLayout.showCurrentPage(StateLayout.State.ON_LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null){
            presenter.detachView();
        }
    }

    @Override
    public void onLoginSuccess(LoginInfo loginInfo) {
        LogUtil.e("name : " + loginInfo.getUsername());
        mStateLayout.showCurrentPage(StateLayout.State.ON_SUCCESS);
        textView.setText(loginInfo.getXytoken());
    }

    @Override
    public void onLoginFailure(int code, String describe) {
        LogUtil.e("error......");
        mStateLayout.showCurrentPage(StateLayout.State.ON_FAIL);
    }

    @Override
    public void onSubscribe(String methodTag, Disposable d) {

    }

    @Override
    public void onLoading() {
        mStateLayout.showCurrentPage(StateLayout.State.ON_LOADING);
    }

    @Override
    public void onError(String methodTag, String describe) {
        mStateLayout.showCurrentPage(StateLayout.State.ON_FAIL);
    }

    @Override
    public void onEmpty(String methodTag) {
        mStateLayout.showCurrentPage(StateLayout.State.ON_EMPTY);
    }

    @Override
    public void noNet() {
        mStateLayout.showCurrentPage(StateLayout.State.ON_NO_NET);
    }
}
