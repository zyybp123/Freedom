package com.bpz.commonlibrary.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.bpz.commonlibrary.entity.FileDetailEntity;
import com.bpz.commonlibrary.mvp.BasePresenter;

import io.reactivex.disposables.Disposable;

/**
 * 文件选择模块
 * <p>
 * 1.最大选择数设置
 * 2.文件分类
 * 3.常用文件的库写入，读出
 * 4.应用内的文件管理
 * 5.能按文件后缀名，检索mimeType
 */
public class FileListFragment extends BaseRefreshFragment<FileDetailEntity> {

    @Override
    public RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected void toLoadMore() {

    }

    @Override
    protected void toRefresh() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onSubscribe(String methodTag, Disposable d) {

    }

    @Override
    public void onError(String methodTag, String describe) {

    }

    @Override
    public void onEmpty(String methodTag) {

    }

    @Override
    public void noNet() {

    }
}
