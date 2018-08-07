package com.bpz.freedom.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpz.commonlibrary.adapter.base.MyBaseRecyclerAdapter;
import com.bpz.commonlibrary.adapter.base.MyBaseViewHolder;
import com.bpz.commonlibrary.ui.banner.PBanner;
import com.bpz.freedom.R;
import com.bpz.freedom.entity.BannerEntity;
import com.bpz.freedom.entity.TestEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter2Test2 extends MyBaseRecyclerAdapter<TestEntity, MyBaseViewHolder> {
    public static final int ITEM_BANNER = 0;
    public static final int ITEM_CARD = 1;
    public static final int ITEM_TITLE = 2;
    public static final int ITEM_LIST = 3;

    public Adapter2Test2(List<TestEntity> mDataList) {
        super(mDataList);
    }

    @NonNull
    @Override
    public MyBaseViewHolder getMyViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (ITEM_BANNER == viewType) {
            view = getInflateView(parent, R.layout.banner);
        } else if (ITEM_LIST == viewType) {
            view = getInflateView(parent, R.layout.item_test);
        } else {
            view = new TextView(parent.getContext());
        }
        return new MyBaseViewHolder(view);
    }

    @Override
    public void setItemData(MyBaseViewHolder holder, int position) {
        PBanner banner = holder.itemView.findViewById(R.id.banner);
        TextView tvName = holder.itemView.findViewById(R.id.tv_name);

        if (banner != null) {
            TestEntity testEntity = mDataList.get(position);
            banner.setModel(PBanner.Model.ONLY_INDICATOR);
            banner.setLocation(PBanner.Location.CENTER);
            banner.setDataList((List) testEntity.getData());
        }

        if (tvName != null) {
            tvName.setText("这是" + (position - 1) + "条数据！");
        }
    }

}
