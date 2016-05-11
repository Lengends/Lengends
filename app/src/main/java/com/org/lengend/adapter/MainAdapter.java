package com.org.lengend.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.lengend.R;
import com.org.lengend.entity.MainDataEntity;

import java.util.ArrayList;

/**
 * Created by wangyanfei on 2016/5/11.
 */
public class MainAdapter extends Adapter<MainAdapter.MainViewHolder>{

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
    public void onBindViewHolder(MainViewHolder holder, int position) {
        final MainDataEntity entity = data.get(position);
        holder.title.setText(entity.getTitle());
        holder.desc.setText(entity.getDesc());
        System.out.println("=======onBindViewHolder==========="+position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("=======onClick===========");
                Intent intent = new Intent(activity, entity.getGoClass());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }



    class MainViewHolder extends ViewHolder{
        private View rootView;
        private TextView title;
        private TextView desc;

            public MainViewHolder(View itemView) {
                super(itemView);
                rootView = itemView;
                title = (TextView) itemView.findViewById(R.id.title);
                desc = (TextView) itemView.findViewById(R.id.desc);
            }
        }
}
