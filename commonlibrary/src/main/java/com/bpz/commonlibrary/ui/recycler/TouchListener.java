package com.bpz.commonlibrary.ui.recycler;

import android.support.v7.widget.RecyclerView;

public interface TouchListener {
    boolean onMove(int fromPosition, int toPosition);

    void onSwipe(int position);

    void onTouchFinish(RecyclerView.ViewHolder viewHolder, int actionState);
}
