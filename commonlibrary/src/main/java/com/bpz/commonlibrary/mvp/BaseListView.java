package com.bpz.commonlibrary.mvp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BaseListView<T> extends BaseView {
    void getListDataSuccess(@NotNull List<T> data);
    void refreshFail();
    void loadMoreFail();

}
