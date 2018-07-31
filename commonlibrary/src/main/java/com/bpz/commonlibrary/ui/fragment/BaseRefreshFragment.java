package com.bpz.commonlibrary.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.mvp.BaseListView;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.LogUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRefreshFragment<T> extends BaseFragment
        implements OnRefreshLoadMoreListener, BaseListView<T> {
    public StateLayout mStateLayout;
    public View refreshView;
    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public List<T> mDataList = new ArrayList<>();
    public int mCurrentPage = 1;
    /**
     * 默认显示下拉刷新头，不显示上拉加载更多
     */
    public boolean showFooter = false;
    public boolean showHeader = true;
    public boolean hasMore = true;
    public boolean onRefreshing = false;


    @Override
    public boolean isNeedLazy() {
        return false;
    }

    @Override
    public int getRootViewLayoutId() {
        return R.layout.fr_base_refresh_fragment;
    }

    @Override
    public void initialRequest() {
        //初始化请求的位置
    }

    @Override
    public void viewHasBind() {
        mStateLayout = mRootView.findViewById(R.id.fr_state_layout);
        refreshView = View.inflate(mActivity, R.layout.fr_base_refresh_layout, null);
        mRefreshLayout = refreshView.findViewById(R.id.fr_refresh_layout);
        mRecyclerView = refreshView.findViewById(R.id.fr_recycler_view);
        mStateLayout.setStatePage(StateLayout.State.ON_SUCCESS, refreshView);
        mAdapter = getAdapter();
        if (mAdapter == null) {
            LogUtil.e("get adapter is null !");
            return;
        }
        mLayoutManager = getLayoutManager();
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,
                    false);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        onLoading();
        //设置下拉刷新，上拉加载更多的监听器
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        //设置是否自动加载更多
        mRefreshLayout.setEnableAutoLoadMore(false);
        //设置不满一屏是否显示上拉加载更多
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setEnableLoadMore(showFooter);
    }

    /**
     * 获取recyclerView的Adapter
     */
    public abstract RecyclerView.Adapter getAdapter();

    public RecyclerView.LayoutManager getLayoutManager() {
        //默认返回垂直的布局管理器
        return new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onLoading() {
        if (mStateLayout != null) {
            mStateLayout.showCurrentPage(StateLayout.State.ON_LOADING);
        }
    }

    @Override
    public void getListDataSuccess(List<T> data) {
        showSuccess();
        mDataList.addAll(data);
        mAdapter.notifyDataSetChanged();
        if (onRefreshing) {
            LogUtil.e(mFragmentTag, "refresh end");
            mRefreshLayout.finishRefresh();
            onRefreshing = false;
        }
        if (showFooter) {
            if (hasMore) {
                LogUtil.e(mFragmentTag, "load more end");
                //还有更多数据
                mRefreshLayout.finishLoadMore();
            } else {
                //显示全部加载完成，并不再触发加载更事件
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    public void showSuccess() {
        if (mStateLayout != null) {
            mStateLayout.showCurrentPage(StateLayout.State.ON_SUCCESS);
        }
    }

    @Override
    public void refreshFail() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(false);
        }
    }

    @Override
    public void loadMoreFail() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadMore(false);
        }
    }

    /**
     * @return 返回false表示不能向上滚动了, 否则可以上滑
     */
    public boolean canUp() {
        return mRecyclerView != null && !mRecyclerView.canScrollVertically(1);
    }

    /**
     * 继续获取数据，子类需要覆盖此方法
     */
    public void getMoreOrStop() {
        LogUtil.e(mFragmentTag, "getMoreOrStop");
        //默认处理，不加载更多
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        canLoadMore();
    }

    public void canLoadMore() {
        LogUtil.e(mFragmentTag, "canLoadMore currentPage: " + mCurrentPage);
        if (hasMore) {
            mCurrentPage++;
            toLoadMore();
        }
    }

    protected abstract void toLoadMore();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //刷新中，重置集合，当前页数，刷新中的标识，加载更多的标识
        onRefreshing = true;
        hasMore = true;
        mCurrentPage = 1;
        mDataList.clear();
        mRefreshLayout.setNoMoreData(false);
        toRefresh();
    }

    protected abstract void toRefresh();

    /**
     * @return 返回false表示不能向下滚动了, 否则可以下滑
     */
    public boolean canDown() {
        return mRecyclerView != null && !mRecyclerView.canScrollVertically(-1);
    }


}
