package com.bpz.commonlibrary.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.base.BasePagerAdapter;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.ui.banner.PBanner;

import java.util.List;

/**
 * banner的viewPager的数据适配器
 *
 * @param <T>
 */
public class BannerPagerAdapter<T> extends BasePagerAdapter<T> {
    private static final String TAG = "BannerPagerAdapter";
    private OnImgShowListener<T> listener;
    @DrawableRes
    private int defaultEmptyImg = R.drawable.fr_empty;

    public BannerPagerAdapter(List<T> mList) {
        super(mList);
    }

    public BannerPagerAdapter(List<T> mList, OnImgShowListener<T> listener) {
        super(mList);
        this.listener = listener;
    }

    public BannerPagerAdapter(List<T> mList, OnImgShowListener<T> listener, int defaultEmptyImg) {
        super(mList);
        this.listener = listener;
        this.defaultEmptyImg = defaultEmptyImg;
    }

    @NonNull
    @Override
    public View getItemView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        T data = mList.get(position);
        if (data == null) {
            imageView.setImageResource(defaultEmptyImg);
        } else {
            if (listener != null) {
                listener.onImageShow(imageView, data);
            }
        }
        return imageView;
    }
}
