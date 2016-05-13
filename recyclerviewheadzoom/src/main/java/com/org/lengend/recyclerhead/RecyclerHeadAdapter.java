package com.org.lengend.recyclerhead;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.lengend.base.BaseRecyclerViewAdapter;
import com.org.lengend.base.BaseViewHolder;
import com.org.lengend.base.DataEntity;
import com.org.lengend.base.DataUtils;

import java.util.ArrayList;

/**
 * Created by wangyanfei on 2016/5/12.
 */
public class RecyclerHeadAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder>{

    private static final int ITEM_HEAD = 0;
    private static final int ITEM_DEF = 1;
    private Activity activity;
    private ArrayList<DataEntity> data;
    private HeadViewListener headViewListener;

    public RecyclerHeadAdapter(Activity activity){
        this.activity = activity;
        data = DataUtils.getTestData(20);
    }

    public void setHeadViewListener(HeadViewListener headViewListener) {
        this.headViewListener = headViewListener;
    }

    @Override
    public int getItemViewType(int position) {
//        if(position == 0){
//            return ITEM_HEAD;
//        }else{
            return ITEM_DEF;
//        }
    }

    RecyclerView recyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
//        if(viewType == ITEM_HEAD){
//            RecyclerZoomHeadView headView = new RecyclerZoomHeadView(activity);
////            headView.setHeadViewListener(headViewListener);
////            headView.setRecyclerView(recyclerView);
//            holder = new HeadViewHolder(headView);
//        }else{
            View itemView = View.inflate(activity,R.layout.item_layout,null);
            holder = new MyViewHolder(itemView);
//        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(getItemViewType(position) == ITEM_HEAD){

            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClick(null,position);
                    }
                }
            });

        }else{
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            DataEntity entity = data.get(position);
            myViewHolder.title.setText(entity.getTitle());
            myViewHolder.desc.setText(entity.getDesc());
            myViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClick(null,position);
                    }
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MyViewHolder extends BaseViewHolder{
        private TextView title;
        private TextView desc;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }
    }
    class HeadViewHolder extends BaseViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }

    }
}
