package com.bpz.commonlibrary.interf.listener;

import android.widget.ImageView;

/**
 * 设置图片显示的接口
 *
 * @param <T>
 */
public interface OnImgShowListener<T> {
    void onImageShow(ImageView imageView, T data);
}
