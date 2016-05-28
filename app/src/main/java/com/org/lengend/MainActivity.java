package com.org.lengend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.org.lengend.adapter.MainAdapter;
import com.org.lengend.base.BaseActivity;
import com.org.lengend.base.OnRecyclerViewClickItemListener;
import com.org.lengend.coordinatorlayout.CoordinatorActivity;
import com.org.lengend.entity.MainDataEntity;
import com.org.lengend.photoview.PhotoActivity;
import com.org.lengend.recyclerhead.ZoomHeadViewActivity;

import java.util.ArrayList;

//import com.org.lengend.coordinatorlayout.CoordinatorActivity;

public class MainActivity extends BaseActivity implements OnRecyclerViewClickItemListener{

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        System.out.println("=====recyclerView======"+recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(MainActivity.this);
        adapter.setData(getData());
        adapter.setOnRecyclerViewClickItemListener(MainActivity.this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(Object data, int position) {
        MainDataEntity entity = (MainDataEntity) data;
        Intent intent = new Intent(MainActivity.this, entity.getGoClass());
        startActivity(intent);
    }

    private ArrayList<MainDataEntity> getData(){
        ArrayList<MainDataEntity> data = new ArrayList<>();

        MainDataEntity photo = new MainDataEntity();
        photo.setTitle("PhotoActivity");
        photo.setDesc("图片放大、缩小、旋转功能");
        photo.setGoClass(PhotoActivity.class);
        data.add(photo);


        MainDataEntity headView = new MainDataEntity();
        headView.setTitle("ZoomHeadViewActivity");
        headView.setDesc("HeadView放大缩小");
        headView.setGoClass(ZoomHeadViewActivity.class);
        data.add(headView);


        MainDataEntity coordinator = new MainDataEntity();
        coordinator.setTitle("CoordinatorActivity");
        coordinator.setDesc("CoordinatorActivity");
        coordinator.setGoClass(CoordinatorActivity.class);
        data.add(coordinator);



        return data;
    }
}
