package com.bpz.commonlibrary.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.base.BasePagerAdapter;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.ui.banner.PBanner;
import com.bpz.commonlibrary.util.LogUtil;

import java.util.ArrayList;
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
    private View.OnTouchListener touchListener;
    private List<View> imgViewList = new ArrayList<>();
    private int count;
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
                              int defaultEmptyImg, OnItemClickListener<T> clickListener
    ,View.OnTouchListener touchListener
    ) {
        super(mList);
        this.listener = listener;
        this.clickListener = clickListener;
        this.touchListener = touchListener;
        this.defaultEmptyImg = defaultEmptyImg;
    }


    @Override
    public int getCount() {
        int dataSize = super.getCount();
        //总数小于等于1不必循环了
        count = dataSize <= 1 ? dataSize : dataSize + 2;
        return count;
    }

    @NonNull
    @Override
    public View getItemView(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final int realPosition;
        if (mList.size() <= 1){
            realPosition = position;
        }else {
            if (position == 0) {
                //显示数据列表的最后一个
                realPosition = mList.size() - 1;
            } else if (position == mList.size() + 1) {
                //显示数据列表第一个
                realPosition = 0;
            } else {
                realPosition = position - 1;
            }
        }
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
                        clickListener.onItemClick(v,realPosition, data);
                    }
                });
            }
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (touchListener != null){
                       return touchListener.onTouch(v,event);
                    }
                    return false;
                }
            });
        }
        return imageView;
    }
}
