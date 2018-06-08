package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FreeIndicator extends LinearLayout {
    public FreeIndicator(Context context) {
        this(context, null);
    }

    public FreeIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreeIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

}
