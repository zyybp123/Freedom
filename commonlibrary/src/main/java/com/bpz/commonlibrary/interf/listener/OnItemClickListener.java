package com.bpz.commonlibrary.interf.listener;

import android.view.View;

/**
 * 点击事件传递接口
 *
 * @param <T>
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view,int position, T itemData);
}
