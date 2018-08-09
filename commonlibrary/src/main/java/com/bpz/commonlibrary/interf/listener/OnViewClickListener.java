package com.bpz.commonlibrary.interf.listener;

import android.view.View;

public interface OnViewClickListener<T> {
    void onClick(View v, T data);
}
