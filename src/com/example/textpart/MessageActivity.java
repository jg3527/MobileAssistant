package com.example.textpart;


import com.example.textpart.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class MessageActivity extends Activity {
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("gj", "OnCreat");
	}
	protected void onStart(){
		super.onStart();
		Log.i("gj", "OnStart");
		LinearLayout layout = new LinearLayout(this);
		ScrollView scrollView = new ScrollView(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(layout);
		layout.setWeightSum(1);
		super.setContentView(layout);	
		Intent intent =getIntent();
		String thread_id = (String)intent.getSerializableExtra("thread_id");
		Log.i("gj", ""+thread_id);
		displayTheConversation(thread_id,layout);		
	}
	protected void onPause(){
		Log.i("gj", "messagePause");
		super.onPause();
		super.onDestroy();
	}
	protected void displayTheConversation(String thread_id,LinearLayout layout){
		Uri uri = Uri.parse("content://sms/");
		ContentResolver contentResolver = getContentResolver();
		Cursor c = contentResolver.query(uri, null, "thread_id"+ "=?" , new String[]{thread_id}, null);
		Log.i("gj", "display "+thread_id);
		c.moveToLast();
		this.setTitle("短信" +" ("+c.getString(2)+")");
		RelativeLayout layoutDetail[] =new RelativeLayout[c.getCount()];
		for(int i=0;i<c.getCount();i++){
			layoutDetail[i] = new RelativeLayout(this);			
			if(c.getString(9).equals("1")){//1:收到的短信在左边
				setReceiveTextView(layoutDetail[i],c.getString(12));
			}else{
				setSendTextView(layoutDetail[i],c.getString(12));
			}
			layout.addView(layoutDetail[i]);
			c.moveToPrevious();	
		}
		c.close();
	}
	protected void setReceiveTextView(RelativeLayout layout,String string){
		layout.setPadding(30, 10, 150, 10);
		layout.setGravity(2);
		TextView textView = new TextView(this);
		textView.setPadding(10, 10, 20, 10);
		textView.setTextSize(20);
		textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.receive));
		textView.setText(string);
		layout.addView(textView);
	}
	protected void setSendTextView(RelativeLayout layout,String string){
		layout.setPadding(150, 10, 30, 10); 
		layout.setGravity(4);//4:左对齐
		TextView textView = new TextView(this);
		textView.setPadding(10, 10, 20, 10);
		textView.setTextSize(20);
		textView.setText(string);
		textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.send));		
		layout.addView(textView);
	}
	
}
