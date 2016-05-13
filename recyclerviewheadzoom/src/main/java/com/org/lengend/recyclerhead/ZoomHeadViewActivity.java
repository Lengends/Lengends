package com.org.lengend.recyclerhead;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.org.lengend.base.BaseActivity;
import com.org.lengend.base.OnRecyclerViewClickItemListener;

/**
 * Created by wangyanfei on 2016/5/12.
 */
public class ZoomHeadViewActivity extends BaseActivity implements OnRecyclerViewClickItemListener,HeadViewListener{

    private RecyclerZoomHeadView recyclerView;
    private RecyclerHeadAdapter adapter;
    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        initView();
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerZoomHeadView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            private int totalDy = 0;
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                totalDy -= dy;
//                System.out.println("========totalDy========"+totalDy);
//                // setTranslation/Alpha here according to totalDy.
//            }
//        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ZoomHeadViewActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerHeadAdapter(ZoomHeadViewActivity.this);
//        adapter.setRecyclerView(recyclerView);
        adapter.setOnRecyclerViewClickItemListener(ZoomHeadViewActivity.this);
        adapter.setHeadViewListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Object data, int position) {

    }

    @Override
    public View getHeadView() {
        if(headView == null){
            headView = View.inflate(this,R.layout.headview,null);
        }
        return headView;
    }
}
