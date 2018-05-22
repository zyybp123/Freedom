package com.bpz.commonlibrary.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/2/23.
 * 线性布局填充标签，数据适配器的基类
 */

public abstract class BaseLinearAdapter {
    protected String msgTag = this.getClass().getSimpleName();
    /**
     * 数据变化监听器
     */
    private OnDataChanged onDataChanged;

    /**
     * 获取标签的数量
     */
    public abstract int getTabCount();

    /**
     * 获取标签的View对象
     *
     * @param parent   容器
     * @param position 索引
     * @return 返回View对象
     */
    public abstract View getTabView(ViewGroup parent, int position);

    /**
     * 设置条目点击事件
     *
     * @param itemView 条目View
     * @param parent   容器
     * @param position 索引
     */
    public abstract void onItemClick(View itemView, ViewGroup parent, int position);

    /**
     * 数据源发生变化则调用对应的方法
     */
    public void notifyDataSetChanged() {
        onDataChanged.onChange();
    }

    public void setOnDataChanged(OnDataChanged onDataChanged) {
        this.onDataChanged = onDataChanged;
    }

    public interface OnDataChanged {
        void onChange();
    }
}
