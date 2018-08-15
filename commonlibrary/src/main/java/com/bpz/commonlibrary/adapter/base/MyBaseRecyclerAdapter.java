package com.bpz.commonlibrary.adapter.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bpz.commonlibrary.interf.ListData;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;
import com.bpz.commonlibrary.interf.listener.OnItemLongClickListener;
import com.bpz.commonlibrary.ui.recycler.MyDragListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class MyBaseRecyclerAdapter<T extends ListData, V extends MyBaseViewHolder> extends RecyclerView.Adapter<V> {
    public List<T> mDataList;
    public OnItemClickListener<T> itemClickListener;
    public OnItemLongClickListener<T> itemLongClickListener;
    public MyDragListener dragStartListener;

    public MyBaseRecyclerAdapter(List<T> mDataList,
                                 OnItemClickListener<T> itemClickListener) {
        this(mDataList);
        this.itemClickListener = itemClickListener;
    }

    public MyBaseRecyclerAdapter(List<T> mDataList) {
        this.mDataList = mDataList;
    }

    public MyBaseRecyclerAdapter(List<T> mDataList,
                                 OnItemLongClickListener<T> itemLongClickListener) {
        this(mDataList);
        this.itemLongClickListener = itemLongClickListener;
    }

    public MyBaseRecyclerAdapter(List<T> mDataList,
                                 OnItemClickListener<T> itemClickListener,
                                 OnItemLongClickListener<T> itemLongClickListener) {
        this(mDataList);
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    public MyBaseRecyclerAdapter(List<T> mDataList,
                                 OnItemClickListener<T> itemClickListener,
                                 OnItemLongClickListener<T> itemLongClickListener,
                                 MyDragListener dragStartListener) {
        this(mDataList);
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.dragStartListener = dragStartListener;
    }

    public View getInflateView(@NotNull ViewGroup parent, int banner) {
        return LayoutInflater.from(parent.getContext())
                .inflate(banner, parent, false);
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getMyViewHolder(parent, viewType);
    }

    /**
     * 由子类来完成viewHolder的生成
     *
     * @param parent   容器
     * @param viewType 条目类型
     * @return 返回holder
     */
    @NonNull
    public abstract V getMyViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull final V holder, int position) {
        setItemData(holder, position);
        final int layoutPosition = holder.getLayoutPosition();
        final T data = mDataList.get(position);
        if (data != null) {
            //条目数据不为空，设置点击和长按事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v,layoutPosition, data);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemLongClickListener != null) {
                        itemLongClickListener.onLongClick(layoutPosition, data);
                        return true;
                    }
                    return false;
                }
            });

            View handleView = holder.handleView;
            if (handleView != null) {
                handleView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (dragStartListener != null) {
                                dragStartListener.onStartDrag(holder);
                            } else {
                                return false;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 处理条目样式等
     *
     * @param holder   holder
     * @param position 条目索引
     */
    public abstract void setItemData(V holder, int position);
}
