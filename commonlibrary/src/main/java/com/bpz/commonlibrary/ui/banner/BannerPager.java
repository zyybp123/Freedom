package com.bpz.commonlibrary.ui.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.bpz.commonlibrary.adapter.BannerPagerAdapter;

import java.util.ArrayList;

/**
 * 用来做轮播图的view Pager
 */
public class BannerPager extends ViewPager implements ViewPager.OnPageChangeListener {
    private static final String TAG = "BannerPager";

    public BannerPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        addOnPageChangeListener(this);
        //setAdapter(new BannerPagerAdapter(new ArrayList()));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

    }

    @Override
    public void onPageSelected(int position) {
        //int realPosition = stringList.size() <= 1 ? position : position % stringList.size();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
