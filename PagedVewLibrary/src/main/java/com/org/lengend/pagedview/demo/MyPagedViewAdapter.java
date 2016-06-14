package com.org.lengend.pagedview.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.org.lengend.pagedview.R;
import com.org.lengend.pagedview.PagedViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wangyanfei on 2016/6/14.
 */
public class MyPagedViewAdapter extends PagedViewAdapter<MyPagedViewAdapter.PagedViewHolder> {

    private Context context;
    List<String> imageUrls;
    public MyPagedViewAdapter(Context context){
        this.context = context;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public PagedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.image_view, null);
        PagedViewHolder holder = new PagedViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(PagedViewHolder holder, int position) {
        String url = imageUrls.get(position);
        Picasso.with(context).load(url).into(holder.imageView);
    }



    static class PagedViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public PagedViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
