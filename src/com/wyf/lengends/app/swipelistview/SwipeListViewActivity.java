package com.wyf.lengends.app.swipelistview;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wyf.lengends.R;
import com.wyf.lengends.app.base.BaseActivity;
import com.wyf.lengends.widget.swipelistview.SwipeListView;

public class SwipeListViewActivity extends BaseActivity{
	
	 private String[] contentItems = {
	            "Content Item 1", "Content Item 2", "Content Item 3",
	            "Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",
	            "Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
	            "Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",
	            "Content Item 16" 
	    };
	    
	   
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.swipelistview);
	  
	        final SwipeListView listView = (SwipeListView) findViewById(R.id.listview);
	        
	        DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
			int px = (int) (100 * (dm.densityDpi / 220f));
			listView.setOffsetLeft(mScreenWidth-px);
			
			
	        final MyAdapter adapter = new MyAdapter(this, contentItems);
	        listView.setAdapter(adapter);
	        
	        adapter.setDelListener(new OnBtnClickListener(){

				@Override
				public void onClick(int position) {
					Toast.makeText(SwipeListViewActivity.this, "deletde", Toast.LENGTH_SHORT).show();
					
				}	
			});
	    }
	    
	    class MyAdapter extends BaseAdapter{

	    	private Context context;
	    	private LayoutInflater inflater;
	    	private String[]  data;
	    	
	    	private OnBtnClickListener listener;
	    	/**
	    	 * @param listener the listener to set
	    	 */

	    	public void setDelListener(OnBtnClickListener listener){
	    		this.listener = listener;
	    	}
	    	
	    	public MyAdapter(Context context,String[] data) {
	    		this.context = context;
	    		inflater = LayoutInflater.from(context);
	    		this.data = data;
	    	}

	    	@Override
	    	public int getCount() {
	    		return data == null ? 0 : data.length;
	    	}

	    	@Override
	    	public long getItemId(int position) {
	    		return position;
	    	}

	    	@Override
	    	public View getView(final int position, View convertView, ViewGroup parent) {

	    		Holder holder = null;
	    		if (convertView == null) {
	    			holder = new Holder();
				convertView = inflater.inflate(R.layout.swipelistview_device_list_item, null);
	    			holder.img_device_icon = (ImageView) convertView.findViewById(R.id.list_device_icon);
	    			holder.tv_device_name = (TextView) convertView.findViewById(R.id.list_device_name);
	    			holder.toggbtn_switch = (ToggleButton) convertView.findViewById(R.id.list_device_switch);
	    			holder.btn_del = (ImageButton)convertView.findViewById(R.id.devicelist_del);
	    			convertView.setTag(holder);
	    		} else {
	    			holder = (Holder) convertView.getTag();
	    		}
	    		holder.tv_device_name.setText(data[position]);
	    		holder.btn_del.setOnClickListener(new OnClickListener() {	
	    			@Override
	    			public void onClick(View v) {
	    				if(null!=listener) listener.onClick(position);
	    			}
	    		});
	    		return convertView;
	    			
	    	}

	    	@Override
	    	public Object getItem(int arg0) {
	    		// TODO Auto-generated method stub
	    		return data[arg0];
	    	}
	    	
	    	
	    	class Holder {
	    		public ImageView img_device_icon;
	    		public TextView tv_device_name;
	    		public ToggleButton toggbtn_switch;
	    		
	    		public ImageButton btn_del;
	    		
	    	}
	    	
	    }
	    
	    public interface OnBtnClickListener{
    		public void onClick(int position);
    	}

}
