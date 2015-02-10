package com.example.textpart;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class ContactSynTxtActivity extends TabActivity implements OnClickListener{
	public static final int FILE_RESULT_CODE = 1;  
	private Button outputChoosePathButton,inputChoosePathButton,outputButton,inputButton;
    private TextView outputPathTextView,outputHintTextView,inputPathTextView,inputHintTextView,outputInstructionTextView,inputInstructionTextView; 
    private String outputPath,inputPath;
    private Thread mInsertThread,mOutputThread;
    private int isTab1;
    private TabHost tabHost;
	public static final String HELP_MESSAGE_INPUT = 
	"������ϵ��ʹ��˵��: \n" + 
	"�ļ���ʽ����:\n " + 
	"���� �ֻ��� סլ�绰 \n" +
	"ÿһ���Կո�ָ��������Ϊ�գ��ֻ��ź�סլ�绰����Ϊ�� ,�ظ�����ᴴ���µ���ϵ�ˣ������ã�";
	public static final String HELP_MESSAGE_OUTPUT =
	"������ϵ��ʹ��˵���� \n" + 
	"Ĭ�ϵĵ����ļ���Ϊ'�ҵ���ϵ��.txt'" +
	"������ϰ����Ϣֻ�����������ֻ��ź�סլ�绰�����ܻᶪʧ������Ϣ \n\n" +
	"***����ļ�����word�򿪣���ʱѡ���ַ�����utf-8��������������" +
	"***����USB��ʱ���޷����뵼��!";
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ContactContant.INSERT_FAIL:
				inputHintTextView.setText(ContactContant.FAIL_INSERT);
				endInsertContact();
				break;
			case ContactContant.INSERT_SUCCESS:
				inputHintTextView.setText(String.format(
						ContactContant.SUCCESS_INSERT,
						ContactToolInsertUtils.getSuccessCount(),
						ContactToolInsertUtils.getFailCount()));
				endInsertContact();
				break;
			case ContactContant.OUTPUT_FAIL:
				outputHintTextView.setText(ContactContant.FAIL_OUTPUT);
				endOutputContact();
				break;
			case ContactContant.OUTPUT_SUCCESS:
				outputHintTextView.setText((String.format(
						ContactContant.SUCCESS_OUTPUT,
						ContactToolOutputUtils.getCount())));
				endOutputContact();
				break;
			}
		}
	};
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.contact_sync); 
        outputPath = "/";
        tabHost = getTabHost();
        TabSpec tab1 = tabHost.newTabSpec("tab1").setIndicator("����").setContent(R.id.tab1);
		TabSpec tab2 = tabHost.newTabSpec("tab2").setIndicator("����").setContent(R.id.tab2);
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		outputInstructionTextView = (TextView)findViewById(R.id.instructionTextView);
		inputInstructionTextView = (TextView)findViewById(R.id.instructionTextView1);
		outputInstructionTextView.setText(HELP_MESSAGE_OUTPUT);
		inputInstructionTextView.setText(HELP_MESSAGE_INPUT);
		outputChoosePathButton = (Button)findViewById(R.id.choosePathButton);  
        inputChoosePathButton = (Button)findViewById(R.id.choosePathButton1);
        outputPathTextView = (TextView)findViewById(R.id.fileText); 
        outputPathTextView.setText("��ǰ·��Ϊ����Ŀ¼");
        inputPathTextView = (TextView)findViewById(R.id.fileText1);
        outputHintTextView = (TextView)findViewById(R.id.hintTextView); 
        inputHintTextView = (TextView)findViewById(R.id.hintTextView1); 
        outputButton = (Button)findViewById(R.id.outputButton);
        inputButton = (Button)findViewById(R.id.inputButton);
        outputPathTextView.setBackgroundColor(this.getResources().getColor(R.color.LemonChiffon));
        inputPathTextView.setBackgroundColor(this.getResources().getColor(R.color.LemonChiffon));
        outputHintTextView.setText("");
        inputHintTextView.setText("");
        outputChoosePathButton.setOnClickListener(this); 
        inputChoosePathButton.setOnClickListener(this); 
        outputButton.setOnClickListener(this);
        inputButton.setOnClickListener(this);
    }  
      
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if(FILE_RESULT_CODE == requestCode){  
            Bundle bundle = null;  
            if(data!=null&&(bundle=data.getExtras())!=null){
            	if(isTab1==1){
            	outputPath = bundle.getString("file");
                outputPathTextView.setText("ѡ���ļ���Ϊ��"+bundle.getString("file"));             
            	}else{
            		inputPath = bundle.getString("file");
                    inputPathTextView.setText("ѡ���ļ�Ϊ��"+bundle.getString("file"));
            	} 
            }
        }  
}
    private void outputContact(){
    	if(outputPath.equals("/"))
    	{
    		Log.i("gj", "//////");
    		outputHintTextView.setText("��ѡ������·������Ŀ¼�²��ܵ�����");
    		//Toast.makeText(this, "��ѡ������·������Ŀ¼�²��ܵ�����", Toast.LENGTH_LONG);
    	}
    	else{
    		File file = new File(outputPath+"/ͨѶ¼����.txt");
    		if(file.exists()){
    			createDialog(this, ContactContant.WARNDIALOG_TITLE,
    					ContactContant.OUTPUT_WARNDIALOG_MESSAGE, true,
    					ContactContant.DIALOG_TYPE_OUTPUT);
    		}else {
    			doOutputContact();
    		}
		}
	}
    public void createDialog(Context context, String title, String message,
			boolean hasCancel, final int type) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(ContactContant.DIALOG_OK,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						switch (type) {
							case ContactContant.DIALOG_TYPE_HELP:
								dialog.cancel();
								break;
							case ContactContant.DIALOG_TYPE_INSERT:
								doInsertContact();
								break;
							case ContactContant.DIALOG_TYPE_OUTPUT:
								doOutputContact();
								break;
						}
					}
				});
		if (hasCancel) {
			builder.setNeutralButton(ContactContant.DIALOG_CANCEL,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
		}
		builder.show();
	}
    private void doOutputContact(){
    	disableButtons();
		outputHintTextView.setText(ContactContant.STATUS_OUTPUTING);
		if (mOutputThread != null) {
			mOutputThread.interrupt();
			mOutputThread = null;
		}
		mOutputThread = new Thread(new OutputRunnable(this));
		if (mOutputThread != null) {
			mOutputThread.start();
		}
	}
    private void disableButtons(){
    	outputButton.setEnabled(false);
    	outputChoosePathButton.setEnabled(false);
    	inputButton.setEnabled(false);
    	inputChoosePathButton.setEnabled(false);
    }
    private void enableButtons(){
    	outputButton.setEnabled(true);
    	outputChoosePathButton.setEnabled(true);
    	inputButton.setEnabled(true);
    	inputChoosePathButton.setEnabled(true);
    }

	class OutputRunnable implements Runnable {
		private Context mContext;

		public OutputRunnable(Context context) {
			mContext = context;
		}

		@Override
		public void run() {
			boolean result = ContactToolOutputUtils.outputContacts(mContext,outputPath);
			if (result) {
				mHandler.sendEmptyMessage(ContactContant.OUTPUT_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(ContactContant.OUTPUT_FAIL);
			}
		}
	}
	private void endOutputContact(){
		isTab1 = 0;
	enableButtons();
	}
	private void endInsertContact(){	
		enableButtons();
	}
	private void doInsertContact() {
	   disableButtons();
		outputHintTextView.setText(ContactContant.STATUS_INSERTING);
		if (mInsertThread != null) {
			mInsertThread.start();
		}
	}
	private void insertContact() {
		//String path = mEditText.getText().toString();
		if (inputPath == null || inputPath.equals(ContactContant.NO_TEXT)) {
			Toast.makeText(this, ContactContant.FAIL_EDITTEXT_NOT_INPUT, Toast.LENGTH_SHORT).show();
			inputHintTextView.setText(ContactContant.FAIL_EDITTEXT_NOT_INPUT);
			return;
		}
		if (!new File(inputPath).exists()) {			
			Toast.makeText(this, ContactContant.FAIL_FIRE_NOT_EXIST, Toast.LENGTH_SHORT).show();
			inputHintTextView.setText(ContactContant.FAIL_FIRE_NOT_EXIST);
			return;
		}
		if (mInsertThread != null) {
			mInsertThread.interrupt();
			mInsertThread = null;
		}
		String charset = ContactContant.CHARSET_GBK;				
		mInsertThread = new Thread(new InsertRunnable(this, inputPath, charset));
		createDialog(this, ContactContant.WARNDIALOG_TITLE,
				ContactContant.INSERT_WARNDIALOG_MESSAGE, true,
				ContactContant.DIALOG_TYPE_INSERT);
	}


	class InsertRunnable implements Runnable {
		private Context mContext;
		private String mPath;
		private String mCharset;

		public InsertRunnable(Context context, String path, String charset) {
			mPath = path;
			mContext = context;
			mCharset = charset;
		}

		@Override
		public void run() {
			boolean result = ContactToolInsertUtils.insertIntoContact(mContext,
					mPath, mCharset);
			if (result) {
				mHandler.sendEmptyMessage(ContactContant.INSERT_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(ContactContant.INSERT_FAIL);
			}
		}
	}
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case(R.id.choosePathButton):
			outputChoosePathButtonClicked();
			break;
		case(R.id.choosePathButton1):
			inputChoosePathButtonClicked();
			break;
		case(R.id.inputButton):
			inputButtonClicked();
			break;
		case(R.id.outputButton):
			outputButtonClicked();
			break;
		}
		
	}
	private void inputButtonClicked(){
  	  insertContact();
    }
    private void outputButtonClicked(){
  	  outputContact();
    }
    private void inputChoosePathButtonClicked(){
  	  
        Intent intent = new Intent(ContactSynTxtActivity.this,DirManagerActivity.class);  
        Bundle data = new Bundle();
        data.putSerializable("isOutput", "0");
        intent.putExtras(data);
        startActivityForResult(intent, FILE_RESULT_CODE); 
    }
    private void outputChoosePathButtonClicked(){
  	  isTab1 = 1;
        Intent intent = new Intent(ContactSynTxtActivity.this,DirManagerActivity.class);  
        Bundle data = new Bundle();
        data.putSerializable("isOutput", "1");
        intent.putExtras(data);
        startActivityForResult(intent, FILE_RESULT_CODE);
    }
  }
