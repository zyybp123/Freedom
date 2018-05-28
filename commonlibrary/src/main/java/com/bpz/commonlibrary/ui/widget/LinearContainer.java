package com.bpz.commonlibrary.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.adapter.base.BaseLinearAdapter;

import org.jetbrains.annotations.NotNull;


/**
 * Created by Administrator on 2018/2/23.
 * 线性布局容器
 * 很多时候动态添加少量内容时，没必要使用ListView或RecyclerView
 */

public class LinearContainer extends LinearLayout {
    public final String LOG_TAG = this.getClass().getSimpleName();
    /**
     * 添加标签时的参数
     */
    private ViewGroup.LayoutParams mParams;
    /**
     * 是否均分，默认均分，true为均分
     */
    private boolean mIsAverage = true;

    public LinearContainer(Context context) {
        this(context, null);
    }

    public LinearContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NotNull Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LinearContainer, 0, 0);
        mIsAverage = typeArray.getBoolean(R.styleable.LinearContainer_mIsAverage, mIsAverage);
        //获取此线性布局的方向,设置布局参数
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            if (mIsAverage) {
                //垂直布局,均分
                mParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            } else {
                //垂直布局，不均分
                mParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } else if (orientation == HORIZONTAL) {
            if (mIsAverage) {
                //水平布局,均分
                mParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 1);
            } else {
                //水平布局,不均分
                mParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
    }

    /**
     * 设置适配器
     *
     * @param adapter 数据适配器
     */
    public void setAdapter(final BaseLinearAdapter adapter) {
        if (adapter != null) {
            //adapter不为空
            adapter.setOnDataChanged(new BaseLinearAdapter.OnDataChanged() {
                @Override
                public void onChange() {
                    LogUtil.e(LOG_TAG, "----data has changed----");
                    addTabView(adapter);
                }
            });
            addTabView(adapter);
        } else {
            LogUtil.e(LOG_TAG, "--------BottomBarAdapter is null !--------");
        }
    }

    /**
     * 添加标签
     *
     * @param adapter 数据适配器
     */
    public void addTabView(BaseLinearAdapter adapter) {
        //获取标签数量
        int tabCount = adapter.getTabCount();
        //添加之前先移除所有子控件
        removeAllViews();
        for (int i = 0; i < tabCount; i++) {
            //获取标签View,并添加到容器
            View tabView = adapter.getTabView(this, i);
            tabView.setOnClickListener(new ItemClickListener(adapter, i));
            addView(tabView, mParams);
        }
    }

    /**
     * 获取标签View对象
     *
     * @param position 索引
     * @return 返回标签对象，若无则返回null
     */
    public View getTab(int position) {
        int childCount = getChildCount();
        //容错
        if (position < 0) {
            position = 0;
        }
        if (position >= childCount) {
            position = childCount - 1;
        }
        //在范围中
        return getChildAt(position);
    }

    /**
     * 条目点击事件监听器
     */
    public class ItemClickListener implements OnClickListener {
        int selectPosition;
        BaseLinearAdapter adapter;

        ItemClickListener(BaseLinearAdapter adapter, int selectPosition) {
            this.adapter = adapter;
            this.selectPosition = selectPosition;
        }

        @Override
        public void onClick(View v) {
            //条目点击事件
            adapter.onItemClick(v, LinearContainer.this, selectPosition);
        }
    }
}
