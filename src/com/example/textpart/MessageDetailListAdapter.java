package com.example.textpart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageDetailListAdapter extends BaseAdapter {
	TextToSpeech tts;
	List<Map<String,String>> list;
	Context context;
	DisplayMetrics dm;
	public MessageDetailListAdapter() {
		super();		
	}
	public View getView(int position,View convertView,ViewGroup parent){
		LinearLayout layout = null;		
		if(getItem(position).get("Receive").equals("1")){	
			layout = setReceiveTextView(position); 
			Log.i("gj", "收到短信");
		}else
		{
			layout = setSendTextView(position);
		Log.i("gj", "发出短信");	}		
		return layout;
	}
	public Map<String,String> getItem(int position){
		Log.i("gj", "list "+list.toString());
		Log.i("gj", "List Map"+list.get(position).toString());
		return list.get(position);
	}
	protected LinearLayout setReceiveTextView(int position){		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setPadding(10, 10, dm.widthPixels/8, 10);
		layout.setGravity(Gravity.LEFT);
		Log.i("gj", "左对齐");
		TextView textView = new TextView(context);
		textView.setPadding(10, 10, 10, 10);
		textView.setTextSize(18);
		textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.receive));
		textView.setText(this.getItem(position).get("body"));
		letTheTextViewBeAbleToSpeak(textView);		
		layout.addView(textView);
		return layout;
	}
	protected LinearLayout setSendTextView(int position){
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL); 
		layout.setPadding(dm.widthPixels/8, 10, 10, 10); 
		layout.setGravity(Gravity.RIGHT);//4:右对齐
		TextView textView = new TextView(context);
		textView.setPadding(10, 10, 20, 10);
		textView.setTextSize(18);
		textView.setText(this.getItem(position).get("body"));
		textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.send));		
		letTheTextViewBeAbleToSpeak(textView);	
		layout.addView(textView);
		return layout;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public long getItemId(int arg0) {
	
		return arg0;
	}
	
	private void letTheTextViewBeAbleToSpeak(TextView textView){
		final String message = textView.getText().toString();		
		textView.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				int i=tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
				if(i==TextToSpeech.SUCCESS)
					return true;
				else
				return false;				
			}});
		textView.setClickable(true);
		
		textView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					if(tts.isSpeaking())
						tts.stop();					
				}
				
			});
		}
		
	
}
