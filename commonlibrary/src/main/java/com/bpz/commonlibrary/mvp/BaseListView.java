package com.bpz.commonlibrary.mvp;

import java.util.List;

public interface BaseListView<T> extends BaseView {
    void getListDataSuccess(List<T> data);

}
