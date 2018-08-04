package com.bpz.commonlibrary.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;

import com.bpz.commonlibrary.entity.StrokeEntity;


/**
 * Created by Administrator on 2018/5/7.
 * 处理shape，状态选择器的工具类
 */

public class BgResUtil {

    private static final String TAG = "BgResUtil";

    private BgResUtil() {

    }

    /**
     * 获取矩形图片
     *
     * @param color        颜色
     * @param strokeEntity 边框信息
     */
    public static GradientDrawable getRecBg(ColorStateList color, StrokeEntity strokeEntity) {
        return getDrawable(GradientDrawable.RECTANGLE, color, null, strokeEntity);
    }

    /**
     * 代码生成shape图片
     *
     * @param shape        形状 GradientDrawable.RECTANGLE（矩形）, OVAL（圆）, LINE（线）, RING（环形）
     * @param colors       颜色状态选择器
     * @param size         宽,高 传入null不设置
     * @param strokeEntity 包装了边框信息的数据实体
     * @return 返回图片对象
     */
    public static GradientDrawable getDrawable(int shape, ColorStateList colors,
                                               Point size,
                                               StrokeEntity strokeEntity) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape);
        if (size != null) {
            drawable.setSize(size.x, size.y);
        }
        if (hasNativeStateListAPI()) {
            drawable.setColor(colors);
        } else {
            if (colors == null) {
                drawable.setColor(Color.TRANSPARENT);
            } else {
                drawable.setColor(colors.getColorForState(drawable.getState(), Color.TRANSPARENT));
            }
        }
        StrokeEntity.setStroke(drawable, strokeEntity);
        return drawable;
    }

    private static boolean hasNativeStateListAPI() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 获取矩形图片
     *
     * @param color        颜色
     * @param strokeEntity 边框信息
     * @param radius       圆角数组（左上，右上，右下，左下）
     */
    public static GradientDrawable getRecBg(ColorStateList color, StrokeEntity strokeEntity, float... radius) {
        GradientDrawable drawable = getDrawable(GradientDrawable.RECTANGLE, color, null, strokeEntity);
        setCorners(drawable, radius);
        return drawable;
    }

    /**
     * 设置圆角的方法
     *
     * @param drawable     图片
     * @param cornerRadius 圆角数组（左上，右上，右下，左下）
     */
    public static void setCorners(GradientDrawable drawable, float[] cornerRadius) {
        if (drawable == null || cornerRadius == null) {
            LogUtil.e(TAG, "drawable or cornerRadius is null !");
            return;
        }
        int length = cornerRadius.length;
        if (length > 0) {
            float[] corners = new float[8];
            for (int i = 0; i < 8; i++) {
                if (i / 2 < cornerRadius.length) {
                    corners[i] = cornerRadius[i / 2];
                } else {
                    corners[i] = 0;
                }
            }
            //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
            drawable.setCornerRadii(corners);
        }
    }


    /**
     * 使用代码的方式生成状态选择器，对应XML中的Selector
     *
     * @return 返回状态选择器图片
     */
    public static StateListDrawable generateSelector(Drawable pressed, Drawable normal) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);//设置按下的图片
        drawable.addState(new int[]{android.R.attr.state_selected}, pressed);//设置按下的图片
        drawable.addState(new int[]{android.R.attr.state_checked}, pressed);//设置按下的图片
        drawable.addState(new int[]{}, normal);//设置默认的图片
        //添加渐变
        //drawable.setEnterFadeDuration(500);
        //drawable.setExitFadeDuration(500);
        return drawable;
    }

    /**
     * 颜色状态选择器
     *
     * @param pressed 按下的色值
     * @param normal  正常的色值
     * @return 返回对应的颜色状态选择器
     */
    @NonNull
    public static ColorStateList getColorSelector(int pressed, int normal) {
        int[][] states = new int[][]{
                {android.R.attr.state_checked},
                {android.R.attr.state_pressed},
                {android.R.attr.state_selected},
                {}
        };
        int[] colors = new int[]{pressed, pressed, pressed, normal};
        return new ColorStateList(states, colors);
    }

    /**
     * 动态创建带上分隔线或下分隔线的Drawable。
     *
     * @param separatorColor 分割线颜色。
     * @param bgColor        Drawable 的背景色。
     * @param top            true 则分割线为上分割线，false 则为下分割线。
     * @return 返回所创建的 Drawable。
     */
    public static LayerDrawable createItemSeparatorBg(@ColorInt int separatorColor, @ColorInt int bgColor, int separatorHeight, boolean top) {

        ShapeDrawable separator = new ShapeDrawable();
        separator.getPaint().setStyle(Paint.Style.FILL);
        separator.getPaint().setColor(separatorColor);

        ShapeDrawable bg = new ShapeDrawable();
        bg.getPaint().setStyle(Paint.Style.FILL);
        bg.getPaint().setColor(bgColor);

        Drawable[] layers = {separator, bg};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        layerDrawable.setLayerInset(1, 0, top ? separatorHeight : 0, 0, top ? 0 : separatorHeight);
        return layerDrawable;
    }

    public static Bitmap vectorDrawableToBitmap(Context context, @DrawableRes int resVector) {
        Drawable drawable = getVectorDrawable(context, resVector);
        if (drawable != null) {
            Bitmap b = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
            drawable.draw(c);
            return b;
        }
        return null;
    }

    /////////////// VectorDrawable /////////////////////
    @Nullable
    public static Drawable getVectorDrawable(Context context, @DrawableRes int resVector) {
        try {
            return AppCompatResources.getDrawable(context, resVector);
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in getVectorDrawable. resVector=" + resVector + ", resName=" + context.getResources().getResourceName(resVector) + e.getMessage());
            return null;
        }
    }

}
