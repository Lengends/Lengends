package com.org.lengend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.org.lengend.activity.ScreenInfoActivity;
import com.org.lengend.adapter.MainAdapter;
import com.org.lengend.base.BaseActivity;
import com.org.lengend.base.OnRecyclerViewClickItemListener;
import com.org.lengend.coordinatorlayout.CoordinatorActivity;
import com.org.lengend.coordinatorlayout.DemoActivity;
import com.org.lengend.coordinatorlayout.ProfileActivity;
import com.org.lengend.entity.MainDataEntity;
import com.org.lengend.photoview.PhotoActivity;
import com.org.lengend.photoview.PhotoViewActivity;
import com.org.lengend.recyclerhead.ZoomHeadViewActivity;
import com.org.lengend.tablayout.TabLayoutActivity;

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

        MainDataEntity photoView = new MainDataEntity();
        photoView.setTitle("PhotoViewActivity");
        photoView.setDesc("图片放大、缩小、旋转功能");
        photoView.setGoClass(PhotoViewActivity.class);
        data.add(photoView);


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


        MainDataEntity profileActivity = new MainDataEntity();
        profileActivity.setTitle("ProfileActivity");
        profileActivity.setDesc("ProfileActivity");
        profileActivity.setGoClass(ProfileActivity.class);
        data.add(profileActivity);


        MainDataEntity demoActivity = new MainDataEntity();
        demoActivity.setTitle("DemoActivity");
        demoActivity.setDesc("DemoActivity");
        demoActivity.setGoClass(DemoActivity.class);
        data.add(demoActivity);


        MainDataEntity tabLayout = new MainDataEntity();
        tabLayout.setTitle("TabLayoutActivity");
        tabLayout.setDesc("TabLayoutActivity");
        tabLayout.setGoClass(TabLayoutActivity.class);
        data.add(tabLayout);


        MainDataEntity screenInfo = new MainDataEntity();
        screenInfo.setTitle("ScreenInfoActivity");
        screenInfo.setDesc("获取手机屏幕信息");
        screenInfo.setGoClass(ScreenInfoActivity.class);
        data.add(screenInfo);



        return data;
    }
}
