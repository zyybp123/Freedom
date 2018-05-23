package com.bpz.commonlibrary.ui.bottombar;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpz.commonlibrary.util.LogUtil;
import com.bpz.commonlibrary.R;


/**
 * Created by Administrator on 2017/10/20.
 * 底部导航栏标签
 */

public class BottomBarTab extends FrameLayout {
    /**
     * 图片从网络加载
     */
    public static final String IMG_LOAD_FROM_NET = "IMG_LOAD_FROM_NET";
    /**
     * 图片从本地加载
     */
    public static final String IMG_LOAD_LOCAL = "IMG_LOAD_LOCAL";
    /**
     * 文字从网络加载
     */
    public static final String TEXT_LOAD_FROM_NET = "TEXT_LOAD_FROM_NET";
    /**
     * 文字从本地加载
     */
    public static final String TEXT_LOAD_LOCAL = "TEXT_LOAD_LOCAL";
    /**
     * 根布局
     */
    public View mRootView;
    /**
     * 图片
     */
    private ImageView mBottomIcon;
    /**
     * 标题
     */
    private TextView mBottomTitle;
    /**
     * 消息红点
     */
    private TextView mBottomDot;
    /**
     * 消息数字
     */
    private TextView mBottomBadge;
    private FrameLayout mBottomIconFr;
    boolean mIsSelected;
    BottomBarBean mBottomBarBean;

    String logTag;

    public BottomBarTab(@NonNull Context context) {
        this(context, null);
    }

    public BottomBarTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarTab(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        logTag = LogUtil.getLogTag(this);
        //加载基本布局,找到控件
        mRootView = LayoutInflater
                .from(getContext())
                .inflate(R.layout.fr_bottom_bar_tab_layout, this, false);
        mBottomIcon = mRootView.findViewById(R.id.fr_bottom_tab_icon);
        mBottomTitle = mRootView.findViewById(R.id.fr_bottom_tab_title);
        mBottomDot = mRootView.findViewById(R.id.fr_bottom_tab_dot);
        mBottomBadge = mRootView.findViewById(R.id.fr_bottom_tab_badge);
        mBottomIconFr = mRootView.findViewById(R.id.fr_bottom_tab_icon_fr);
        mIsSelected = isSelected();
        addView(mRootView);
    }

    public void setMBottomBarBean(BottomBarBean mBottomBarBean) {
        this.mBottomBarBean = mBottomBarBean;
        //初始化
        setStyle(mBottomBarBean);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        //选中状态变更
        mBottomBarBean.setSelected(selected);
        setStyle(mBottomBarBean);
    }

    public void setStyle(BottomBarBean barBean) {
        if (barBean != null) {
            String loadTagImg = barBean.getLoadTagImg();
            mIsSelected = barBean.isSelected();
            if (IMG_LOAD_FROM_NET.equals(loadTagImg)) {
                //加载网络图片
                String iconUrlNormal = barBean.getIconUrlNormal();
                String iconUrlSelect = barBean.getIconUrlSelect();
                setSelect(mIsSelected, iconUrlSelect, iconUrlNormal);
            } else if (IMG_LOAD_LOCAL.equals(loadTagImg)) {
                //加载本地图片
                int iconResNormal = barBean.getIconResNormal();
                int iconResSelect = barBean.getIconResSelect();
                setSelect(mIsSelected, iconResSelect, iconResNormal);
            } else {
                //默认加载本地图片
                int iconResNormal = barBean.getIconResNormal();
                int iconResSelect = barBean.getIconResSelect();
                setSelect(mIsSelected, iconResSelect, iconResNormal);
            }

            String loadTagText = barBean.getLoadTagText();
            String title = barBean.getTitle();
            //默认的文字选中的颜色
            int textColorNormal;
            //默认的文字未选中的颜色
            int textColorSelect;
            if (TEXT_LOAD_FROM_NET.equals(loadTagText)) {
                //加载网络文字
                textColorNormal = barBean.getTextColorNormalNet();
                textColorSelect = barBean.getTextColorSelectNet();
            } else if (TEXT_LOAD_LOCAL.equals(loadTagText)) {
                //加载本地文字
                textColorNormal = barBean.getTextColorNormal();
                textColorSelect = barBean.getTextColorSelect();
            } else {
                //默认加载本地文字
                textColorNormal = barBean.getTextColorNormal();
                textColorSelect = barBean.getTextColorSelect();
            }
            setSelect(mIsSelected, title, textColorSelect, textColorNormal);

        } else {
            LogUtil.e(logTag, "BottomBarBean is null");
        }
    }

    /**
     * 设置网络图片选中状态
     *
     * @param isSelected  选中状态
     * @param selectedUrl 选中的图片
     * @param normalUrl   未选中的图片
     */
    public void setSelect(boolean isSelected, String selectedUrl, String normalUrl) {
        if (isSelected) {
            //选中状态
            //ImageLoad.getInstance().glideSimple(mContext, mBottomIcon, selectedUrl);
        } else {
            //未选中状态
            //ImageLoad.getInstance().glideSimple(mContext, mBottomIcon, normalUrl);
        }
    }


    /**
     * 设置本地图片选中状态
     *
     * @param isSelected       选中状态
     * @param selectedDrawable 选中的图片
     * @param normalDrawable   未选中的图片
     */
    public void setSelect(boolean isSelected, int selectedDrawable, int normalDrawable) {
        if (isSelected) {
            //选中状态
            mBottomIcon.setImageResource(selectedDrawable);
        } else {
            //未选中状态
            mBottomIcon.setImageResource(normalDrawable);
        }
    }

    /**
     * 设置文字选中状态
     *
     * @param isSelected    选中状态
     * @param selectedColor 选中的图片
     * @param normalColor   未选中的图片
     */
    public void setSelect(boolean isSelected, String title, int selectedColor, int normalColor) {
        mBottomTitle.setText(title);
        if (isSelected) {
            //选中状态
            mBottomTitle.setTextColor(selectedColor);
        } else {
            //未选中状态
            mBottomTitle.setTextColor(normalColor);
        }
    }

    //隐藏文字
    public void setTitleHide() {
        mBottomTitle.setVisibility(GONE);
    }

    //隐藏红点
    public void setDotHide() {
        mBottomDot.setVisibility(GONE);
    }

    //显示红点
    public void setDotShow() {
        mBottomDot.setVisibility(VISIBLE);
    }

    //隐藏小图标
    public void setBadgeHide() {
        mBottomBadge.setVisibility(GONE);
    }

    //设置小图标显示的数,小于等于零不显示,大于99显示···
    public void setBadgeText(int number) {
        if (number > 0) {
            mBottomBadge.setVisibility(View.VISIBLE);
            if (number > 99) {
                mBottomBadge.setText("···");
            } else {
                String text = "" + number;
                mBottomBadge.setText(text);
            }
        } else {
            mBottomBadge.setVisibility(View.GONE);
        }
    }

    //分别对外提供获取各控件的方法
    public ImageView getBottomIcon() {
        return mBottomIcon;
    }

    public TextView getBottomTitle() {
        return mBottomTitle;
    }

    public TextView getBottomDot() {
        return mBottomDot;
    }

    public TextView getBottomBadge() {
        return mBottomBadge;
    }

    public FrameLayout getBottomIconFr() {
        return mBottomIconFr;
    }

}