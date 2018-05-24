package com.bpz.commonlibrary.ui.pop;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 * 作为独立模块使用的页面基类
 */

public abstract class BasePage<T> {
    public List<T> data;
    public OnItemClickListener<T> listener;
    public boolean isWrapContent;
    protected Context context;
    /**
     * 页面根布局
     */
    private View mRootView;

    BasePage(Context context, List<T> data, OnItemClickListener<T> listener, boolean isWrapContent) {
        this.context = context;
        this.data = data;
        this.listener = listener;
        this.isWrapContent = isWrapContent;
        this.mRootView = loadRootView();
    }

    /**
     * 加载页面根布局
     *
     * @return 返回View对象
     */
    public abstract View loadRootView();

    /**
     * 获取页面根布局
     *
     * @return 返回根布局View对象
     */
    public View getMRootView() {
        return mRootView;
    }

    /**
     * 获取页面标识
     *
     * @return 返回当前类的类名
     */
    public String getPageTag() {
        return this.getClass().getName();
    }

    public interface OnItemClickListener<T> {
        void onClick(int position, T data);
    }
}
