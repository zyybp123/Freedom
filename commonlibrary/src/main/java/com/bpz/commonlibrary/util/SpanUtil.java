package com.bpz.commonlibrary.util;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.URLSpan;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private static final String TAG = "SpanUtil";

    private SpanUtil() {
    }

    /**
     * 部分文字改变颜色
     *
     * @param text   文本
     * @param change 要变色的文本
     */
    public static SpannableString changeColor(String text, String change, int color) {
        SpannableString spanString = new SpannableString(text);
        List<Integer> indexList = StringUtil.getIndexList(text, change);
        if (indexList.size() == 0) {
            //文本内容或要更改样式的内容为空或文本不包含要变化的文本，直接返回
            return spanString;
        } else {
            for (int i = 0; i < indexList.size(); i++) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                int start = indexList.get(i);
                int end = start + change.length();
                spanString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanString;
    }


}
