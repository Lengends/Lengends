package com.org.lengend.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.org.lengend.base.widget.ScaleImageView;
import com.org.lengend.pagedview.PagedViewAdapter;
import com.org.lengend.recycleview.entity.ItemLayout1;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wangyanfei on 2016/6/17.
 */
public class PagedAdapter extends PagedViewAdapter<PagedAdapter.ViewHolder>{
    private Context context;
    private List<ItemLayout1> data;

    public PagedAdapter(Context context){
        this.context = context;
    }

    public void setData(List<ItemLayout1> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = View.inflate(context, R.layout.recycle_image_item,null);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ItemLayout1 layout1 = data.get(position);
        holder.imageView.setScale(16, 9);
        Picasso.with(context).load(layout1.getImageUrl()).into(holder.imageView);
        holder.desc.setText(layout1.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item-->"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ScaleImageView imageView;
        TextView desc;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ScaleImageView) itemView.findViewById(R.id.image);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }
    }
}
