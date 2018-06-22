package com.bpz.commonlibrary.ui.fragment;

import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bpz.commonlibrary.R;

public class BaseWebFragment extends BaseFragment {
    public WebView mWebView;
    public ProgressBar mProgressBar;

    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return R.layout.fr_base_web_fragment_layout;
    }

    @Override
    public void viewHasBind() {
        mWebView = mRootView.findViewById(R.id.fr_web_view);
        mProgressBar = mRootView.findViewById(R.id.fr_pb_loading);

    }

    @Override
    public void initialRequest() {

    }
}
