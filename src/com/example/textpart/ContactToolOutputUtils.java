package com.example.textpart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class ContactToolOutputUtils  {
	private static final String TAG = "ContactOutputTool";
	private static int mCount = 0;
	

	public static boolean outputContacts(Context context,String outputPath) {
		init();
		try {
			String result = getFromContactDatabase(context);
			Log.i("gj", "write file");
			Log.i("gj", "output Path: "+outputPath);			
			writeFile(outputPath+"/通讯录导出.txt", result);
		} catch (Exception e) {
			Log.i("gj", "Error in outputContacts " + e.getMessage());
			Log.e(TAG, "Error in outputContacts " + e.getMessage());
			return false;
		}
		return true;
	}

	private static void init() {
		mCount = 0;
	}

	private static String getFromContactDatabase(Context context) {
		StringBuilder resultBuilder = new StringBuilder();
		Cursor cursor = context
				.getContentResolver()
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] { StructuredName.DISPLAY_NAME,
								Data.RAW_CONTACT_ID }, Data.MIMETYPE + "= ?",
						new String[] { StructuredName.CONTENT_ITEM_TYPE }, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// get display name and row id
			String displayName = cursor.getString(0);
			int id = cursor.getInt(1);
			
			// get phone num
			Cursor mobileCursor = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI,
					new String[] { Phone.NUMBER },
					Data.RAW_CONTACT_ID + " = " + id + " AND " + Data.DATA2
							+ " = " + ContactContant.MOBILE_ID, null, null);
			String mobileNum = ContactContant.NO_TEXT;
			mobileCursor.moveToFirst();
			if (!mobileCursor.isAfterLast()) {
				mobileNum = mobileCursor.getString(0);
			}
			mobileCursor.close();

			// get home num
			Cursor homeCursor = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI,
					new String[] { Phone.NUMBER },
					Data.RAW_CONTACT_ID + " = " + id + " AND " + Data.DATA2
							+ " = " + ContactContant.HOME_ID, null, null);
			String homeNum = ContactContant.NO_TEXT;
			homeCursor.moveToFirst();
			if (!homeCursor.isAfterLast()) {
				homeNum = homeCursor.getString(0);
			}
			homeCursor.close();

			if (displayName != null && (!displayName.equals(ContactContant.NO_TEXT) || 
					!displayName.equals(ContactContant.NULL_TEXT))) {
				String result = displayName + ContactContant.SPACE_STRING_4;
				if(mobileNum.equals(ContactContant.NO_TEXT)){
					result += ContactContant.NO_MOBILE_NUM;
				}
				else {
					result += mobileNum;
				}
				result += ContactContant.SPACE_STRING_8 + homeNum;
				result += ContactContant.ENTER_CHAR_LINUX;
				String checkString = resultBuilder.toString();
				if(!checkString.contains(result) && (mobileNum.equals(ContactContant.NO_TEXT) ||
						!checkString.contains(mobileNum))){
					resultBuilder.append(result);
					mCount++;
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		return resultBuilder.toString();
	}

	private static void writeFile(String path, String buffer) {
		try {
			File file = new File(path);
			FileWriter writer = new FileWriter(file, false);
			writer.write(buffer);
			writer.close();
		} catch (IOException e) {
			Log.i("gj", "writeFile exception"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static int getCount(){
		return mCount;
	}
}
