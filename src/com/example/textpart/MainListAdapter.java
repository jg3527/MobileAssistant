package com.example.textpart;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.example.textpart.data.DataService;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainListAdapter extends BaseAdapter {
	private HashSet<String> unreadMessageSet ;
	private Context context;
	private List<HashMap<String,Object>> list;
	
	public MainListAdapter(Context context,List<HashMap<String,Object>> data, HashSet<String> set) {
		this.context = context;
		this.list = data;
		this.unreadMessageSet = set;
	
	}
	public View getView(int position,View convertView,ViewGroup parent){
		LinearLayout layout ;//= setItemView((String)this.getItem(position).get("addressOrPerson"),(String)this.getItem(position).get("lastConversation"));
		//Log.i("gj", "unreadMessageSet null?"+(unreadMessageSet==null));
		//Log.i("gj", "contain?"+unreadMessageSet.contains(this.getItem(position).get("thread_id")));
		if(unreadMessageSet!=null&&unreadMessageSet.contains(this.getItem(position).get("thread_id")))
			{
			int unreadNum = DataService.unReadMessage_idNum(context, (String)this.getItem(position).get("thread_id"));
			Log.i("gj", "unreadNum: "+unreadNum);
			layout = setContainUnreadMessageItemView((String)this.getItem(position).get("addressOrPerson"),(String)this.getItem(position).get("lastConversation"),unreadNum);
			//setUnreadMessageTextView(layout,DataService.unReadMessage_idNum(context, (String)this.getItem(position).get("thread_id")));
			}else{
				Log.i("gj", "no unread");
			layout = setItemView((String)this.getItem(position).get("addressOrPerson"),(String)this.getItem(position).get("lastConversation"));
			}
		return layout;
	}
	public Map<String,Object> getItem(int position){		
		return list.get(position);
	}
	private LinearLayout setItemView(String displayName,String message){
		LinearLayout layout = new LinearLayout(context);
		TextView textView1 = new TextView(context);
		TextView textView2 = new TextView(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		textView1.setText(displayName);
		textView2.setText(message);
		textView1.setText(displayName);
		textView1.setTextSize(20);
		textView1.setPadding(10, 0, 0, 0);
		textView2.setText(message);
		textView2.setSingleLine(true);
		textView2.setEllipsize(TruncateAt.END);
		textView2.setPadding(10, 2, 0, 4);
		textView2.setTextSize(14);
		layout.addView(textView1);
		layout.addView(textView2);
		return layout;
	}
	private LinearLayout setContainUnreadMessageItemView(String displayName,String message,int unReadMessageNum){
		LinearLayout layout = new LinearLayout(context);
		LinearLayout layout1 = new LinearLayout(context);
		layout1.setOrientation(LinearLayout.HORIZONTAL);
		TextView tv = new TextView(context);
		tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.unread_message));
		//tv.setBackgroundColor(R.color.LemonChiffon);
		//tv.setBackground(context.getResources().getDrawable(R.drawable.unread_message));
		//tv.setBackground(context.getResources().getDrawable(R.drawable.unread_message));
		Log.i("gj", "urmN: "+unReadMessageNum);
		tv.setText(unReadMessageNum+"");
		TextView textView1 = new TextView(context);
		TextView textView2 = new TextView(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		textView1.setText(displayName);
		textView2.setText(message);
		textView1.setText(displayName);
		textView1.setTextSize(20);
		textView1.setPadding(10, 0, 0, 0);
		textView2.setText(message);
		textView2.setSingleLine(true);
		textView2.setEllipsize(TruncateAt.END);
		textView2.setPadding(10, 2, 0, 4);
		textView2.setTextSize(14);
		layout1.addView(textView1);
		layout1.addView(tv);
		layout.addView(layout1);
		layout.addView(textView2);
		return layout;
	}
	
//	private void setUnreadMessageTextView(LinearLayout layout, int unReadMessageNum)
//	{
//		TextView tv = new TextView(context);
//		tv.setBackground(context.getResources().getDrawable(R.drawable.unread_message));
//		tv.setText(unReadMessageNum);
//		layout.addView(tv);
//		layout.setBackgroundResource(R.color.LemonChiffon);
//	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}


}
