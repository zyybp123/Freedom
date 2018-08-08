package com.bpz.freedom.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bpz.commonlibrary.mvp.BasePresenter;
import com.bpz.commonlibrary.mvp.BaseView;
import com.bpz.commonlibrary.ui.fragment.BaseFragment;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.tzq.LoginInfo;
import com.bpz.freedom.net.NetEaseHost;
import com.bpz.freedom.presenter.NetEasePresenter;
import com.bpz.freedom.ui.LoginPresenter;
import com.bpz.freedom.ui.view.LoginView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import io.reactivex.disposables.Disposable;

public class TestFragment2 extends BaseFragment<LoginPresenter> implements LoginView {
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
    public void initialRequest() {
        mPresenter.doLogin("17600108092", "a123456");
        mStateLayout.showCurrentPage(StateLayout.State.ON_LOADING);
        //NetEasePresenter easePresenter = new NetEasePresenter();
        //easePresenter.preload();
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void viewHasBind() {
        mStateLayout = mRootView.findViewById(R.id.state_layout);
        mStateLayout.setOpenDefault(false);
        textView = new TextView(mActivity);
        textView.setTextSize(16);
        textView.setTextColor(Color.RED);
        TextView view = new TextView(mActivity);
        view.setText("加载失败！");

        TextView view1 = new TextView(mActivity);
        view1.setText("为空！");


        TextView view2 = new TextView(mActivity);
        view2.setText("No net !");

        mStateLayout.setStatePage(StateLayout.State.ON_SUCCESS, textView)
                .setStatePage(StateLayout.State.ON_LOADING, new ProgressBar(mActivity))
                .setStatePage(StateLayout.State.ON_FAIL, view)
                .setStatePage(StateLayout.State.ON_EMPTY, view1)
                .setStatePage(StateLayout.State.ON_NO_NET, view2)
        ;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void onLoginSuccess(LoginInfo loginInfo) {
        LogUtil.e("name : " + loginInfo.getUsername());
        mStateLayout.showCurrentPage(StateLayout.State.ON_SUCCESS);
        textView.setText(loginInfo.getXytoken());


        Thread thread = new Thread() {
            int count = -1;

            @Override
            public void run() {

                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(3000);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mStateLayout.showCurrentPage(count);
                                count++;
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
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
