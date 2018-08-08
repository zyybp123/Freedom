package com.bpz.commonlibrary.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyBaseViewHolder extends RecyclerView.ViewHolder {
    public View handleView;

    public MyBaseViewHolder(View itemView) {
        super(itemView);
        handleView = getHandleView();
    }

    /**
     * 子类复写此方法可获取一个拖拽手柄的view对象
     */
    public View getHandleView() {
        return itemView;
    }
}
