package com.bpz.commonlibrary.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * 控制viewPager的循环
 *
 * @param <T>
 */
public class PPagerAdapter<T> extends PagerAdapter {
    private List<T> mDataList;
    private boolean canLoop = true;
    //private boolean canAutoLoop = true;

    @Override
    public int getCount() {

        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    public int getListSize() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
