package com.org.lengend.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wangyanfei on 2016/5/11.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder{
    public View rootView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        rootView = itemView;
    }
}
