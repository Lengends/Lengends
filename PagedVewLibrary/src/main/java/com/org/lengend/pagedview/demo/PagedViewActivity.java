package com.org.lengend.pagedview.demo;

import android.os.Bundle;

import com.org.lengend.pagedview.R;
import com.org.lengend.base.BaseActivity;
import com.org.lengend.pagedview.PageIndicatorView;
import com.org.lengend.pagedview.PagedView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyanfei on 2016/6/14.
 */
public class PagedViewActivity extends BaseActivity {

    private PagedView cyclePagedView;
    private MyPagedViewAdapter adapter;
    private List<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_pagedview);
    }

    @Override
    protected void initView() {
        urls.add("http://img4.imgtn.bdimg.com/it/u=1238985129,963468829&fm=21&gp=0.jpg");
        urls.add("http://img3.imgtn.bdimg.com/it/u=4258063781,3013778552&fm=21&gp=0.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=2231837044,3643597260&fm=21&gp=0.jpg");
        urls.add("http://pic0.mofang.com/2014/1113/20141113110312116.jpg");
        cyclePagedView = (PagedView) findViewById(R.id.cyclePagedView);
        adapter = new MyPagedViewAdapter(this);
        adapter.setImageUrls(urls);
        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        cyclePagedView.setPageIndicator(pageIndicatorView);
        //是否自动轮播
        cyclePagedView.setAutoPage(true);
        //设置自动轮播时间间隔
        cyclePagedView.setStepPageTime(3000);
        cyclePagedView.setAdapter(adapter);
        //设置当前显示的索引，从0开始
//        cyclePagedView.setCurrentIndex(5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cyclePagedView != null){
            cyclePagedView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cyclePagedView != null){
            cyclePagedView.onPause();
        }
    }
}
