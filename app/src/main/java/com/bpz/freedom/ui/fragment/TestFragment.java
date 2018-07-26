package com.bpz.freedom.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpz.commonlibrary.ui.fragment.BaseRefreshFragment;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.ImgUrl;
import com.bpz.freedom.R;
import com.bpz.freedom.adapter.Adapter2Test;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class TestFragment extends BaseRefreshFragment<Integer> {
    @Override
    public RecyclerView.Adapter getAdapter() {
        return new Adapter2Test(R.layout.item_test, mDataList);
    }

    @Override
    public void initialRequest() {
        showFooter = true;
        getRequest();
    }

    public void getRequest() {
        new Thread() {
            @Override
            public void run() {
                final ArrayList<Integer> numbers = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    numbers.add(i);
                }
                try {
                    Thread.sleep(3000);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getListDataSuccess(numbers);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    onError("getList", e.getMessage());
                }
            }
        }.start();
    }

    @Override
    protected void toLoadMore() {
        getRequest();
        if (mCurrentPage >= 3) {
            hasMore = false;
        }
        LogUtil.e(mFragmentTag, "load more....");
        LogUtil.e(mFragmentTag,"current page is: " + mCurrentPage);
    }

    @Override
    protected void toRefresh() {
        getRequest();
        LogUtil.e(mFragmentTag, "refresh....");
    }


    @Override
    public void onSubscribe(String methodTag, Disposable d) {

    }

    @Override
    public void onError(String methodTag, String describe) {

    }

    @Override
    public void onEmpty(String methodTag) {

    }

    @Override
    public void noNet() {

    }


}
