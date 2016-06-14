package com.org.lengend.pagedview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by wangyanfei on 2016/6/14.
 */
public abstract class PagedViewAdapter<T extends RecyclerView.ViewHolder>{
    private PagedView pagedView;
    protected final void setPagedView(PagedView pagedView){
        this.pagedView = pagedView;
    }

    public void notifyDataSetChanged(){
        if(pagedView != null){
            pagedView.notifyDataSetChanged();
        }
    }

    public abstract int getCount();
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(T holder, int position);
}
