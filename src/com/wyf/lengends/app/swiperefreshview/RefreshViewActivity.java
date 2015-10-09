package com.wyf.lengends.app.swiperefreshview;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import com.wyf.lengends.R;
import com.wyf.lengends.app.TextAdatper;
import com.wyf.lengends.app.base.BaseActivity;
import com.wyf.lengends.app.swiperefreshview.SwipyRefreshLayout.OnRefreshListener;

public class RefreshViewActivity extends BaseActivity {

	private SwipyRefreshLayout mSwipeRefreshLayout;
	private ListView listView;
	private ArrayList<String> data = new ArrayList<String>();
	private TextAdatper adapter;
	private Handler mHandler = new Handler(){ 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case 1: 
            	adapter.setData(data);
            	adapter.notifyDataSetChanged(); 
            	mSwipeRefreshLayout.setRefreshing(false); 
                //swipeRefreshLayout.setEnabled(false); 
                break; 
            default: 
                break; 
            } 
        } 
    }; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swiperefreshview);
		mSwipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
		listView = (ListView) findViewById(R.id.listView);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
//		mSwipeRefreshLayout
//				.setProgressBackgroundColor(R.color.swipe_background_color);
//		mSwipeRefreshLayout.setPadding(20, 20, 20, 20); 
//		mSwipeRefreshLayout.setProgressViewOffset(true, 0, 24); 
//		mSwipeRefreshLayout.setDistanceToTriggerSync(50); 
//		mSwipeRefreshLayout.setProgressViewEndTarget(true, 80);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(final SwipyRefreshLayoutDirection direction) {
				Log.d("MainActivity", "Refresh triggered at "
		                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
				new Thread(new Runnable() { 
                    @Override 
                    public void run() { 
                    	if(direction == SwipyRefreshLayoutDirection.TOP){ //下拉刷新
                    		data.clear(); 
                            for(int i=0;i<20;i++){ 
                                data.add("SwipeRefreshLayout下拉刷新"+i); 
                            } 
                    	}else if(direction == SwipyRefreshLayoutDirection.BOTTOM){//加载更多
                    		int size = data.size();
                    		for(int i = size; i < size + 20; i++){ 
                                data.add("SwipeRefreshLayout下拉刷新"+i); 
                            } 
                    	}
                        mHandler.sendEmptyMessageDelayed(1, 5000);
                    } 
                }).start(); 
			}
		});
		data.add("1111111111111111");
		data.add("2222222222222222");
		data.add("3333333333333333");
		data.add("4444444444444444");
		data.add("5555555555555555");
		data.add("6666666666666666");
		data.add("7777777777777777");
		data.add("8888888888888888");
		data.add("9999999999999999");
		data.add("aaaaaaaaaaaaaaaa");
		data.add("bbbbbbbbbbbbbbbb");
		data.add("cccccccccccccccc");
		data.add("dddddddddddddddd");
		data.add("eeeeeeeeeeeeeeee");
		data.add("ffffffffffffffff");
		data.add("gggggggggggggggg");
		data.add("hhhhhhhhhhhhhhhh");
		data.add("iiiiiiiiiiiiiiii");
		data.add("jjjjjjjjjjjjjjjj");
		adapter = new TextAdatper(this, data);
		listView.setAdapter(adapter);
	}
}
