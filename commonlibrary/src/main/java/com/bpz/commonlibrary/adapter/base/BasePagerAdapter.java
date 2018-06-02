package com.bpz.commonlibrary.adapter.base;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ZYY
 * on 2018/1/20 10:15.
 */

public abstract class BasePagerAdapter<T> extends PagerAdapter {
    protected List<T> mList;
    private boolean isNotify;

    public BasePagerAdapter(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = getItemView(container, position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (!isNotify) {
            return super.getItemPosition(object);
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        isNotify = true;
        super.notifyDataSetChanged();
        isNotify = false;
    }

    @NonNull
    public abstract View getItemView(ViewGroup container, int position);
}
