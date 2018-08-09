package com.bpz.commonlibrary.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.mvp.BaseListView;
import com.bpz.commonlibrary.ui.recycler.MyDragListener;
import com.bpz.commonlibrary.ui.recycler.MyTouchHelperCallback;
import com.bpz.commonlibrary.ui.recycler.TouchListener;
import com.bpz.commonlibrary.ui.widget.StateLayout;
import com.bpz.commonlibrary.util.ListUtil;
import com.bpz.commonlibrary.util.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRefreshFragment<T> extends BaseFragment
        implements OnRefreshLoadMoreListener, BaseListView<T>,
        TouchListener, MyDragListener {
    public StateLayout mStateLayout;
    public View refreshView;
    public SmartRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public List<T> mDataList = new ArrayList<>();
    /**
     * 分页加载时的当前页
     */
    public int mCurrentPage = 1;
    /**
     * 默认允许下拉刷新,不允许上拉加载更多
     */
    public boolean refreshEnabled = true;
    public boolean loadMoreEnabled = false;
    /**
     * 是否还有更多数据
     */
    public boolean hasMoreData = true;
    /**
     * 是否在下拉刷新中
     */
    public boolean onRefreshing = false;
    /**
     * 是否在上拉加载更多中
     */
    public boolean onLoadingMore = false;
    /**
     * 上拉加载更多失败
     */
    public boolean onLoadMoreFail = false;
    /**
     * 下拉刷新失败
     */
    public boolean onRefreshFail = false;
    /**
     * 是否允许长按拖拽条目
     */
    public boolean canMove = false;
    public ItemTouchHelper mItemTouchHelper;
    /**
     * 默认不允许条目的侧滑
     */
    public boolean canSwipe = false;
    /**
     * 是否允许拖拽手柄移动条目
     */
    public boolean canDragHandle = false;


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

        mItemTouchHelper = new ItemTouchHelper(
                new MyTouchHelperCallback(this, canMove, canSwipe)
        );
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        onLoading();
        //设置下拉刷新，上拉加载更多的监听器
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        //设置是否自动加载更多
        mRefreshLayout.setEnableAutoLoadMore(false);
        //设置不满一屏不显示上拉加载更多
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        //设置是否下拉刷新和上拉加载更多
        mRefreshLayout.setEnableLoadMore(loadMoreEnabled);
        mRefreshLayout.setEnableRefresh(refreshEnabled);
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
    public boolean onMove(int fromPosition, int toPosition) {
        if (mAdapter != null) {
            Collections.swap(mDataList, fromPosition, toPosition);
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onSwipe(int position) {
        if (mAdapter != null) {
            itemSwipe(position);
        }
    }

    @Override
    public void onTouchFinish(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (!canSwipe && mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 子类复写此方法可自行处理左右的滑动事件
     *
     * @param position 当前条目索引
     */
    public void itemSwipe(int position) {
        mDataList.remove(ListUtil.getCorrectPosition(mDataList, position));
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (canDragHandle && mItemTouchHelper != null) {
            mItemTouchHelper.startDrag(viewHolder);
        }
    }

    @Override
    public void getListDataSuccess(@NotNull List<T> data) {
        showSuccess();
        if (!onRefreshing && !onLoadingMore){
            mDataList.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
        if (!refreshEnabled) {
            //不允许下拉刷新
            if (!loadMoreEnabled) {
                //不允许上拉加载更多，每次加载重置状态，设置数据即可
                resetDataStatus();
                mDataList.addAll(data);
                mAdapter.notifyDataSetChanged();
            } else {
                dealLoadMore(data);
            }
        } else {
            //有下拉刷新
            if (onRefreshing && !onRefreshFail) {
                //在下拉刷新中，且下拉刷新成功，更新数据
                LogUtil.e(mFragmentTag, "refresh end");
                mDataList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh();
                onRefreshing = false;
            }
            if (loadMoreEnabled) {
                dealLoadMore(data);
            }
        }
    }

    public void showSuccess() {
        if (mStateLayout != null) {
            mStateLayout.showCurrentPage(StateLayout.State.ON_SUCCESS);
        }
    }

    private void resetDataStatus() {
        onRefreshFail = false;
        onLoadMoreFail = false;
        onRefreshing = false;
        onLoadingMore = false;
        if (mDataList != null) {
            mDataList.clear();
        }
    }

    public void dealLoadMore(List<T> data) {
        if (onLoadingMore && !onLoadMoreFail) {
            //上拉加载更多中且上拉加载更多未失败
            mDataList.addAll(data);
            mAdapter.notifyDataSetChanged();
            if (hasMoreData) {
                LogUtil.e(mFragmentTag, "load more end");
                //还有更多数据
                mRefreshLayout.finishLoadMore();
            } else {
                //显示全部加载完成，并不再触发加载更事件
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
            onLoadingMore = false;
            onLoadMoreFail = false;
        }
    }

    @Override
    public void refreshFail() {
        //下拉刷新失败
        onRefreshing = false;
        onRefreshFail = true;
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(false);
        }
    }

    @Override
    public void loadMoreFail() {
        //上拉加载更多失败
        onLoadingMore = false;
        onLoadMoreFail = true;
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        canLoadMore();
    }

    public void canLoadMore() {
        LogUtil.e(mFragmentTag, "canLoadMore currentPage: " + mCurrentPage);
        if (hasMoreData) {
            //有更多数据，上拉加载更多
            onLoadingMore = true;
            if (!onLoadMoreFail) {
                //如果上拉加载更多数没有失败，才继续加页
                mCurrentPage++;
            }
            toLoadMore();
        }
    }

    protected abstract void toLoadMore();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //刷新中，重置集合，当前页数，刷新中的标识，加载更多的标识
        hasMoreData = true;
        onRefreshing = true;
        onLoadingMore = false;
        onLoadMoreFail = false;
        onRefreshFail = false;
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
