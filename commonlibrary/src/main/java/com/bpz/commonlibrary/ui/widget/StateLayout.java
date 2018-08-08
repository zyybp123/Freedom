package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 状态层布局，主要用来展现各种不同的状态
 */
public class StateLayout extends FrameLayout {
    private static final String TAG = "StateLayout";
    OnRetryListener listener;
    private int currentState;
    private SparseArray<View> mPages;
    private int[] states = new int[]{
            State.ON_FAIL, State.ON_EMPTY, State.ON_LOADING,
            State.ON_SUCCESS, State.ON_NO_NET
    };
    private LayoutParams params;
    private ProgressBar pbLoading;
    private ImageView ivState;
    private TextView tvTips;
    private View baseOtherView;
    /**
     * 是否使用默认层显示所有其他状态,默认使用
     */
    private boolean isOpenDefault = true;


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
        mPages = new SparseArray<>();
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        baseOtherView = View.inflate(context, R.layout.fr_base_state_layout, null);
        pbLoading = baseOtherView.findViewById(R.id.fr_pb_loading);
        ivState = baseOtherView.findViewById(R.id.fr_iv_state);
        tvTips = baseOtherView.findViewById(R.id.fr_tv_tips);
        if (isOpenDefault) {
            //其它状态公用一个界面时，添加进这个页面
            addView(baseOtherView);
        }
    }


    public void setListener(OnRetryListener listener) {
        this.listener = listener;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public boolean isOpenDefault() {
        return isOpenDefault;
    }

    public void setOpenDefault(boolean openDefault) {
        isOpenDefault = openDefault;
        if (!isOpenDefault && baseOtherView != null) {
            removeView(baseOtherView);
        }
    }

    public StateLayout setStatePage(int state, View view) {
        if (view == null || mPages == null) {
            LogUtil.e(TAG, "state view or pages is null !");
            return this;
        }
        if (isOpenDefault) {
            //是使用默认布局显示其它状态界面时，只添加成功状态的界面的布局
            if (state != State.ON_SUCCESS) {
                return this;
            }
        }
        View viewTemp = mPages.get(state);
        if (viewTemp != null) {
            //已经添加到容器中,要更新容器里的视图
            removeView(viewTemp);
        }
        mPages.put(state, view);
        addView(view);
        return this;
    }

    public StateLayout setTips(String text) {
        if (tvTips != null) {
            tvTips.setText(StringUtil.getNotNullStr(text));
        }
        return this;
    }

    public StateLayout setIcons(@DrawableRes int resId) {
        if (ivState != null) {
            ivState.setImageResource(resId);
        }
        return this;
    }

    public StateLayout isShowTips(boolean isShow) {
        if (tvTips != null) {
            tvTips.setVisibility(isShow ? VISIBLE : GONE);
        }
        return this;
    }

    public StateLayout isShowIcons(boolean isShow) {
        if (ivState != null) {
            ivState.setVisibility(isShow ? VISIBLE : GONE);
        }
        return this;
    }

    public void showCurrentPage(int currentState, boolean isOpenDefault) {
        setCurrentState(currentState);
        setOpenDefault(isOpenDefault);
        showCurrentPage();
    }

    public void showCurrentPage() {
        if (mPages == null) {
            //说明没有设置页面
            LogUtil.e(TAG, " mPages is null !");
            return;
        }
        if (isOpenDefault) {
            LogUtil.e(TAG, "currentState --->" + currentState);
            //其他状态，共用一个界面的情况
            View view = mPages.get(State.ON_SUCCESS);
            if (currentState == State.ON_SUCCESS) {
                if (view != null) {
                    view.setVisibility(VISIBLE);
                }
                if (baseOtherView != null) {
                    baseOtherView.setVisibility(GONE);
                }
            } else {
                if (view != null) {
                    view.setVisibility(GONE);
                }
                if (baseOtherView != null) {
                    baseOtherView.setVisibility(VISIBLE);
                }
                defaultLayoutSet();
            }
            return;
        }
        LogUtil.e(TAG, "currentState --->" + currentState);
        for (int state : states) {
            View view = mPages.get(state);
            if (view != null) {
                //能找到对应状态的view，只显示当前状态的view
                boolean isCurrent = currentState == state;
                if (isCurrent) {
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(GONE);
                }
            }
        }
    }

    private void defaultLayoutSet() {
        switch (currentState) {
            case State.ON_EMPTY:
                pbLoading.setVisibility(GONE);
                tvTips.setText(getResources().getString(R.string.frEmpty));
                ivState.setVisibility(VISIBLE);
                ivState.setImageResource(R.drawable.fr_empty);
                break;
            case State.ON_FAIL:
                pbLoading.setVisibility(GONE);
                tvTips.setText(getResources().getString(R.string.frFail));
                ivState.setVisibility(VISIBLE);
                ivState.setImageResource(R.drawable.fr_fail);
                break;
            case State.ON_LOADING:
                pbLoading.setVisibility(VISIBLE);
                ivState.setVisibility(GONE);
                tvTips.setText(getResources().getString(R.string.frOnLoading));
                break;
            case State.ON_NO_NET:
                pbLoading.setVisibility(GONE);
                tvTips.setText(getResources().getString(R.string.frNoNet));
                ivState.setVisibility(VISIBLE);
                ivState.setImageResource(R.drawable.fr_no_net);
                break;
        }
        if (currentState != State.ON_SUCCESS && currentState != State.ON_LOADING) {
            //不处于正在加载中和成功状态才设置点击重新加载的事件
            baseOtherView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRetry(currentState);
                    }
                }
            });
        }
    }

    public void showCurrentPage(int currentState) {
        setCurrentState(currentState);
        showCurrentPage();
    }

    public interface State {
        int ON_FAIL = -1;
        int ON_EMPTY = 0;
        int ON_LOADING = 1;
        int ON_SUCCESS = 2;
        int ON_NO_NET = 3;
    }

    public interface OnRetryListener {
        void onRetry(int state);
    }
}


