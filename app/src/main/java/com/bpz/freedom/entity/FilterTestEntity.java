package com.bpz.freedom.entity;

import com.bpz.commonlibrary.interf.FilterData;
import com.bpz.commonlibrary.util.StringUtil;

public class FilterTestEntity implements FilterData {
    private String title;
    private boolean selected;

    @Override
    public String getBarTitle() {
        return StringUtil.getNotNullStr(title);
    }

    @Override
    public void setBarTile(String title) {
        this.title = title;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
