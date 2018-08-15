package com.bpz.commonlibrary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bpz.commonlibrary.R;
import com.bpz.commonlibrary.interf.listener.OnImgShowListener;
import com.bpz.commonlibrary.interf.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter2Banner<T> extends RecyclerView.Adapter<Adapter2Banner.MyViewHolder> {
    private static final String TAG = "Adapter2Banner";
    private boolean canLoop = true;
    private List<T> mDataList;
    private OnItemClickListener<T> itemClickListener;
    private OnImgShowListener<T> imgShowListener;

    public Adapter2Banner(List<T> mDataList, OnItemClickListener<T> itemClickListener,
                          OnImgShowListener<T> imgShowListener) {
        this.mDataList = mDataList;
        if (this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }
        this.itemClickListener = itemClickListener;
        this.imgShowListener = imgShowListener;
    }

    public Adapter2Banner(boolean canLoop, List<T> mDataList,
                          OnItemClickListener<T> itemClickListener, OnImgShowListener<T> imgShowListener) {
        this(mDataList, itemClickListener, imgShowListener);
        this.canLoop = canLoop;
    }

    @NonNull
    @Override
    public Adapter2Banner.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fr_item_banner, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter2Banner.MyViewHolder holder, int position) {
        holder.init(position);
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() < 2) {
            canLoop = false;
        }
        if (canLoop) {
            return Integer.MAX_VALUE;
        } else {
            return mDataList.size();
        }
    }

    private T getData(int position) {
        return mDataList.get(getRealPosition(position));
    }

    private int getRealPosition(int position) {
        return canLoop ? position % mDataList.size() : position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fr_iv);
        }

        void init(final int position) {
            if (imgShowListener != null) {
                imgShowListener.onImageShow(imageView, getData(position));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击事件
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v,position, getData(position));
                    }
                }
            });
        }
    }


}
