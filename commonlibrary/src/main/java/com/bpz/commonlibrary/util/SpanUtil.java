package com.bpz.commonlibrary.util;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.LocaleSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 处理TextView的显示
 * <p>
 * 字符层次
 * UnderlineSpan:下划线
 * StrikethroughSpan:删除线
 * SubscriptSpan:下标
 * SuperscriptSpan:上标
 * BackgroundColorSpan:背景
 * ForegroundColorSpan:前景(文字本身的颜色)
 * DynamicDrawableSpan:图片
 * ImageSpan:允许添加图片(继承了DynamicDrawableSpan)
 * StyleSpan:字体风格(Typeface.BOLD,Typeface.ITALIC,Typeface.NORMAL)
 * TypefaceSpan:字体(monospace, serif)
 * AbsoluteSizeSpan:设置绝对大小
 * RelativeSizeSpan:设置相对大小
 * ScaleXSpan:设置X方向的缩放
 * MaskFilterSpan:模糊或凸显字符
 * URLSpan:超链接(继承了ClickableSpan)
 * ClickableSpan:可点击
 * LocaleSpan:本地化
 * MetricAffectingSpan:度量相关
 * ReplacementSpan:替换
 * TextAppearanceSpan:直接指定风格
 * <p>
 * 段落级
 * LeadingMarginSpan：用来处理像word中项目符号一样的接口；
 * BulletSpan：子弹
 * DrawableMarginSpan：图片外边距
 * IconMarginSpan：图标外边距
 * QuoteSpan：引用(文本前加竖线)
 * Standard：LeadingMarginSpan2： *
 * AlignmentSpan：用来处理整个段落对齐的接口；Standard
 * LineBackgroundSpan：用来处理一行的背景的接口；
 * LineHeightSpan：用来处理一行高度的接口；
 * TabStopSpan：用来将字符串中的"\t"替换成相应的空行；
 * 自定义span
 * TextRoundSpan
 * <p>
 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
 * Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
 * Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
 * Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)
 */
public class SpanUtil {
    public static final int COLOR_FOREGROUND = 0;
    public static final int STYLE_UNDERLINE = 0;
    public static final int STYLE_STRIKETHROUGH = 1;
    public static final int STYLE_SUBSCRIPT = 2;
    public static final int STYLE_SUPERSCRIPT = 3;
    public static final int SIZE_RELATIVE = 0;

    private static final String TAG = "SpanUtil";

    private SpanUtil() {
    }

    public static SpannableString changeColor(@NotNull SpannableString text, String change) {
        String dest = text.subSequence(0, text.length()).toString();
        LocaleSpan localeSpan = new LocaleSpan(Locale.getDefault());
        //LineHeightSpan
        //ImageSpan imageSpan = new ImageSpan();
        return text;
    }

    @Contract(pure = true)
    public static SpannableString setImg(@NotNull SpannableString spanString,
                                         List<String> changes) {
        return spanString;
    }

    public static SpannableString someClick(@NotNull SpannableString spanString,
                                            List<String> changes, final View.OnClickListener listener) {
        String text = spanString.subSequence(0, spanString.length()).toString();
        if (StringUtil.isSpace(text) || changes == null || changes.size() == 0) {
            return spanString;
        }
        int changesSize = changes.size();
        //取文本集合做循环
        for (int i = 0; i < changesSize; i++) {
            String change = changes.get(i);
            List<Integer> indexList = StringUtil.getIndexList(text, change);
            if (indexList.size() > 0) {
                for (int j = 0; j < indexList.size(); j++) {
                    int start = indexList.get(j);
                    int end = start + change.length();
                    ClickableSpan span = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (listener != null) {
                                listener.onClick(widget);
                            }
                        }
                    };
                    spanString.setSpan(span, start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spanString;

    }

    /**
     * 为部分文本设置缩放
     *
     * @param spanString  格式文本串
     * @param changes     要设置格式的文本
     * @param proportions 倍数
     * @param model       0:相对大小，其它:X方向的缩放
     * @return 返回设置后的格式文本
     */
    public static SpannableString someScale(@NotNull SpannableString spanString,
                                            List<String> changes, List<Float> proportions,
                                            int model) {
        String text = spanString.subSequence(0, spanString.length()).toString();
        if (StringUtil.isSpace(text) || changes == null || changes.size() == 0) {
            return spanString;
        }
        int changesSize = changes.size();
        int colorsSize = proportions.size();
        //取文本集合做循环
        for (int i = 0; i < changesSize; i++) {
            Float proportion;
            String change = changes.get(i);
            if (colorsSize >= changesSize) {
                proportion = proportions.get(i);
            } else {
                proportion = proportions.get(i % colorsSize);
            }
            List<Integer> indexList = StringUtil.getIndexList(text, change);
            if (indexList.size() > 0) {
                for (int j = 0; j < indexList.size(); j++) {
                    int start = indexList.get(j);
                    int end = start + change.length();
                    Object span;
                    if (model == SIZE_RELATIVE) {
                        //相对大小
                        span = new RelativeSizeSpan(proportion);
                    } else {
                        //X方向的缩放
                        span = new ScaleXSpan(proportion);
                    }
                    spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spanString;
    }


    /**
     * 为部文字设置下划线,删除线，下标，上标，
     *
     * @param spanString 格式文本串
     * @param changes    要设置格式的文本
     * @param model      0:下划线，1:删除线，2:下标，3:上标
     * @return 返回设置后的格式文本
     */
    public static SpannableString someStyle(@NotNull SpannableString spanString,
                                            List<String> changes, int model) {
        String text = spanString.subSequence(0, spanString.length()).toString();
        if (StringUtil.isSpace(text) || changes == null || changes.size() == 0) {
            return spanString;
        }
        for (int i = 0; i < changes.size(); i++) {
            String change = changes.get(i);
            List<Integer> indexList = StringUtil.getIndexList(text, change);
            if (indexList.size() > 0) {
                for (int j = 0; j < indexList.size(); j++) {
                    Object span;
                    switch (model) {
                        case STYLE_UNDERLINE:
                            span = new UnderlineSpan();
                            break;
                        case STYLE_STRIKETHROUGH:
                            span = new StrikethroughSpan();
                            break;
                        case STYLE_SUBSCRIPT:
                            span = new SubscriptSpan();
                            break;
                        case STYLE_SUPERSCRIPT:
                            span = new SuperscriptSpan();
                            break;
                        default:
                            span = new UnderlineSpan();
                            break;
                    }
                    int start = indexList.get(j);
                    int end = start + change.length();
                    spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spanString;
    }


    /**
     * 部分字符设置颜色
     *
     * @param spanString 格式化文本
     * @param changes    要变色的文本集合
     * @param colors     颜色集合
     * @param model      模式，0为前景色，其它为背景色
     * @return 返回修改后的文本
     */
    public static SpannableString changeColors(@NotNull SpannableString spanString,
                                               List<String> changes,
                                               List<Integer> colors, int model) {
        String text = spanString.subSequence(0, spanString.length()).toString();
        if (StringUtil.isSpace(text) || changes == null || changes.size() == 0
                || colors == null || colors.size() == 0) {
            return spanString;
        }
        int changesSize = changes.size();
        int colorsSize = colors.size();
        //取文本集合做循环
        for (int i = 0; i < changesSize; i++) {
            Integer color;
            String change = changes.get(i);
            if (colorsSize >= changesSize) {
                //颜色集合大于等于文本集合，顺次取
                color = colors.get(i);
            } else {
                //颜色集小于文本集合，循环取
                color = colors.get(i % colorsSize);
            }
            changeColor(spanString, model, color, change);
        }
        return spanString;
    }

    /**
     * 为某些字符设置颜色
     *
     * @param spanString 格式文本串
     * @param model      模式
     * @param color      颜色
     * @param change     要变色的文本
     * @return 返回设置好的
     */
    public static SpannableString changeColor(@NotNull SpannableString spanString, int model, Integer color, String change) {
        String text = spanString.subSequence(0, spanString.length()).toString();
        List<Integer> indexList = StringUtil.getIndexList(text, change);
        if (indexList.size() > 0) {
            for (int j = 0; j < indexList.size(); j++) {
                int start = indexList.get(j);
                int end = start + change.length();
                Object colorSpan;
                if (model == COLOR_FOREGROUND) {
                    //前景
                    colorSpan = new ForegroundColorSpan(color);
                } else {
                    //背景
                    colorSpan = new BackgroundColorSpan(color);
                }
                spanString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanString;
    }
}
