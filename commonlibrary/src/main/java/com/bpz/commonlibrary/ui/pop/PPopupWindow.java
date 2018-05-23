package com.bpz.commonlibrary.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.util.DimensionUtil;
import com.bpz.commonlibrary.util.LogUtil;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Administrator on 2018/2/24.
 * 一个利用popupWindow的显示控件（为全屏的pop）
 */

public class PPopupWindow {
    private static final String TAG = "PPopupWindow";
    private PopupWindow pw;
    /**
     * 根布局
     */
    private View mRootView;
    /**
     * 页面容器，由外部实现
     */
    private FrameLayout mPageContainer;
    /**
     * 布局参数，将子页面添加进容器时用到
     */
    FrameLayout.LayoutParams mLayoutParams;
    /**
     * pop内的页面map，按筛选页面的标识存储
     */
    private Map<String, BasePage> pageMap;
    private BasePage currentPage;
    /**
     * 非透明区域距离容器的左上右下的边距
     */
    private int mLeftMargin = 0;
    private int mTopMargin = 0;
    private int mRightMargin = 0;
    private int mBottomMargin = 0;

    public PPopupWindow(Context context) {
        //加载根布局
        mRootView = View.inflate(context, R.layout.fr_p_popup_base_layout, null);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击根布局，隐藏pop
                hidePop();
            }
        });
        mPageContainer = mRootView.findViewById(R.id.fr_fl_page);
        createLayoutParam();
        createPop(context);
        //初始一个新的map
        pageMap = new HashMap<>();
    }

    private void createPop(Context context) {
        //创建pop
        pw = new PopupWindow(mRootView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //设置高度,背景，动画，点击空白是否消失
        pw.setHeight(DimensionUtil.totalSize(context).y - DimensionUtil.getStatusBarHeight(context));
        pw.setBackgroundDrawable(new ColorDrawable(Color.argb(127, 0, 0, 0)));
        //pw.setAnimationStyle(R.style.FilterPopupAnimation);
        //pw.setOutsideTouchable(true);
    }

    public PopupWindow getPopupWindow() {
        return pw;
    }

    /**
     * 设置布局边距 左，上，右，下
     */
    public void setMargins(int mLeftMargin, int mTopMargin, int mRightMargin, int mBottomMargin) {
        this.mLeftMargin = mLeftMargin;
        this.mTopMargin = mTopMargin;
        this.mRightMargin = mRightMargin;
        this.mBottomMargin = mBottomMargin;
    }

    private void createLayoutParam() {
        //创建一个布局参数
        mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }


    /**
     * 初始化popWindow
     *
     * @param v 相对于哪个控件显示
     */
    public void showPop(View v, BasePage page, boolean isFull) {
        if (mRootView == null || page == null) {
            LogUtil.e(TAG, "root view or page is null !" + mRootView + "..." + page);
            return;
        }
        View pageMRootView = page.getMRootView();
        if (pageMRootView == null) {
            LogUtil.e(TAG, "pageMRootView is null !");
            return;
        }
        if (pw == null) {
            LogUtil.e(TAG, "popupWindow is null !");
            //如果pop对象为null则重新创建
            createPop(mRootView.getContext());
        }
        if (mLayoutParams == null) {
            LogUtil.e(TAG, "mLayoutParams is null !");
            //布局参数为空，重新创建
            createLayoutParam();
        }
        if (pageMap == null) {
            LogUtil.e(TAG, "pageMap is null !");
            //如果map为null则新建一个map
            pageMap = new HashMap<>();
        }
        if (!pageMap.containsValue(page)) {
            //如果map中没有此页面，则将页面添加到map，再加入容器
            pageMap.put(page.getPageTag(), page);
        }
        //如果已经包含了此页面，则直接添加进容器
        mPageContainer.removeAllViews();
        setMargin();
        mPageContainer.addView(pageMRootView, mLayoutParams);
        //保存当前page实例
        currentPage = page;
        if (!pw.isShowing()) {
            //如果pop不在显示中，就显示
            if (isFull) {
                //定位显示（起始坐标为左上角）
                pw.showAtLocation(v, Gravity.TOP, 0, 0);
            } else {
                //相对于某控件显示（偏移量均为0）
                showAsDropDown(pw, v, 0, 0);
            }
            //设置是否显示
            isShow = false;
        }
    }

    /**
     * 设置边距，控制位置
     */
    private void setMargin() {
        if (mLayoutParams == null) {
            createLayoutParam();
        }
        mLayoutParams.leftMargin = mLeftMargin;
        mLayoutParams.topMargin = mTopMargin;
        mLayoutParams.rightMargin = mRightMargin;
        mLayoutParams.bottomMargin = mBottomMargin;
    }

    /**
     * 解决6.0以后pop的显示问题
     *
     * @param pw     popupWindow
     * @param anchor v
     * @param xOff   x轴偏移
     * @param yOff   y轴偏移
     */
    private void showAsDropDown(PopupWindow pw, View anchor, int xOff, int yOff) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pw.setHeight(height);
            pw.showAsDropDown(anchor, xOff, yOff);
        } else {
            pw.showAsDropDown(anchor, xOff, yOff);
        }
        isShow = true;
    }

    /**
     * 记录pop是否显示
     */
    private boolean isShow = true;

    /**
     * 隐藏pop的方法
     */
    public void hidePop() {
        if (pw != null) {
            pw.dismiss();
            isShow = false;
        }
    }

    /**
     * 获取当前pop里的页面对象
     *
     * @return 返回当前pop里的页面对象
     */
    public BasePage getCurrentPage() {
        return currentPage;
    }

}
