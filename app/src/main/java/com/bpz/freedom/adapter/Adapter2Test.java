package com.bpz.freedom.adapter;

import android.support.annotation.Nullable;

import com.bpz.freedom.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class Adapter2Test extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public Adapter2Test(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_name, "这是第 " + item + " 条");
    }
}
