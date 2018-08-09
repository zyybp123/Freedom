package com.bpz.freedom.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpz.commonlibrary.mvp.BasePresenter;
import com.bpz.commonlibrary.mvp.BaseView;
import com.bpz.commonlibrary.ui.banner.PBanner;
import com.bpz.commonlibrary.ui.fragment.BaseRefreshFragment;
import com.bpz.commonlibrary.ui.recycler.RecycleViewDivider;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.freedom.ImgUrl;
import com.bpz.freedom.R;
import com.bpz.freedom.adapter.Adapter2Test;
import com.bpz.freedom.adapter.Adapter2Test2;
import com.bpz.freedom.entity.BannerEntity;
import com.bpz.freedom.entity.TestEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class TestFragment extends BaseRefreshFragment<TestEntity> {
    List<BannerEntity> list = new ArrayList<>();
    PBanner<BannerEntity> banner;

    @Override
    public void initialRequest() {
        getRequest();
        //mRefreshLayout.autoRefresh();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        canMove = true;
        /*mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity,
                LinearLayoutManager.HORIZONTAL,1, Color.BLUE));*/
        //canSwipe = true;
        loadMoreEnabled = true;
        banner = new PBanner<>(mActivity);
        banner.setModel(PBanner.Model.ONLY_INDICATOR);
        banner.setLocation(PBanner.Location.CENTER);
        banner.setDataList(list);
        banner.setBannerListener(new PBanner.BannerListener<BannerEntity>() {
            @Override
            public void onItemClick(int position, BannerEntity itemData) {
                //条目点击事件
                String bannerClickUrl = itemData.getBannerClickUrl();
                //webViewFragmentN.loadWebPage(bannerClickUrl);
            }
        });
        Adapter2Test2 adapter2Test = new Adapter2Test2(mDataList, this);
        return adapter2Test;
    }

    @Override
    protected void toLoadMore() {
        getRequest();

    }

    @Override
    protected void toRefresh() {
        getRequest();
        LogUtil.e(mFragmentTag, "refresh....");
    }

    public void getRequest() {
        new Thread() {
            @Override
            public void run() {
                final ArrayList<TestEntity> numbers = new ArrayList<>();
                for (int i = 0; i < ImgUrl.IMG_S.length; i++) {
                    list.add(new BannerEntity(ImgUrl.IMG_S[i], ImgUrl.IMG_S[i], "标题" + (i + 1)));
                }
                //numbers.add(new TestEntity(Adapter2Test2.ITEM_BANNER,list));
                for (int i = 0; i < 20; i++) {
                    numbers.add(new TestEntity(Adapter2Test2.ITEM_LIST, i));
                }
                try {
                    if (mCurrentPage >= 3) {
                        loadMoreFail();
                        return;
                    }
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
    protected BasePresenter<BaseView> getPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.autoStart();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.autoStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (banner != null) {
            banner.onDestroy();
        }
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
