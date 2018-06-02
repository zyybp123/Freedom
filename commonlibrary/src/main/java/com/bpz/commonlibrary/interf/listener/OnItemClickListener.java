package com.bpz.commonlibrary.interf.listener;

/**
 * 点击事件传递接口
 *
 * @param <T>
 */
public interface OnItemClickListener<T> {
    void onItemClick(int position, T itemData);
}
