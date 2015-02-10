package com.example.textpart;



import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;



import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity {
	private static final String App_Id = "wxb0fe8f827930b26a";
	private IWXAPI api;
	private Button WXStartButton,txtContactSynButton;
	private TextView hintTextView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);
		//��appע�ᵽ΢�ţ�΢���ն˲�����Ӧ��app
		api = WXAPIFactory.createWXAPI(this, App_Id, false);
		api.registerApp(App_Id);
 		
		WXStartButton = (Button)findViewById(R.id.wx_start);
		hintTextView = (TextView)findViewById(R.id.hintTextView);
		txtContactSynButton = (Button)findViewById(R.id.txtContactSyn);
		txtContactSynButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ContactActivity.this,ContactSynTxtActivity.class);
				startActivity(intent);
				
			}
			
		});
		WXStartButton.setOnClickListener(new OnClickListener(){
		//Toast.makeText(this, "�밲װ΢�ţ�", Toast.LENGTH_LONG);
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(api.isWXAppInstalled())
				{
					api.openWXApp();
				
				}else
				{
					hintTextView.setVisibility(0);
				}
				
			}
			
		});
	}
}
