package com.bpz.commonlibrary.ui.banner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.adapter.base.BasePagerAdapter;
import com.bpz.commonlibrary.ui.widget.LinearContainer;
import com.bpz.commonlibrary.util.PeriodicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 自定一个banner控件
 * 属性：指示器，数字，标题的相对位置，轮播间隔时间
 * 指示器的宽高，颜色，间距
 * 模式：标题，数字，数字+标题，指示器，指示器+标题，
 */
public class PBanner<T> extends FrameLayout implements PeriodicUtil.Task, ViewPager.OnPageChangeListener {
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

    private ViewPager mViewPager;
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
        periodicUtil = new PeriodicUtil(this, periodicTime);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = View.inflate(context, R.layout.fr_p_banner_layout, this);
        mViewPager = rootView.findViewById(R.id.p_banner_vp);
        mFLIndicator = rootView.findViewById(R.id.p_banner_fl_in);
        mTvTitle = rootView.findViewById(R.id.p_banner_title);
        mTvNum = rootView.findViewById(R.id.p_banner_num);
        mLCIndicator = rootView.findViewById(R.id.p_banner_indicator);
        mViewPager.addOnPageChangeListener(this);
    }

    public void setData(ImgShowListener<T> imgShowListener, List<T> mDataList) {
        this.imgShowListener = imgShowListener;
        this.mDataList = mDataList;
        makeListSizeOne();
        if (mViewPager != null) {
            mViewPager.setAdapter(new Adapter2Pager(mDataList, imgShowListener));
        }
    }

    private void makeListSizeOne() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (mDataList.size() == 0) {
            mDataList.add(null);
        }
        if (mDataList.size() == 1) {
            //只有一个元素，不轮询
            isAutoLoop = false;
        }
    }

    public void startLoop() {
        if (periodicUtil != null && isAutoLoop) {
            periodicUtil.start();
        }
    }

    public void stopLoop() {
        if (periodicUtil != null) {
            periodicUtil.stop();
        }
    }

    @Override
    public void execute() {
        //此处执行定时任务,处于子线程
        if (currentPage >= -1 && currentPage <= mDataList.size()) {
            currentPage++;
        } else {
            currentPage = 0;
        }
        new Handler(getContext().getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(currentPage);
            }
        }, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //当前页
        currentPage = position;
        T data = mDataList.get(position);
        if (data != null) {
            mTvTitle.setText(data.toString());
            mTvNum.setText(String.format(Locale.getDefault(), "%d/%d", position, mDataList.size()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    public class Adapter2Pager extends BasePagerAdapter<T> {
        private ImgShowListener<T> listener;

        public Adapter2Pager(List<T> mList) {
            super(mList);
        }

        public Adapter2Pager(List<T> mList, ImgShowListener<T> listener) {
            super(mList);
            this.listener = listener;
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
                    listener.setImgShow(imageView, data);
                }
            }
            return imageView;
        }
    }


}
