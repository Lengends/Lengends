package com.org.lengend.recycleview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.org.lengend.base.BaseActivity;
import com.org.lengend.base.DataUtils;
import com.org.lengend.recycleview.entity.ItemLayout1;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangyanfei on 2016/6/17.
 */
public class HorizontalRecycleViewActivity extends BaseActivity{
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view_activity);
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        HorizontalAdapter adapter = new HorizontalAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<ItemLayout1> list = new ArrayList<>();
        List<String> urls = DataUtils.getUrls(20);
        for (String url : urls){
            ItemLayout1 layout1 = new ItemLayout1();
            layout1.setImageUrl(url);
            list.add(layout1);
        }

        adapter.setUrls(list);
        mRecyclerView.setAdapter(adapter);
    }
}
