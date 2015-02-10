package com.example.textpart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TextSpeechInActivity extends Activity {
	EditText speechTextBox;
	Button start,stop;
	 public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.speech_text_in_fragment);
	 }
	 public void onStart()
	 {
		 
	 }
}
