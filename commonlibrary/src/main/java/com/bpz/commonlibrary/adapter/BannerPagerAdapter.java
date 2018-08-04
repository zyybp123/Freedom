package com.bpz.commonlibrary.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.base.BasePagerAdapter;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.ui.banner.PBanner;
import com.bpz.commonlibrary.util.LogUtil;

import java.util.List;

/**
 * banner的viewPager的数据适配器
 *
 * @param <T>
 */
public class BannerPagerAdapter<T> extends BasePagerAdapter<T> {
    private static final String TAG = "BannerPagerAdapter";
    private OnItemClickListener<T> clickListener;
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

    public BannerPagerAdapter(List<T> mList, OnImgShowListener<T> listener,
                              int defaultEmptyImg, OnItemClickListener<T> clickListener) {
        super(mList);
        this.listener = listener;
        this.clickListener = clickListener;
        this.defaultEmptyImg = defaultEmptyImg;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        //总数小于等于1不必循环了
        return count <= 1 ? count : Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public View getItemView(ViewGroup container, final int position) {
        LogUtil.e(TAG, "getItemView: " + position);
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int realPosition = mList.size() <= 1 ? position : position % mList.size();
        final T data = mList.get(realPosition);
        if (data == null) {
            imageView.setImageResource(defaultEmptyImg);
        } else {
            if (listener != null) {
                listener.onImageShow(imageView, data);
            }
            if (clickListener != null) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(position, data);
                    }
                });
            }
        }
        return imageView;
    }
}
