package com.org.lengend.recycleview;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.org.lengend.base.BaseActivity;
import com.org.lengend.base.DataUtils;
import com.org.lengend.recycleview.entity.ItemData;
import com.org.lengend.recycleview.entity.ItemLayout1;
import com.org.lengend.recycleview.entity.ItemTitleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyanfei on 2016/6/17.
 */
public class RecycleViewActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle_view_activity);
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapter(this);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int spanSize = layoutManager.getSpanCount();
                switch (adapter.getItemViewType(position)){

                    case ItemData.ITEM_TYPE_1:
                        spanSize = layoutManager.getSpanCount();
                        break;

                    case ItemData.ITEM_TYPE_2:
                    case ItemData.ITEM_TYPE_3:
                        spanSize = 3;
                        break;

                    case ItemData.ITEM_TYPE_4:
                        spanSize = 2;
                        break;

                    case ItemData.ITEM_TYPE_5:
                        spanSize = layoutManager.getSpanCount();
                        break;
                }
                return spanSize;
            }
        });

        adapter.setData(getTestData());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }




    private List<ItemData> getTestData(){

        String linkUrl = "http://www.baidu.com";
        List<ItemData> list = new ArrayList<>();
        //添加轮播Item
        ItemData layou1 = new ItemData();
        List<ItemLayout1> bannelData = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            ItemLayout1 bannel = new ItemLayout1();
            bannel.setTitle("bannel---->" + i);
            bannel.setImageUrl(DataUtils.getUrls(1,null).get(0));
            bannel.setLinkUrl(linkUrl);
            bannelData.add(bannel);
        }
        layou1.setType(ItemData.ITEM_TYPE_1);
        layou1.setData(bannelData);
        list.add(layou1);


        //添加title
        ItemData title1 = new ItemData();
        ItemTitleData titleData = new ItemTitleData();
        titleData.setTitle("下面是一行两个图片，宽 > 高");
        title1.setType(ItemData.ITEM_TYPE_5);
        title1.setData(titleData);
        list.add(title1);



        //添加每行两个图片，宽 > 高 的Item
        for (int i = 0; i < 10; i++){
            ItemData itemData2 = new ItemData();
            itemData2.setType(ItemData.ITEM_TYPE_2);
            ItemLayout1 layout2 = new ItemLayout1();
            layout2.setTitle("Layout2---->" + i);
            layout2.setImageUrl(DataUtils.getUrls(1,null).get(0));
            itemData2.setData(layout2);
            list.add(itemData2);
        }



        //添加title
        ItemData title3 = new ItemData();
        ItemTitleData titleData3 = new ItemTitleData();
        titleData3.setTitle("下面是一行两个图片，宽 < 高");
        title3.setType(ItemData.ITEM_TYPE_5);
        title3.setData(titleData3);
        list.add(title3);

        //添加每行两个图片，宽 < 高 的Item
        for (int i = 0; i < 12; i++){
            ItemData itemData3 = new ItemData();
            itemData3.setType(ItemData.ITEM_TYPE_3);
            ItemLayout1 layout3 = new ItemLayout1();
            layout3.setTitle("Layout3---->" + i);
            layout3.setImageUrl(DataUtils.getUrls(1).get(0));
            itemData3.setData(layout3);
            list.add(itemData3);
        }



        //添加title
        ItemData title4 = new ItemData();
        ItemTitleData titleData4 = new ItemTitleData();
        titleData4.setTitle("添加每行3个图片");
        title4.setType(ItemData.ITEM_TYPE_5);
        title4.setData(titleData4);
        list.add(title4);

        //添加每行3个图片
        for (int i = 0; i < 12; i++){
            ItemData itemData4 = new ItemData();
            itemData4.setType(ItemData.ITEM_TYPE_4);
            ItemLayout1 layout4 = new ItemLayout1();
            layout4.setTitle("Layout4---->" + i);
            layout4.setImageUrl(DataUtils.getUrls(1).get(0));
            itemData4.setData(layout4);
            list.add(itemData4);
        }



        //添加title
        ItemData title6 = new ItemData();
        ItemTitleData titleData6 = new ItemTitleData();
        titleData6.setTitle("横向滑动ListView");
        title6.setType(ItemData.ITEM_TYPE_5);
        title6.setData(titleData6);
        list.add(title6);

        //横向滑动ListView
        ItemData itemData6 = new ItemData();
        itemData6.setType(ItemData.ITEM_TYPE_6);
        List<ItemLayout1> data6 = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            ItemLayout1 layout6 = new ItemLayout1();
            layout6.setTitle("Layout6---->" + i);
            layout6.setImageUrl(DataUtils.getUrls(1).get(0));
            data6.add(layout6);
        }
        itemData6.setData(data6);
        list.add(itemData6);


        return list;
    }
}
