package com.example.textpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.textpart.data.DataService;
public class MainActivity extends Activity implements OnClickListener{
	private int deleteItemId;
	private List<String> listAllTreadId;
	private List<HashMap<String,Object>> list;
	private ListView listView;
	private Button searchStartButton,newMessageButton,contactSynButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("gj", "MainActivity OnCreat");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
		searchStartButton.setOnClickListener(this);
		newMessageButton.setOnClickListener(this);
		contactSynButton.setOnClickListener(this);		
		
	}
	private void init(){
		listAllTreadId = DataService.getAllTread_id(this);
		list = new ArrayList<HashMap<String,Object>>();		
		listView =(ListView)findViewById(R.id.MessageList);
		searchStartButton = (Button) findViewById(R.id.searchStartButton);
		newMessageButton = (Button) findViewById(R.id.newMessageButton);
		contactSynButton = (Button) findViewById(R.id.contactSynButton);
	}
	private void showTheList(ListView listView){
		Log.i("gj", "Read sqlite 1");
		list = DataService.getTheList(this);
		Log.i("gj", "Read sqlite 2");
		MainListAdapter adapter;
		adapter = new MainListAdapter(this,list,DataService.unReadMessageThread_id(this));		
		listView.setAdapter(adapter);
		Log.i("gj", "Display List");
	} 
	@Override  
    public boolean onContextItemSelected(MenuItem item) {  
		DataService.deleteMessageDialogByTread_Id(this,listAllTreadId.get(deleteItemId));
		listAllTreadId = DataService.getAllTread_id(this);
		showTheList(listView);
		return super.onContextItemSelected(item);  
    }
	private void letTheListViewItemCanBeDeleted(ListView listView){
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				deleteItemId = position;
				
				Log.i("gj", "长按");
				return false;
			}});
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.setHeaderTitle("删除会话？");     
                menu.add(0, 0, 0, "删除会话");     
            }  
        });   
	}
	protected void onStart(){
		super.onStart();
		letTheListViewItemCanBeDeleted(listView);
		Log.i("gj", "MainActivity OnStart");
		listAllTreadId = DataService.getAllTread_id(this);
		listView =(ListView)findViewById(R.id.MessageList);		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Log.i("gj", position+"被点击");
				Bundle data = new Bundle();				
				data.putSerializable("thread_id",listAllTreadId.get(position));
				Log.i("gj", listAllTreadId.get(position)+"!!");
				Intent intent = new Intent(MainActivity.this,MessageDetailActivity.class);
				intent.putExtras(data);
				startActivity(intent);
				Log.i("gj", position+"start");
				}
			}
		 );
		
	}
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		showTheList(listView);
		super.onResume();
	}
		@Override
		public void onClick(View arg0) {
		switch (arg0.getId())
		{
			case R.id.searchStartButton:
				searchStartButtonClicked();
				break;
			case R.id.newMessageButton:
				newMessageButtonClicked();
				break;
			case R.id.contactSynButton:
				contactSynButtonClicked();
				break;
			}
		}
		private void searchStartButtonClicked(){
			Log.i("gj", "searchStartButton");
			Intent intent = new Intent(MainActivity.this,SearchTextActivity.class);
			startActivity(intent);
		}
		private void newMessageButtonClicked(){
			Intent intent = new Intent(MainActivity.this,NewTextActivity.class);
			startActivity(intent);
		}
		private void contactSynButtonClicked(){
			Intent intent = new Intent(MainActivity.this,ContactActivity.class);
			startActivity(intent);
		}
		
}
/*		Log.i("gj","total: "+c.getCount());
if(c.moveToFirst()){
	for(int j=0;j<20;j++){
for(int i=0;i<c.getColumnCount();i++){
	Log.i("gj", c.getColumnName(i)+": "+c.getString(i));
}
c.moveToNext();
}
}else{Log.i("gj", "Something Wrong!");}
for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
{
    int nameColumn = cur.getColumnIndex(People.NAME);
    int phoneColumn = cur.getColumnIndex(People.NUMBER);
    String name = cur.getString(nameColumn);
    String phoneNumber = cur.getString(phoneColumn);
}
Log.i("gj","aa"+c.getColumnName(1));*/