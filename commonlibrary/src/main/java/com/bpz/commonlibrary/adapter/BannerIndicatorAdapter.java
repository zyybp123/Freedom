package com.bpz.commonlibrary.adapter;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bpz.commonlibrary.adapter.base.BaseLinearAdapter;
import com.bpz.commonlibrary.entity.StrokeEntity;
import com.bpz.commonlibrary.interf.SelectStatus;
import com.bpz.commonlibrary.ui.widget.LinearContainer;
import com.bpz.commonlibrary.util.BgResUtil;

import java.util.List;

/**
 * 图形指示器
 *
 * @param <T>
 */
public class BannerIndicatorAdapter<T extends SelectStatus> extends BaseLinearAdapter {
    private ColorStateList colorStateList;
    private StrokeEntity strokeEntity;
    private List<T> dataList;
    private float radius;
    private float piWidth;
    private float piHeight;

    public BannerIndicatorAdapter(List<T> dataList,
                                  float piWidth, float piHeight,
                                  @ColorInt int colorNormal, @ColorInt int colorSelected,
                                  float radius) {
        this.dataList = dataList;
        this.radius = radius;
        this.piWidth = piWidth;
        this.piHeight = piHeight;
        colorStateList = BgResUtil.getColorSelector(colorNormal, colorSelected);
    }

    @Override
    public int getTabCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public View getTabView(ViewGroup parent, int position) {
        View view = new View(parent.getContext());
        if (parent instanceof LinearContainer){
            ViewGroup.LayoutParams mParams = ((LinearContainer) parent).mParams;
            if (mParams != null){
                mParams.width = (int) piWidth;
                mParams.height = (int) piHeight;
                view.setLayoutParams(mParams);
            }
        }
        view.setBackground(BgResUtil.getRecBg(colorStateList, strokeEntity,
                radius, radius, radius, radius));
        view.setSelected(dataList.get(position).isSelected());
        return view;
    }

    @Override
    public void onItemClick(View itemView, ViewGroup parent, int position) {
        //处理单选
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelected(i == position);
        }
        notifyDataSetChanged();
    }
}
