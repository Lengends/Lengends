package com.wyf.lengends.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wyf.lengends.R;

public class TextAdatper extends BaseAdapter{
	private ArrayList<String> data;
	private Context context;
	
	public TextAdatper(Context context, ArrayList<String> data) {
		this.context = context;
		this.data = data;
	}
	
	public void setData(ArrayList<String> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = View.inflate(context, R.layout.text_layout, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.textView1);
		tv.setText(getItem(position));
		return convertView;
	}

}
