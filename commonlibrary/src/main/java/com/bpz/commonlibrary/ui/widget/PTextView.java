package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.entity.StrokeEntity;
import com.bpz.commonlibrary.util.BgResUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Administrator on 2018/5/7.
 * 自定义的textView,改的是背景图片
 * <p>
 * 1.指定圆角的大小。
 * 2.分别指定不同方向的圆角大小。
 * 3.支持分别指定背景色和边框色，指定颜色时支持使用 color 或 ColorStateList
 */

public class PTextView extends AppCompatTextView {
    public static final String TAG = "PTextView";
    float cornerRadius = 4;
    float leftTopRadius;
    float rightTopRadius;
    float rightBottomRadius;
    float leftBottomRadius;

    int color = Color.TRANSPARENT;
    ColorStateList colorStateList;

    float strokeWidth;
    int strokeColor;
    ColorStateList strokeColorStateList;
    //int

    public PTextView(Context context) {
        this(context, null);
    }

    public PTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(@NotNull Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PTextView, 0, 0);
        colorStateList = typeArray.getColorStateList(R.styleable.PTextView_pColor);
        leftTopRadius = typeArray.getDimension(R.styleable.PTextView_pLeftTopCRadius, leftTopRadius);
        rightTopRadius = typeArray.getDimension(R.styleable.PTextView_pRightTopCRadius, rightTopRadius);
        rightBottomRadius = typeArray.getDimension(R.styleable.PTextView_pRightBottomCRadius, rightBottomRadius);
        leftBottomRadius = typeArray.getDimension(R.styleable.PTextView_pLeftBottomCRadius, leftBottomRadius);
        strokeWidth = typeArray.getDimension(R.styleable.PTextView_strokeWidth, strokeWidth);
        strokeColorStateList = typeArray.getColorStateList(R.styleable.PTextView_strokeColor);
        initView();
    }

    private void initView() {
        StrokeEntity strokeEntity = new StrokeEntity(true);
        strokeEntity.color = strokeColorStateList;
        strokeEntity.width = (int) strokeWidth;
        setBackground(BgResUtil.getRecBg(colorStateList, strokeEntity,
                leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius));
    }


}
