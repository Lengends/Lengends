package com.org.lengend.recycleview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.lengend.base.widget.ScaleImageView;
import com.org.lengend.pagedview.PageIndicatorView;
import com.org.lengend.pagedview.PagedView;
import com.org.lengend.recycleview.entity.ItemData;
import com.org.lengend.recycleview.entity.ItemLayout1;
import com.org.lengend.recycleview.entity.ItemTitleData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wangyanfei on 2016/6/17.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemData> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public RecycleViewAdapter(Context context){
        this.context = context;
    }

    public void setData(List<ItemData> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        View itemView;
        switch (viewType){
            case ItemData.ITEM_TYPE_1:
                itemView = View.inflate(context, R.layout.cycle_pagedview, null);
                holder = new BannelViewHolder(itemView,context);

                break;

            case ItemData.ITEM_TYPE_2:
                itemView = View.inflate(context, R.layout.recycle_image_item, null);
                holder = new ImageViewHolder(itemView);
                break;

            case ItemData.ITEM_TYPE_3:
                itemView = View.inflate(context, R.layout.recycle_image_item, null);
                holder = new ImageViewHolder(itemView);
                break;

            case ItemData.ITEM_TYPE_4:
                itemView = View.inflate(context, R.layout.recycle_image_item, null);
                holder = new ImageViewHolder(itemView);
                break;

            case ItemData.ITEM_TYPE_5:
                itemView = View.inflate(context, R.layout.def_text_item, null);
                holder = new TitleViewHolder(itemView);
                break;
            case ItemData.ITEM_TYPE_6:
                itemView = View.inflate(context, R.layout.horizonta_recycle_view, null);
                holder = new HorizontalViewHolder(itemView,context);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemData itemData = data.get(position);
        switch (getItemViewType(position)){
            case ItemData.ITEM_TYPE_1:
                BannelViewHolder horizontalViewHolder = (BannelViewHolder) holder;
                if(horizontalViewHolder.adapter.getCount() == 0){
                    List<ItemLayout1> layout1Data = itemData.getData(List.class);
                    horizontalViewHolder.adapter.setData(layout1Data);
                    horizontalViewHolder.adapter.notifyDataSetChanged();
                }


                break;
            case ItemData.ITEM_TYPE_2:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                ItemLayout1 layout2 = itemData.getData(ItemLayout1.class);
                Picasso.with(context).load(layout2.getImageUrl()).into(imageViewHolder.imageView);
                imageViewHolder.imageView.setScale(2,1);
                imageViewHolder.title.setText(layout2.getTitle());

                break;


            case ItemData.ITEM_TYPE_3:
                ImageViewHolder imageViewHolder3 = (ImageViewHolder) holder;
                ItemLayout1 layout3 = itemData.getData(ItemLayout1.class);
                Picasso.with(context).load(layout3.getImageUrl()).into(imageViewHolder3.imageView);
                imageViewHolder3.imageView.setScale(3,4);
                imageViewHolder3.title.setText(layout3.getTitle());

                break;

            case ItemData.ITEM_TYPE_4:
                ImageViewHolder imageViewHolder4 = (ImageViewHolder) holder;
                ItemLayout1 layout4 = itemData.getData(ItemLayout1.class);
                Picasso.with(context).load(layout4.getImageUrl()).into(imageViewHolder4.imageView);
                imageViewHolder4.imageView.setScale(3,4);
                imageViewHolder4.title.setText(layout4.getTitle());

                break;

            case ItemData.ITEM_TYPE_5:
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                ItemTitleData titleData = itemData.getData(ItemTitleData.class);
                titleViewHolder.title.setText(titleData.getTitle());
                break;

            case ItemData.ITEM_TYPE_6:
                HorizontalViewHolder horizontalViewHolder1 = (HorizontalViewHolder) holder;

                if(horizontalViewHolder1.adapter.getItemCount() == 0){
                    List<ItemLayout1> horData = itemData.getData(List.class);
                    horizontalViewHolder1.adapter.setUrls(horData);
                }
                horizontalViewHolder1.adapter.notifyDataSetChanged();
                break;

        }

    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    /**
     * 水平滑动的Item
     */
    private static class BannelViewHolder extends RecyclerView.ViewHolder{

        private PagedView pagedView;
        private PagedAdapter adapter;
        public BannelViewHolder(View itemView,Context context) {
            super(itemView);
            //得到控件
            pagedView = (PagedView) itemView.findViewById(R.id.cyclePagedView);
            PageIndicatorView pageIndicatorView = (PageIndicatorView) itemView.findViewById(R.id.pageIndicatorView);
            pagedView.setPageIndicator(pageIndicatorView);
            adapter = new PagedAdapter(context);
            pagedView.setAdapter(adapter);
            pagedView.setAutoPage(true);
        }
    }

    /**
     * 水平滑动的Item
     */
    private static class HorizontalViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView recyclerView;
        private HorizontalAdapter adapter;
        public HorizontalViewHolder(View itemView,Context context) {
            super(itemView);
            //得到控件
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleView);
            adapter = new HorizontalAdapter(context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }


    /**
     * TitleItem
     */
    private static class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        public TitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text);
        }
    }


    /**
     * TitleItem
     */
    private static class ImageViewHolder extends RecyclerView.ViewHolder{

        private ScaleImageView imageView;
        private TextView title;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ScaleImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.desc);
        }
    }



    /**
     * 正常的Item
     */
    private static class NormalViewHolder extends RecyclerView.ViewHolder{


        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }






}
