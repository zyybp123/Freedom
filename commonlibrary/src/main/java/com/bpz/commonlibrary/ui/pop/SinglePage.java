package com.bpz.commonlibrary.ui.pop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.net.download.State;
import com.bpz.commonlibrary.ui.widget.StateLayout;

import java.util.List;

public class SinglePage<T> extends BasePage<T> {
    public SinglePage(Context context, List<T> data, OnItemClickListener<T> listener, boolean isWrapContent) {
        super(context, data, listener, isWrapContent);
    }

    @Override
    public View loadRootView() {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setAdapter(getAdapter());
        return recyclerView;
    }

    private RecyclerView.Adapter getAdapter() {
        return null;
    }
}
