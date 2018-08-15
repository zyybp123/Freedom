package com.bpz.commonlibrary.ui.filter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpz.commonlibrary.LibApp;
import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.util.DimensionUtil;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;

public class FilterBarTab extends LinearLayout {
    private static final String TAG = "FilterBarTab";
    public View mRootView;
    private TextView mTvTitle;
    private ImageView mIvArrow;
    /**
     * 标签在筛选栏里的索引
     */
    private int position;
    /**
     * 总的标签数
     */
    private int count;
    /**
     * 文字选中和未选中的颜色
     */
    @ColorInt
    private int mNormalColor = Color.GRAY;
    @ColorInt
    private int mSelectColor = Color.BLACK;

    public FilterBarTab(@NonNull Context context) {
        this(context, null);
    }

    public FilterBarTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterBarTab(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mRootView = LayoutInflater
                .from(context)
                .inflate(R.layout.fr_base_filter_tab_layout, this);
        mTvTitle = mRootView.findViewById(R.id.tv_filter_title);
        mIvArrow = mRootView.findViewById(R.id.iv_filter_arrow);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }

    public void setSelectColor(int mSelectColor) {
        this.mSelectColor = mSelectColor;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mTvTitle == null || mIvArrow == null){
            return;
        }
        if (count > 0) {
            if (position < 0) {
                position = 0;
            }
            if (position >= count) {
                position = count - 1;
            }
            //有总数，且索引在总数范围内总数
            mTvTitle.setMaxWidth(DimensionUtil.totalSize(LibApp.mContext).x / count);
            //mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
        }
        //变更选中状态
        mTvTitle.setTextColor(selected ? mSelectColor : mNormalColor);
        //可以根据需求实现，变更颜色，方向等
        mIvArrow.setRotation(selected ? 180 : 0);
    }

    public void setTitle(String barTitle) {
        if (mTvTitle != null){
            LogUtil.e(TAG,"title: " + barTitle);
            mTvTitle.setText(StringUtil.getNotNullStr(barTitle));
        }
    }
}
