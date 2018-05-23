package com.bpz.commonlibrary.ui.bottombar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.adapter.BaseLinearAdapter;


/**
 * Created by Administrator on 2018/1/15.
 * 底部导航栏
 */

public class BottomBar extends LinearLayout {

    public final String LOG_TAG = this.getClass().getSimpleName();
    ViewGroup.LayoutParams params;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
    }

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

    private void addTabView(BaseLinearAdapter adapter) {
        //控制标签数量在0-5之间
        int tabCount = adapter.getTabCount();
        if (tabCount < 0) {
            LogUtil.e(LOG_TAG, "--------The tab count can only be 0 to 5 !-------");
            tabCount = 0;
        }
        if (tabCount >= 5) {
            LogUtil.e(LOG_TAG, "--------The tab count can only be 0 to 5 !-------");
            tabCount = 5;
        }
        //添加之前先移除所有子控件
        removeAllViews();
        for (int i = 0; i < tabCount; i++) {
            //获取标签View
            View tabView = adapter.getTabView(this, i);
            tabView.setOnClickListener(new ItemClickListener(adapter, i));
            addView(tabView, params);
        }
    }

    /**
     * 获取底部导航栏的标签对象
     *
     * @param position 索引
     * @return 返回标签对象，若无则返回null
     */
    public View getBarTab(int position) {
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


    public class ItemClickListener implements OnClickListener {

        int selectPosition;
        BaseLinearAdapter adapter;

        public ItemClickListener(BaseLinearAdapter adapter, int selectPosition) {
            this.adapter = adapter;
            this.selectPosition = selectPosition;
        }

        @Override
        public void onClick(View v) {
            //处理单选
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = BottomBar.this.getChildAt(i);
                if (childAt != null) {
                    childAt.setSelected(selectPosition == i);
                }
            }
            //条目点击事件
            adapter.onItemClick(v, BottomBar.this, selectPosition);
        }
    }

}
