package com.bpz.commonlibrary.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.BannerIndicatorAdapter;
import com.bpz.commonlibrary.adapter.BannerPagerAdapter;
import com.bpz.commonlibrary.interf.BannerData;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.ui.widget.LinearContainer;
import com.bpz.commonlibrary.util.ImageLoad;
import com.bpz.commonlibrary.util.LogUtil;
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
        OnItemClickListener<T>, OnImgShowListener<T>, ViewPager.OnPageChangeListener,
        View.OnTouchListener {
    private static final String TAG = "PBanner";
    /**
     * 因为只作为banner用，所以控制其数据量最大为10个
     */
    private static final int MAX_DATA_SIZE = 10;
    /**
     * 指示器层的宽，高，背景颜色
     */
    private float ilHeight = 60;
    @ColorInt
    private int ilColor = Color.argb(126, 0, 0, 0);
    /**
     * 指示器，宽，高，选中颜色，默认颜色，间距，圆角
     */
    private float piWidth = 10;
    private float piHeight = 10;
    @ColorInt
    private int piSelectedColor = Color.parseColor("#99FFFFFF");
    @ColorInt
    private int piNormalColor = Color.GRAY;
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
    private int currentPosition;
    private LoopTask task;

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
        ilHeight = typeArray.getDimension(R.styleable.PBanner_ilHeight, ilHeight);
        ilColor = typeArray.getColor(R.styleable.PBanner_ilColor, ilColor);
        piWidth = typeArray.getDimension(R.styleable.PBanner_piWidth, piWidth);
        piHeight = typeArray.getDimension(R.styleable.PBanner_piHeight, piHeight);
        piMargin = typeArray.getDimension(R.styleable.PBanner_piMargin, piMargin);
        piRadius = typeArray.getDimension(R.styleable.PBanner_piRadius, piRadius);
        piNormalColor = typeArray.getColor(R.styleable.PBanner_piNormalColor, piNormalColor);
        piSelectedColor = typeArray.getColor(R.styleable.PBanner_piSelectedColor, piSelectedColor);
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
                defaultEmptyImg, this, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        //mViewPager.setOnTouchListener(this);
        setModel(showModel);
        setLocation(mLocation);
        //配置指示器区域的宽高，背景颜色
        setFLIndicatorSize(ilHeight);
        setFLIndicatorBackgroundColor(ilColor);
        //配置指示器的宽高，选中的颜色等
        setPiSize(piWidth, piHeight, piNormalColor, piSelectedColor, piRadius, piMargin);
        //配置定时任务
        task = new LoopTask();
        periodicUtil = new PeriodicUtil(task, periodicTime);

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
                mFLIndicator.setBackgroundColor(Color.TRANSPARENT);
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

    public void setFLIndicatorSize(float height) {
        if (mFLIndicator == null) {
            return;
        }
        ViewGroup.LayoutParams params = mFLIndicator.getLayoutParams();
        if (params != null) {
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
                          @ColorInt int piSelectedColor, float piRadius, float piMargin) {
        if (mLCIndicator != null) {
            indicatorAdapter = new BannerIndicatorAdapter<>(mDataList,
                    piWidth, piHeight, piNormalColor, piSelectedColor, piRadius, piMargin);
            mLCIndicator.setAdapter(indicatorAdapter);
        }
    }

    public void setDataList(List<T> dataList) {
        if (mAdapter == null || dataList == null || dataList.size() == 0
                || indicatorAdapter == null || mViewPager == null) {
            return;
        }
        mDataList.clear();
        if (dataList.size() > MAX_DATA_SIZE) {
            for (int i = 0; i < MAX_DATA_SIZE; i++) {
                mDataList.add(dataList.get(i));
            }
        } else {
            mDataList.addAll(dataList);
        }
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(1);
        indicatorAdapter.notifyDataSetChanged();
    }

    public void setBannerListener(BannerListener<T> bannerListener) {
        this.bannerListener = bannerListener;
    }

    @Override
    public void onItemClick(View v, int position, T itemData) {
        //跳转的链接
        if (bannerListener != null) {
            bannerListener.onItemClick(v, position, itemData);
        }
    }

    @Override
    public void onImageShow(ImageView imageView, T data) {
        String bannerImgUrl = data.getBannerImgUrl();
        ImageLoad.glideLoad(LibApp.mContext, bannerImgUrl, imageView);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        if (mDataList == null || mDataList.size() == 0 || indicatorAdapter == null) {
            return;
        }
        int realPosition;
        if (mDataList.size() <= 1) {
            realPosition = position;
        } else {
            if (position == 0) {
                //显示数据列表的最后一个
                realPosition = mDataList.size() - 1;
            } else if (position == mDataList.size() + 1) {
                //显示数据列表第一个
                realPosition = 0;
            } else {
                realPosition = position - 1;
            }
        }
        LogUtil.e(TAG, "realPosition: " + realPosition);
        indicatorAdapter.setCurrentItem(realPosition);
        if (mTvTitle == null) {
            return;
        }
        mTvTitle.setText(mDataList.get(realPosition).getBannerTitle());
        if (mTvNum == null) {
            return;
        }
        mTvNum.setText(String.format(Locale.getDefault(), "%d/%d",
                realPosition + 1, mDataList.size()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int count = mDataList.size();
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                //No operation
                if (currentPosition == 0) {
                    mViewPager.setCurrentItem(count, false);
                } else if (currentPosition == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                //start Sliding
                if (currentPosition == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                } else if (currentPosition == 0) {
                    mViewPager.setCurrentItem(count, false);
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING://end Sliding
                break;
        }
    }

    public void onDestroy() {
        if (periodicUtil != null) {
            periodicUtil = null;
        }
        if (task != null) {
            task = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                autoStop();
                break;
            case MotionEvent.ACTION_UP:
                autoStart();
                break;
        }
        return false;
    }

    public void autoStop() {
        if (canLoop()) {
            periodicUtil.stop();
        }
    }

    public void autoStart() {
        if (canLoop()) {
            periodicUtil.start();
        }
    }

    private boolean canLoop() {
        return isAutoLoop && periodicUtil != null && mDataList.size() > 1;
    }

    public interface Model {
        int NONE = 0;
        int ONLY_NUM = 1;
        int ONLY_TITLE = 2;
        int ONLY_INDICATOR = 3;
    }

    public interface Location {
        int START = 0;
        int CENTER = 1;
        int END = 2;
    }

    public interface BannerListener<T> extends OnItemClickListener<T> {
        //void onImageShow(ImageView imageView, T data);
    }

    public class LoopTask implements PeriodicUtil.Task {
        @Override
        public void execute() {
            //回调到主线程
            new Handler(Looper.getMainLooper())
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mViewPager == null || mDataList == null ||
                                    mDataList.size() == 0) {
                                return;
                            }
                            currentPosition++;
                            mViewPager.setCurrentItem(currentPosition);
                        }
                    }, 500);
        }
    }
}
