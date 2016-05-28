package com.org.lengend.base;


import android.support.v7.widget.RecyclerView;

/**
 * Created by wangyanfei on 2016/5/12.
 */
public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    public OnRecyclerViewClickItemListener listener;

    public void setOnRecyclerViewClickItemListener(OnRecyclerViewClickItemListener listener) {
        this.listener = listener;
    }

}
