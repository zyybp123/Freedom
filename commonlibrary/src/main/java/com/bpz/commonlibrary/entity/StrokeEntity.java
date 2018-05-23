package com.bpz.commonlibrary.entity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import com.bpz.commonlibrary.util.LogUtil;


/**
 * Created by Administrator on 2018/5/8.
 * 包装了边框信息的数据实体
 * <stroke
 * android:width="1dp"
 * android:color="#234"
 * android:dashGap="3dp"
 * android:dashWidth="5dp" />
 */

public class StrokeEntity {
    private static final String TAG = "StrokeEntity";
    /**
     * 如果形状为线，其值应该小于所应用的控件的高度
     */
    public int width;
    public ColorStateList color;
    /**
     * 虚线的间隔
     */
    public float dashGap;
    /**
     * 虚线的宽
     */
    public float dashWidth;
    /**
     * 是否是实线
     */
    public boolean isSolidLine;

    public StrokeEntity() {
    }

    public StrokeEntity(boolean isSolidLine) {
        this.isSolidLine = isSolidLine;
    }

    public static void setStroke(GradientDrawable drawable, StrokeEntity strokeEntity) {
        if (drawable == null || strokeEntity == null) {
            LogUtil.e(TAG, "drawable or strokeEntity is null !");
            return;
        }
        LogUtil.e(TAG, "width--->" + strokeEntity.width + ", color--->" + strokeEntity.color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (strokeEntity.isSolidLine) {
                drawable.setStroke(strokeEntity.width, strokeEntity.color);
            } else {
                drawable.setStroke(strokeEntity.width, strokeEntity.color,
                        strokeEntity.dashWidth, strokeEntity.dashGap);
            }
        }else{
            int currColor = strokeEntity.color.getColorForState(drawable.getState(), Color.TRANSPARENT);
            if (strokeEntity.color == null){
                setLine(drawable, strokeEntity, Color.TRANSPARENT);
            }else{
                setLine(drawable, strokeEntity, currColor);
            }
        }
    }

    private static void setLine(GradientDrawable drawable, StrokeEntity strokeEntity, int transparent) {
        if (strokeEntity.isSolidLine) {
            drawable.setStroke(strokeEntity.width, transparent);
        } else {
            drawable.setStroke(strokeEntity.width, transparent,
                    strokeEntity.dashWidth, strokeEntity.dashGap);
        }
    }
}
