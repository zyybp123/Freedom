package com.bpz.commonlibrary.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Administrator on 2018/5/14.
 * 尺寸工具类
 */

public class DimensionUtil {
    private DimensionUtil() {

    }

    /**
     * 根据基准设备的宽获取目标设备的宽
     *
     * @param context             上下文
     * @param standardScreenValue 基准设备的屏幕宽度
     * @param standardValue       在基准设备上的值
     * @return 返回在目标设备上的宽
     */
    public static int getActualWidth(Context context, int standardScreenValue, int standardValue) {
        int targetScreenWidth = totalSize(context).x;
        return standardValue * targetScreenWidth / standardScreenValue;
    }

    /**
     * 获取屏幕的宽高
     *
     * @param context 上下文
     * @return 返回 Point x为宽，y为高
     */
    @NonNull
    public static Point totalSize(@NotNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int mTotalWidth = dm.widthPixels;
        int mTotalHeight = dm.heightPixels;
        return new Point(mTotalWidth, mTotalHeight);
    }

    /**
     * 根据基准设备的宽获取目标设备的高
     *
     * @param context             上下文
     * @param standardScreenValue 基准设备的屏幕高度
     * @param standardValue       在基准设备上的值
     * @return 返回在目标设备上的高
     */
    public static int getActualHeight(Context context, int standardScreenValue, int standardValue) {
        int targetScreenHeight = totalSize(context).y;
        return standardValue * targetScreenHeight / standardScreenValue;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return 返回px值
     */
    public static int dip2px(@NotNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return 返回dp值
     */
    public static int px2dip(@NotNull Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取状态栏高度（单位：px）
     *
     * @return 状态栏高度（单位：px）
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 根据设备信息获取当前分辨率下指定单位对应的像素大小； px,dip,sp -> px
     *
     * @param context 上下文
     * @param unit    单位 TypeValue.COMPLEX_UNIT_PX:
     *                COMPLEX_UNIT_DIP:
     *                COMPLEX_UNIT_SP:等
     * @param size    对应单位的大小
     * @return 返回像素大小
     */
    public float getRawSize(Context context, int unit, float size) {
        Resources r;
        if (context == null) {
            r = Resources.getSystem();
        } else {
            r = context.getResources();
        }
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

}
