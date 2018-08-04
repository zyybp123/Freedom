package com.bpz.commonlibrary.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.Adapter2Banner;
import com.bpz.commonlibrary.adapter.BannerIndicatorAdapter;
import com.bpz.commonlibrary.adapter.BannerPagerAdapter;
import com.bpz.commonlibrary.interf.listener.BannerData;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.ui.widget.LinearContainer;
import com.bpz.commonlibrary.util.ImageLoad;
import com.bpz.commonlibrary.util.PeriodicUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 自定一个banner控件
 * 属性：指示器，数字，标题的相对位置，轮播间隔时间
 * 指示器的宽高，颜色，间距,是否自动循环，是否循环
 * 模式：标题，数字，数字+标题，指示器，指示器+标题，
 */
public class PBanner<T extends BannerData> extends FrameLayout implements
        OnItemClickListener<T>, OnImgShowListener<T> {
    private static final String TAG = "PBanner";
    /**
     * 指示器层的宽，高，背景颜色
     */
    private float ilWidth = 500;
    private float ilHeight = 250;
    @ColorInt
    private int ilColor = Color.argb(126, 0, 0, 0);
    /**
     * 指示器，宽，高，选中颜色，默认颜色，间距，圆角
     */
    private float piWidth = 10;
    private float piHeight = 10;
    @ColorInt
    private int piSelectedColor = Color.WHITE;
    @ColorInt
    private int piNormalColor = Color.parseColor("#99000000");
    private float piMargin = 10;
    private float piRadius = 5;
    /**
     * 标题，数字，大小和颜色
     */
    private float mTitleSize = 14;
    @ColorInt
    private int mTitleColor = Color.WHITE;
    private float mNumSize = 14;
    @ColorInt
    private int mNumColor = Color.WHITE;
    private int showModel = Model.NONE;
    /**
     * 轮播间隔时间
     */
    private int periodicTime = 3000;
    /**
     * 指示器的位置
     */
    private int mLocation = Location.CENTER;

    private ViewPager mViewPager;
    private FrameLayout mFLIndicator;
    private TextView mTvTitle;
    private TextView mTvNum;
    private LinearContainer mLCIndicator;
    private PeriodicUtil periodicUtil;
    private int currentPage = -1;
    private List<T> mDataList = new ArrayList<>();
    @DrawableRes
    private int defaultEmptyImg = R.drawable.fr_empty;
    private boolean isAutoLoop = true;
    private BannerPagerAdapter<T> mAdapter;
    private BannerListener<T> bannerListener;
    private BannerIndicatorAdapter<T> indicatorAdapter;

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

    private void init(@NotNull Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PBanner, 0, 0);
        ilWidth = typeArray.getDimension(R.styleable.PBanner_ilWidth, ilWidth);
        ilHeight = typeArray.getDimension(R.styleable.PBanner_ilHeight, ilHeight);
        ilColor = typeArray.getColor(R.styleable.PBanner_ilColor, ilColor);
        piWidth = typeArray.getDimension(R.styleable.PBanner_piWidth, piWidth);
        piHeight = typeArray.getDimension(R.styleable.PBanner_piHeight, piHeight);
        piMargin = typeArray.getDimension(R.styleable.PBanner_piMargin, piMargin);
        piNormalColor = typeArray.getColor(R.styleable.PBanner_piMargin, piNormalColor);
        piSelectedColor = typeArray.getColor(R.styleable.PBanner_piMargin, piSelectedColor);
        mTitleSize = typeArray.getDimension(R.styleable.PBanner_mTitleSize, mTitleSize);
        mTitleColor = typeArray.getColor(R.styleable.PBanner_mTitleColor, mTitleColor);
        mNumSize = typeArray.getDimension(R.styleable.PBanner_mNumSize, mNumSize);
        mNumColor = typeArray.getColor(R.styleable.PBanner_mNumColor, mNumColor);
        showModel = typeArray.getInt(R.styleable.PBanner_showModel, Model.NONE);
        mLocation = typeArray.getInt(R.styleable.PBanner_showModel, Location.CENTER);
        periodicTime = typeArray.getInt(R.styleable.PBanner_periodicTime, periodicTime);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = View.inflate(context, R.layout.fr_p_banner_layout, this);
        mViewPager = rootView.findViewById(R.id.p_banner_vp);
        mFLIndicator = rootView.findViewById(R.id.p_banner_fl_in);
        mTvTitle = rootView.findViewById(R.id.p_banner_title);
        mTvNum = rootView.findViewById(R.id.p_banner_num);
        mLCIndicator = rootView.findViewById(R.id.p_banner_indicator);
        mAdapter = new BannerPagerAdapter<>(mDataList, this,
                defaultEmptyImg, this);
        mViewPager.setAdapter(mAdapter);
        setModel(showModel);
        setLocation(mLocation);
        //配置指示器区域的宽高，背景颜色
        setFLIndicatorSize(ilWidth, ilHeight);
        setFLIndicatorBackgroundColor(ilColor);
        //配置指示器的宽高，选中的颜色等
        setPiSize(piWidth, piHeight, piNormalColor, piSelectedColor, piRadius);

    }

    public void setModel(int model) {
        if (mFLIndicator == null || mTvTitle == null || mTvNum == null || mLCIndicator == null) {
            return;
        }
        mFLIndicator.setVisibility(VISIBLE);
        mTvTitle.setVisibility(VISIBLE);
        mLCIndicator.setVisibility(VISIBLE);
        mTvNum.setVisibility(VISIBLE);
        switch (model) {
            case Model.NONE:
                mFLIndicator.setVisibility(GONE);
                break;
            case Model.ONLY_NUM:
                mTvTitle.setVisibility(GONE);
                mLCIndicator.setVisibility(GONE);
                break;
            case Model.ONLY_TITLE:
                mLCIndicator.setVisibility(GONE);
                mTvNum.setVisibility(GONE);
                break;
            case Model.ONLY_INDICATOR:
                mTvNum.setVisibility(GONE);
                mTvTitle.setVisibility(GONE);
                break;
        }
    }

    public void setLocation(int location) {
        if (mTvTitle == null || mTvNum == null || mLCIndicator == null) {
            return;
        }
        FrameLayout.LayoutParams params = (LayoutParams) mTvTitle.getLayoutParams();
        if (params == null) {
            return;
        }
        switch (location) {
            case Location.START:
                //左
                params.gravity = Gravity.START;
                break;
            case Location.CENTER:
                //中
                params.gravity = Gravity.CENTER;
                break;
            case Location.END:
                //右
                params.gravity = Gravity.END;
                break;
        }
        mTvTitle.setLayoutParams(params);
        mTvNum.setLayoutParams(params);
        mLCIndicator.setLayoutParams(params);
    }

    public void setFLIndicatorSize(float width, float height) {
        if (mFLIndicator == null) {
            return;
        }
        ViewGroup.LayoutParams params = mFLIndicator.getLayoutParams();
        if (params != null) {
            params.width = (int) width;
            params.height = (int) height;
            mFLIndicator.setLayoutParams(params);
        }
    }

    public void setFLIndicatorBackgroundColor(@ColorInt int color) {
        if (mFLIndicator == null) {
            return;
        }
        mFLIndicator.setBackgroundColor(color);
    }

    public void setPiSize(float piWidth, float piHeight, @ColorInt int piNormalColor,
                          @ColorInt int piSelectedColor, float piRadius) {
        if (mLCIndicator != null) {
            indicatorAdapter = new BannerIndicatorAdapter<>(mDataList,
                    piWidth, piHeight, piNormalColor, piSelectedColor, piRadius);
            mLCIndicator.setAdapter(indicatorAdapter);
        }
    }

    public void setDataList(List<T> dataList) {
        if (mAdapter == null || dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(dataList);
        mAdapter.notifyDataSetChanged();

    }

    public void setBannerListener(BannerListener<T> bannerListener) {
        this.bannerListener = bannerListener;
    }

    @Override
    public void onItemClick(int position, T itemData) {
        //跳转的链接
        if (bannerListener != null) {
            bannerListener.onItemClick(position, itemData);
        }
    }

    @Override
    public void onImageShow(ImageView imageView, T data) {
        String bannerImgUrl = data.getBannerImgUrl();
        ImageLoad.glideLoad(LibApp.mContext, bannerImgUrl, imageView);
        /*if (bannerListener != null) {
            bannerListener.onImageShow(imageView, data);
        }*/
    }


    public interface Model {
        int NONE = 0;
        int ONLY_NUM = 1;
        int ONLY_TITLE = 2;
        int ONLY_INDICATOR = 3;
        //int TITLE_NUM = 3;
        //int TITLE_INDICATOR = 5;
    }

    public interface Location {
        int START = 0;
        int CENTER = 1;
        int END = 2;
    }

    public interface BannerListener<T> extends OnItemClickListener<T> {
        //void onImageShow(ImageView imageView, T data);
    }
}
