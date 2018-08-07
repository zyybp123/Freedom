package com.bpz.freedom.entity;

import com.bpz.commonlibrary.interf.ListData;

public class TestEntity implements ListData {
    private int itemType;
    private Object data;

    public TestEntity(int itemType, Object data) {
        this.itemType = itemType;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
