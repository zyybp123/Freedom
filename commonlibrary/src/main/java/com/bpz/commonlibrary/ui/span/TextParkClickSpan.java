package com.bpz.commonlibrary.ui.span;

import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bpz.commonlibrary.interf.listener.OnViewClickListener;

public class TextParkClickSpan extends ClickableSpan {

    @ColorInt
    private int showColor;
    private boolean hasUnderLine;
    private String clickText;
    private OnViewClickListener<String> listener;

    public TextParkClickSpan(int showColor, boolean hasUnderLine, String clickText,
                             OnViewClickListener<String> listener) {
        this.showColor = showColor;
        this.hasUnderLine = hasUnderLine;
        this.clickText = clickText;
        this.listener = listener;
    }

    @Override
    public void onClick(View widget) {
        //点击事件
        if (listener != null) {
            listener.onClick(widget, clickText);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //设置要点击的文本的颜色和是否显示下划线
        ds.setColor(showColor);
        ds.setUnderlineText(hasUnderLine);
    }
}
