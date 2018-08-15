package com.bpz.commonlibrary.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bpz.commonlibrary.adapter.base.BaseLinearAdapter;
import com.bpz.commonlibrary.interf.FilterData;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.ui.filter.FilterBarTab;
import com.bpz.commonlibrary.ui.widget.LinearContainer;

import java.util.List;

public class MyFilterBarAdapter<T extends FilterData> extends BaseLinearAdapter {
    private List<T> dataList;
    private int normalColor = Color.GRAY;
    private int selectColor = Color.BLUE;
    private OnItemClickListener<T> listener;

    public MyFilterBarAdapter(List<T> dataList, OnItemClickListener<T> listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public int getTabCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public View getTabView(ViewGroup parent, int position) {
        if (parent instanceof LinearContainer){
            ((LinearContainer) parent).mParams.gravity = Gravity.CENTER;
        }
        FilterBarTab barTab = new FilterBarTab(parent.getContext());
        barTab.setNormalColor(normalColor);
        barTab.setSelectColor(selectColor);
        barTab.setCount(getTabCount());
        barTab.setPosition(position);
        barTab.setSelected(dataList.get(position).isSelected());
        barTab.setTitle(dataList.get(position).getBarTitle());
        return barTab;
    }

    @Override
    public void onItemClick(View itemView, ViewGroup parent, int position) {
        //处理单选
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelected(i == position);
        }
        notifyDataSetChanged();
        if (listener != null) {
            listener.onItemClick(itemView,position, dataList.get(position));
        }
    }
}
