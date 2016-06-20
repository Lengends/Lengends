package com.org.lengend.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.lengend.recycleview.entity.ItemLayout1;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wangyanfei on 2016/6/17.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder>{
    private Context context;
    private List<ItemLayout1> urls;
    public HorizontalAdapter(Context context){
        this.context = context;
    }

    public void setUrls(List<ItemLayout1> urls) {
        this.urls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(context, R.layout.recycle_image_item,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(urls.get(position).getImageUrl()).into(holder.imageView);
        holder.textView.setText(urls.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return urls == null ? 0 : urls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.desc);
        }
    }



}
