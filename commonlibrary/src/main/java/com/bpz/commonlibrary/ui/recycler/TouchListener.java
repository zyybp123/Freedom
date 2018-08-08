package com.bpz.commonlibrary.ui.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * 监听RecyclerView的条目移动，侧滑的事件
 */
public interface TouchListener {
    boolean onMove(int fromPosition, int toPosition);

    void onSwipe(int position);

    void onTouchFinish(RecyclerView.ViewHolder viewHolder, int actionState);
}
