package com.example.textpart;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DirManagerListAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	
	public DirManagerListAdapter(Context context, List<String> list){
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout layout = new LinearLayout(context);
		if(position==0){
			//Log.i("gj", "表头创建");
			layout = createListItemView(true,0);
		}else{
			//Log.i("gj", "get View： "+position);
			layout = createListItemView(false,position);
		}
		return layout;
	}
	private LinearLayout createListItemView(boolean isHeader,int position){
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		ImageView img = new ImageView(context);
		if(isHeader){
			//Log.i("gj", "表头背景图片");	
			img.setBackgroundResource(R.drawable.scan_back_dir);
		}
		else if (list.get(position).endsWith(".txt"))
			img.setBackgroundResource(R.drawable.fileicon_txt);
		else{
			//Log.i("gj", position+"backgroud");
			img.setBackgroundResource(R.drawable.scan_ic_folder);}
		TextView tv = new TextView(context);
		tv.setText(list.get(position));
		layout.addView(img);
		layout.addView(tv);
		return layout;
	}
	

}
