package com.bpz.commonlibrary.ui.banner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.BannerPagerAdapter;
import com.bpz.commonlibrary.adapter.base.BasePagerAdapter;
import com.bpz.commonlibrary.ui.widget.LinearContainer;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.PeriodicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 自定一个banner控件
 * 属性：指示器，数字，标题的相对位置，轮播间隔时间
 * 指示器的宽高，颜色，间距,是否自动循环，是否循环
 * 模式：标题，数字，数字+标题，指示器，指示器+标题，
 */
public class PBanner<T> extends FrameLayout{
    private static final String TAG = "PBanner";
    /**
     * 指示器层的宽，高，背景颜色
     */
    private float ilWidth;
    private float ilHeight;
    @ColorInt
    private int ilColor = Color.argb(126, 0, 0, 0);
    /**
     * 指示器，宽，高，选中颜色，默认颜色，间距
     */
    private float piWidth;
    private float piHeight;
    @ColorInt
    private int piSelectedColor;
    @ColorInt
    private int piNormalColor;
    private float piMargin;
    /**
     * 标题，数字，大小和颜色
     */
    private int mTitleSize;
    @ColorInt
    private int mTitleColor;
    private int mNumSize;
    @ColorInt
    private int mNumColor;
    private int showModel;
    /**
     * 轮播间隔时间
     */
    private int periodicTime = 3000;

    private RecyclerView mRecycler;
    private FrameLayout mFLIndicator;
    private TextView mTvTitle;
    private TextView mTvNum;
    private LinearContainer mLCIndicator;
    private PeriodicUtil periodicUtil;
    private int currentPage = -1;
    private List<T> mDataList;
    private ImgShowListener<T> imgShowListener;
    @DrawableRes
    private int defaultEmptyImg = R.drawable.fr_empty;
    private boolean isAutoLoop = true;

    public PBanner(Context context) {
        this(context, null);
    }

    public PBanner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        View rootView = View.inflate(context, R.layout.fr_p_banner_layout, this);
        mRecycler = rootView.findViewById(R.id.p_banner_rv);
        mFLIndicator = rootView.findViewById(R.id.p_banner_fl_in);
        mTvTitle = rootView.findViewById(R.id.p_banner_title);
        mTvNum = rootView.findViewById(R.id.p_banner_num);
        mLCIndicator = rootView.findViewById(R.id.p_banner_indicator);
    }


    public interface Model {
        int NONE = 0;
        int ONLY_NUM = 1;
        int ONLY_TITLE = 2;
        int TITLE_NUM = 3;
        int ONLY_INDICATOR = 4;
        int TITLE_INDICATOR = 5;
    }

    public interface ImgShowListener<T> {
        /**
         * 此方法为外部实现，imageView的展示
         *
         * @param imageView 图片控件
         * @param data      banner数据
         */
        void setImgShow(ImageView imageView, T data);
    }


}
