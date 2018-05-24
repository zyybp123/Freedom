package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 状态层布局，主要用来展现各种不同的状态
 */
public class StateLayout extends FrameLayout {
    private int currentState;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public interface State {
        int ON_FAIL = -1;
        int ON_EMPTY = 0;
        int ON_LOADING = 1;
        int ON_SUCCESS = 2;
        int ON_NO_NET = 3;
    }

//public void
}
