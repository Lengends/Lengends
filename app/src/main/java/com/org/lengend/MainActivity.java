package com.org.lengend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.org.lengend.adapter.MainAdapter;
import com.org.lengend.base.BaseActivity;
import com.org.lengend.entity.MainDataEntity;
import com.org.lengends.photoview.PhotoActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(MainActivity.this);
        adapter.setData(getData());
//        adapter.setOnRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<MainDataEntity> getData(){
        ArrayList<MainDataEntity> data = new ArrayList<>();

        MainDataEntity photo = new MainDataEntity();
        photo.setTitle("PhotoActivity");
        photo.setDesc("图片放大、缩小、旋转功能");
        photo.setGoClass(PhotoActivity.class);
        data.add(photo);
        data.add(photo);



        return data;
    }
}
