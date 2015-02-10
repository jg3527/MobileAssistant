package com.example.textpart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.example.textpart.data.DataService;

public class MessageDetailActivity extends FragmentActivity implements OnClickListener {
	private MessageDetailListAdapter adapter;
	private TextToSpeech tts;
	private EditText editText;
	private ListView listView;
	private DisplayMetrics dm;
	private Button speakButton,sendButton; 
    private BaiduASRDigitalDialog mDialog = null;
    private int mCurrentTheme = Config.DIALOG_THEME;
    private static final String API_KEY = "09uGHwnWmbETBc9UGtvvmeTo";
    private static final String SECRET_KEY = "CzipRW8M8fB9uhBLu4m8OvAVVK1QQE5k";
    private VoiceRecognitionClient mClient;
    private DialogRecognitionListener mRecognitionListener;
    private String address;
    private String thread_id;
    private int deleteMessageId;
    private List<Map<String,String>> list;
    public static final Uri SMS_URI = Uri.parse("content://sms/");
    private BroadcastReceiver sendMessage;
    
 	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		setContentView(R.layout.message_detail);
 		init();

		getContentResolver().registerContentObserver(SMS_URI, true, new SmsObserver(new Handler()));
		sendMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                    // 判断短信是否成功
                    switch (getResultCode()) {
                    case Activity.RESULT_OK:
                            Toast.makeText(MessageDetailActivity.this, "发送成功！", Toast.LENGTH_SHORT)
                                            .show();
                            Log.i("gj", "SUc");
                            break;
                    default:
                            Toast.makeText(MessageDetailActivity.this, "发送失败！", Toast.LENGTH_SHORT)
                                            .show();
                            Log.i("gj", "fail");
                            break;
                    }
            }
    };
    this.registerReceiver(sendMessage, new IntentFilter("SENT_SMS_ACTION"));
	}
 	private void init(){
 		listView = (ListView)findViewById(R.id.message_detail_listView);
		speakButton = (Button)findViewById(R.id.speakButton);
		sendButton = (Button)findViewById(R.id.sendButton);
		editText = (EditText)findViewById(R.id.editText1);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		speakButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
 		Intent intent =getIntent();
		thread_id= (String)intent.getSerializableExtra("thread_id");
		Log.i("gj", "vthread_id"+thread_id);
 	}
	protected void onStart(){
		super.onStart(); 				 
		showTheListView(dm);
	}
	protected void onPause(){
		Log.i("gj", "messagePause");
		//TODO if() 判断receiver 是否在
		this.unregisterReceiver(sendMessage);
		if(tts !=null)
			tts.shutdown();
		super.onPause();
	
	}
	private void showTheListView(DisplayMetrics dm){
		setMessageRead();
		
		Cursor c= DataService.getTheSpecificMessageContext(this, thread_id);
		c.moveToFirst();
		address = c.getString(2);
		if(DataService.readNameByPhoneNumber(this, c.getString(2))==null)
			this.setTitle("短信" +" ("+c.getString(2)+")");
		else
			this.setTitle("短信" +" ("+DataService.readNameByPhoneNumber(this, c.getString(2))+")");
		c.close();	
		adapter = new MessageDetailListAdapter();
		list = DataService.getTheSpecificMessageContextList(this, thread_id);		
		adapter.list = list;
		adapter.context = this;
		adapter.dm = dm;
		initiateTTS();
		adapter.tts = tts;
		listView.setAdapter(adapter);
		listView.setDivider(null);
		listView.setSelection(listView.getAdapter().getCount()-1);//直接显示底部	
		letTheListViewItemCanBeDeleted(listView);
	}
	private void letTheListViewItemCanBeDeleted(ListView listView){
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				deleteMessageId = position;
				
				Log.i("gj", "长按");
				return false;
			}});
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
            
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                menu.setHeaderTitle("删除信息？");     
                menu.add(0, 0, 0, "删除信息");     
            }  
        });   
	}
	@Override  
    public boolean onContextItemSelected(MenuItem item) {  
		DataService.deleteMessageDialogBy_Id(this,list.get(deleteMessageId).get("_id"));
		list = DataService.getTheSpecificMessageContextList(this, thread_id);
		refreshTheList();
		return super.onContextItemSelected(item);  
    }
	private void initiateTTS(){
		tts	= new TextToSpeech(this,new OnInitListener(){
			@Override
			public void onInit(int status) {
				if(status==TextToSpeech.SUCCESS)
				{
					int result = tts.setLanguage(Locale.CHINESE);
					if(result != TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE&&result!=TextToSpeech.LANG_AVAILABLE)
					{
						Log.i("gj", "@@@@"+result);																
					}					
				}
			}					
		});
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case  R.id.speakButton :
			speechButtonClicked();			
			break;
		case R.id.sendButton :
			sendButtonClicked();
			break;
		}
	}
	private void speechButtonClicked(){
		mClient = VoiceRecognitionClient.getInstance(this);
        mClient.setTokenApis(API_KEY, SECRET_KEY);
        mRecognitionListener = new DialogRecognitionListener() {

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> rs = results != null ? results
                        .getStringArrayList(RESULTS_RECOGNITION) : null;
                if (rs != null && rs.size() > 0) {
                	editText.setText(editText.getText()+rs.get(0));
                }

            }
        };
		//editText.setText(null);
        if (mDialog == null || mCurrentTheme != Config.DIALOG_THEME) {
            mCurrentTheme = Config.DIALOG_THEME;
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Bundle params = new Bundle();
            params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
            params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
            params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, Config.DIALOG_THEME);
            params.putInt( BaiduASRDigitalDialog.PARAM_SPEECH_MODE,BaiduASRDigitalDialog.SPEECH_MODE_INPUT);
            mDialog = new BaiduASRDigitalDialog(this, params);
            mDialog.setDialogRecognitionListener(mRecognitionListener);
        }
        mDialog.setSpeechMode(Config.VOICE_TYPE == Config.VOICE_TYPE_SEARCH ? BaiduASRDigitalDialog.SPEECH_MODE_SEARCH
                : BaiduASRDigitalDialog.SPEECH_MODE_INPUT);
        mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_NLU_ENABLE,
                Config.enableNLU);
        mDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE,
                Config.getCurrentLanguage());
        mDialog.show();
	}
	private void sendButtonClicked(){
		SmsManager smsManager = SmsManager.getDefault();
		String sms_content = editText.getText().toString();
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent("SENT_SMS_ACTION"), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent("SMS_DELIVERED"), 0);


		if(sms_content.length() > 70) {
            List<String> contents = smsManager.divideMessage(sms_content);
            for(String sms : contents) {
            	smsManager.sendTextMessage(address, null, sms, sentPI, deliveredPI);
            	insertToSqlite(sms);            	
            }                        
        } else {
        	smsManager.sendTextMessage(address, null, sms_content, sentPI, deliveredPI);
            insertToSqlite(sms_content);
        }
		editText.setText("");
	               
	}
	private void insertToSqlite(String body){
		ContentValues cv = new ContentValues();	 
		   cv.put("thread_id", thread_id);	
	       cv.put("date", System.currentTimeMillis());
	       cv.put("body", body);
	       cv.put("read", 0);
	       cv.put("type", 2);
	       cv.put("address", address);
	       this.getContentResolver().insert(SMS_URI, cv);	     
			}
	private final class SmsObserver extends ContentObserver{

		public SmsObserver(Handler handler) {
			super(handler);
		}
		public void onChange(boolean selfChange)
		{
			refreshTheList();
			Log.i("gj", "SmsObserver");
		}
		
	}
	private void refreshTheList(){
		setMessageRead();
		Log.i("gj", "refreshTheList");
		Log.i("Thread_id", thread_id);
		list = DataService.getTheSpecificMessageContextList(this, thread_id);		
		adapter.list = list;
		listView.setAdapter(adapter);
		listView.setSelection(listView.getAdapter().getCount()-1);
	}
	private void setMessageRead()
	{
		HashSet<String> unReadThread_id = DataService.unReadMessageThread_id(this); 
		if(DataService.isUnreadMessageExist(this)&&unReadThread_id.contains(thread_id)){
			HashSet<String> set = DataService.unReadMessage_id(this,thread_id);
			Log.i("gj", "1111@"+thread_id);
			Log.i("gj", ""+set.toString());
			List<String> list = new ArrayList<String>();
			list.addAll(set);
		      ContentValues values = new ContentValues();  
              values.put("read", "1");        //修改短信为已读模式  
              for(int i = 0;i<list.size();i++){
              getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{""+list.get(i)}); 
              }
		}
	}
}

