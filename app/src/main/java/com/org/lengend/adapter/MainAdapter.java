package com.org.lengend.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.lengend.R;
import com.org.lengend.base.BaseRecyclerViewAdapter;
import com.org.lengend.base.BaseViewHolder;
import com.org.lengend.entity.MainDataEntity;

import java.util.ArrayList;

/**
 * Created by wangyanfei on 2016/5/11.
 */
public class MainAdapter extends BaseRecyclerViewAdapter<MainAdapter.MainViewHolder> {

    private Activity activity;
    private ArrayList<MainDataEntity> data;
    public MainAdapter(Activity activity){
        this.activity = activity;
    }

    public void setData(ArrayList<MainDataEntity> data) {
        this.data = data;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(activity, R.layout.main_item_layout, null);
        MainViewHolder holder = new MainViewHolder(itemView);
        return holder;
    }



    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        final MainDataEntity entity = data.get(position);
        holder.title.setText(entity.getTitle());
        holder.desc.setText(entity.getDesc());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(entity, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MainViewHolder extends BaseViewHolder {
        private TextView title;
        private TextView desc;

            public MainViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                desc = (TextView) itemView.findViewById(R.id.desc);
            }
        }
}
