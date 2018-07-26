package com.bpz.commonlibrary.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    public void initialRequest() {
        //初始化请求的位置
    }

    @Override
    public void getListDataSuccess(List<T> data) {
        showSuccess();
        if (onRefreshing) {
            //刷新中，重置集合
            mDataList.clear();
            mCurrentPage = 1;
        }
        mDataList.addAll(data);
        mAdapter.notifyDataSetChanged();
        if (onRefreshing) {
            mRefreshLayout.finishRefresh(true);
            onRefreshing = false;
        }
        mRefreshLayout.finishLoadMore();
    }

    public void showSuccess() {
        if (mStateLayout != null) {
            mStateLayout.showCurrentPage(StateLayout.State.ON_SUCCESS);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (showFooter) {
            //允许显示加载更多时
            if (mCurrentPage == 1) {
                if (!canUp()) {
                    //当前为第一页，且不可上滑，说明数据不满一屏
                    mRefreshLayout.setEnableLoadMore(false);
                } else {
                    //当前为第一页，可以上滑动
                    canLoadMore();
                }
            } else {
                //大于第二页
                canLoadMore();
            }
        } else {
            //禁止上拉加载更多
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    /**
     * @return 返回false表示不能向上滚动了, 否则可以上滑
     */
    public boolean canUp() {
        return mRecyclerView != null && mRecyclerView.canScrollVertically(1);
    }

    public void canLoadMore() {
        if (hasMore) {
            mRefreshLayout.setEnableLoadMore(true);
            mCurrentPage++;
            toLoadMore();
        } else {
            mRefreshLayout.setNoMoreData(true);
        }
    }

    protected abstract void toLoadMore();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefreshing = true;
        toRefresh();
    }

    protected abstract void toRefresh();

    /**
     * @return 返回false表示不能向下滚动了, 否则可以下滑
     */
    public boolean canDown() {
        return mRecyclerView != null && mRecyclerView.canScrollVertically(-1);
    }


}
