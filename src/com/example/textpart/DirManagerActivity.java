package com.example.textpart;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DirManagerActivity extends Activity implements OnItemClickListener{
	private ListView lv;
	private String isOutput;
	private List<String> pathList;
	private Button okButton,cancelButton;
	private DirManagerListAdapter adapter;
	private String path;
	private MyFileFilter MF;//第一个为当前目录，剩余为当前目录下的
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dir_manager);		
		lv = (ListView)findViewById(R.id.fileListView);
		okButton = (Button)findViewById(R.id.okButton);
		cancelButton = (Button)findViewById(R.id.cancleButton);
		cancelButton.setText("shenme?");
		//okButton.setOnClickListener(this);
		//cancelButton.setOnClickListener(this);
		lv.setOnItemClickListener(this);
		Intent intent =getIntent();
		isOutput= (String)intent.getSerializableExtra("isOutput");
		pathList = fileList("/");
		path = "/";
		listViewSetAdapter();
		MF = new MyFileFilter();
				 
	}
	private void listViewSetAdapter(){
		Log.i("gj", "adapter set");
		adapter = new DirManagerListAdapter(this,pathList);
		lv.setAdapter(adapter);
	}
	private class MyFileFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String filename) {
			if(filename.endsWith(".txt")||new File(filename).isDirectory())
				return true;
			else 
				return false;
		}
		
	}
	private List<String> fileList(String path)	{
		List<String> list = new ArrayList<String>();
		list.add(path);
		Log.i("gj", "path: "+path);
		File tempFile = new File(path);
		File[] files = tempFile.listFiles();
		if(files!=null){
		for (int i = 0; i < files.length; i++) {  
            File file = files[i];  
            if(isOutput.equals("1")){
            	if(file.isDirectory()&&file.canWrite())
            		list.add(file.getName());
            }else{
            	if((file.isDirectory()||file.getName().endsWith(".txt"))&&file.canRead())
            	list.add(file.getName());
            }
        }  
		}
		return list;
	}
	private String getParentPath(String path){
		String parentPath = new String();
		File file = new File(path);
		if(!path.equals("/"))
		parentPath = file.getParent();
		return parentPath;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//List<String> list = fileList((String) tv.getText());
		if(position==0){
			//返回上一级
			if(!path.equals("/")){
			path = getParentPath(path);
			Log.i("gj", "parent path:"+path);
			pathList = fileList(path);
			listViewSetAdapter();
			}
		}else{
			if(!path.equals("/"))
			{
			path = path+"/"+pathList.get(position);
			}else
				path = path+pathList.get(position);
			pathList = fileList(path);
			listViewSetAdapter();
		}
		
	}
	
	
	public void okButtonClicked(View v){
		if(pathList.get(0).endsWith(".txt"))
		{
		Intent data = new Intent(DirManagerActivity.this, ContactSynTxtActivity.class);  		
        Bundle bundle = new Bundle();  
        bundle.putString("file", pathList.get(0));  
        data.putExtras(bundle);  
        setResult(2, data);  
        finish(); 
        } else{
        	Toast.makeText(this, "请选择Txt文件！！", Toast.LENGTH_LONG);
        }
	}
	public void cancelButtonClicked(View v){
		Log.i("gj", "cancelButton clicked");
		super.finish();
		
	}
}
