package com.bpz.freedom.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpz.freedom.R;
import com.bpz.freedom.entity.Test2Entity;

import java.util.List;


/**
 * Created by Administrator on 2017/5/17.
 * popWindow列表展示
 */

public class Adapter2PopList extends RecyclerView.Adapter<Adapter2PopList.MyViewHolder> {
    public static final int ITEM_HEADER = 0;
    public static final int ITEM_CONTENT = 1;
    String itemTag;

    private List dataList;

    public Adapter2PopList(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.check_info_item_header, parent, false);
            itemTag = "header";
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.check_info_item_content, parent, false);
            itemTag = "content";
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.initData(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof String) {
            return ITEM_HEADER;
        } else {
            return ITEM_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        if (dataList != null && dataList.size() > 0) {
            return dataList.size();
        } else {
            return 0;
        }
    }


    public interface OnItemClickListener {
        void onClick(View v, String tag, Object bean, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textName;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            if ("header".equals(itemTag)) {
                textView = (TextView) itemView.findViewById(R.id.tv_title);
            } else if (itemView instanceof LinearLayout) {
                linearLayout = (LinearLayout) itemView;
                textName = (TextView) itemView.findViewById(R.id.tv_name);
            }

        }

        @SuppressLint("DefaultLocale")
        public void initData(final int position) {
            if (textView != null) {
                String text = (String) dataList.get(position);
                text = text.toUpperCase();
                textView.setText(text);
            }
            if (linearLayout != null) {
                if (dataList != null && dataList.size() > 0) {
                    Test2Entity bean = (Test2Entity) dataList.get(position);
                    textName.setText(String.format("%s:%d", bean.getName(), bean.getId()));
                }
            }
        }
    }
}
