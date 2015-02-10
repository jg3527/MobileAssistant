package com.example.textpart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.textpart.data.DataService;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class SearchTextActivity extends Activity implements OnItemClickListener,SearchView.OnQueryTextListener,OnCheckedChangeListener{

	private SearchView searchView;
	private ListView listView;
	private DisplayMetrics dm;
	private int isListView1InTheDetailPage,isListView3InTheDetailPage,isContext,isNameOrPhoneNumber,isDate;
	private List<HashMap <String,Object>> list1;
	private List<Map <String,String>> list2;
	private List<HashMap <String,Object>> list3;
	private TabHost tabHost;
	private String queryPrivate;
	//1:按内容，2：按号码或姓名，3：按时间
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.search_text);
		Log.i("gj", "searchPage2");
		listView = (ListView)findViewById(R.id.searchTextListView);
		
		searchView = (SearchView) findViewById(R.id.searchView);
		searchView.setIconified(false);
		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(true);
		searchView.setOnQueryTextListener(this);
		dm = new DisplayMetrics();
		
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		RadioGroup rg = (RadioGroup)findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(this);
		isListView1InTheDetailPage = 0;
		isListView3InTheDetailPage = 0;
		isContext = 0;
		isNameOrPhoneNumber = 0;
		isDate = 0;
		
	}
	@Override
	public boolean onQueryTextSubmit(String query){
		Log.i("gj","ButtonSubmit");
		if(query.equals(""))
			Toast.makeText(this, "请输入查询的关键字", Toast.LENGTH_LONG);
		else{
			queryPrivate = query;		
			if(isContext==1){
				showTheListView1(query);
				}else if(isNameOrPhoneNumber==1){
				showTheListView2(query);
				}else if(isDate==1){
				showTheListView3(query);
				}else{
					Log.i("gj", "Radio Button");
					Toast.makeText(this, "请选择查询方式", Toast.LENGTH_LONG);
				}			
		}
		return false;
	
	}
	private void showTheListView1(String query){
		list1 = queryByContext(query);
		displayTheListView1();
	}
	private void displayTheListView1(){
		SimpleAdapter simpleAdapter1 = new SimpleAdapter(this,list1,R.layout.text_list,new String[]{"addressOrPerson","lastConversation"},new int[]{R.id.name,R.id.message});		
		listView.setAdapter(simpleAdapter1);		
		listView.setOnItemClickListener(this);
	}
	
	private void showTheListView2(String query){
		list2 = queryByNumberOrName(query);
		MessageDetailListAdapter adapter = new MessageDetailListAdapter();
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		adapter.list = list2;
		adapter.context = this;
		adapter.dm = dm;
		listView.setAdapter(adapter);
		//listView.setDivider(null);
	}
	private void showTheListView3(String query){
		list3 = queryByTime(query);
		displayTheListView3();
	}
	private void displayTheListView3(){
		SimpleAdapter simpleAdapter3 = new SimpleAdapter(this,list3,R.layout.text_list,new String[]{"addressOrPerson","lastConversation"},new int[]{R.id.name,R.id.message});		
		listView.setAdapter(simpleAdapter3);
		listView.setOnItemClickListener(this);
	}
	private List<HashMap <String,Object>> queryByContext(String query){
		
		return DataService.getTheListBySpecificContent(this, query);
		
	}
	private List<Map<String,String>> queryByNumberOrName(String query){
		
		if(!isStringAllNumber(query)){
			String phoneNumber = DataService.readphoneNumberByFuzzName(this, query);
			//phoneNumber = phoneNumber.replace("(", "");
			//phoneNumber = phoneNumber.replace(")", "");
			Log.i("gj", "phoneNumber:"+phoneNumber);
			if(phoneNumber!=null){

				
				Log.i("gj", DataService.getTheSpecificMessageContextListByNumber(this, phoneNumber).toString()+"!@#");
				return DataService.getTheSpecificMessageContextListByNumber(this, phoneNumber);
			}
			else
				return null;	
		}else{
			
			return DataService.getTheSpecificMessageContextListByNumber(this, query);
		}
		
	}
	private List<HashMap<String,Object>> queryByTime(String query){
		
		return DataService.getTheListBySpecificTime(this,query);
	}
	private boolean isStringAllNumber(String string){
		Pattern pattern = Pattern.compile("[0-9]+1");
		Matcher matcher = pattern.matcher((CharSequence)string);
		boolean result = matcher.matches();		 
		return result;
	}
	@Override
	public void onBackPressed(){
		if(isListView1InTheDetailPage==1)
		{
			isListView1InTheDetailPage=0;
			displayTheListView1();
		}else if(isListView3InTheDetailPage==1){
			isListView3InTheDetailPage=0;
			displayTheListView3();
		}else{
		super.onBackPressed();
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			if(isContext==1){
			isListView1InTheDetailPage = 1;
			MessageDetailListAdapter mAdapter = new MessageDetailListAdapter();
			mAdapter.list = (List<Map<String, String>>) list1.get(position).get("conversation");
			mAdapter.context = this;
			mAdapter.dm = dm;
			listView.setAdapter(mAdapter);
			listView.setDivider(null);
			}else{
				isListView3InTheDetailPage = 1;
				MessageDetailListAdapter mAdapter = new MessageDetailListAdapter();
				mAdapter.list = (List<Map<String, String>>) list3.get(position).get("conversation");
				mAdapter.context = this;
				mAdapter.dm = dm;
				listView.setAdapter(mAdapter);
				listView.setDivider(null);
			}
			}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case(R.id.context):
			setIsContextOne();
			break;
		case(R.id.nameOrPhone):
			setIsNameOrPhoneNumberOne();
			break;
		case(R.id.date):
			setIsDateOne();
			break;
		}
		
	}
	private void setIsContextOne(){
		
		isContext = 1;
		isNameOrPhoneNumber = 0;
		isDate = 0;
	}
	private void setIsNameOrPhoneNumberOne(){
		isContext = 0;
		isNameOrPhoneNumber = 1;
		isDate = 0;
	}
	private void setIsDateOne(){
		isContext = 0;
		isNameOrPhoneNumber = 0;
		isDate = 1;
	}

	}
