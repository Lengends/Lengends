package com.wyf.lengends.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wyf.lengends.R;
import com.wyf.lengends.app.base.BaseActivity;
import com.wyf.lengends.app.swipelistview.SwipeListViewActivity;
import com.wyf.lengends.app.swipelistview.SwipeListViewMenuActivity;

public class MainActivity extends BaseActivity {
	
	private ListView listview;
	private ListViewAdapter adapter;
	private ArrayList<MaintItem> list = new ArrayList<MainActivity.MaintItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		init();
		listview = (ListView) findViewById(R.id.listview);
		adapter = new ListViewAdapter();
		listview.setAdapter(adapter);
	}
	
	private void init() {
		
		MaintItem swipelist = new MaintItem();
		swipelist.title = "SwipeListView";
		swipelist.desc = "滑动删除Item的ListView";
		swipelist.listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SwipeListViewActivity.class);
				startActivity(intent);
			}
		};
		list.add(swipelist);
		
		
		MaintItem swipelist_menu = new MaintItem();
		swipelist_menu.title = "SwipeMenuListView";
		swipelist_menu.desc = "滑动删除Item的ListView";
		swipelist_menu.listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SwipeListViewMenuActivity.class);
				startActivity(intent);
			}
		};
		list.add(swipelist_menu);
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	public class ListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public MaintItem getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder;
			
			if(convertView == null){
				convertView = View.inflate(MainActivity.this, R.layout.main_item, null);
				holder = new HolderView();
				holder.item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
				holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
				holder.item_desc = (TextView) convertView.findViewById(R.id.item_desc);
				convertView.setTag(holder);
			}else{
				holder = (HolderView) convertView.getTag();
			}
			
			MaintItem item = getItem(position);
			
			holder.item_title.setText(item.title);
			holder.item_desc.setText(item.desc);
			
			if(item.resId == 0){
				holder.item_icon.setImageResource(R.drawable.ic_launcher);
			}else{
				holder.item_icon.setImageResource(item.resId);
			}
			convertView.setOnClickListener(item.listener);
			return convertView;
		}
		
		
		private class HolderView{
			ImageView item_icon;
			TextView item_title;
			TextView item_desc;
		}

	}
	
	private class MaintItem{
		String title;
		String desc;
		int resId;
		OnClickListener listener;
	}


}
