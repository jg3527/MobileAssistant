package com.example.textpart.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class DataService {
	 static Uri uri = Uri.parse("content://sms/");
	//tread_id 是1；address是2；person是3；body是12
		//获取所有的tread_id
	public  static List<String> getAllTread_id(Context context){
		ContentResolver contentResolver =context.getContentResolver();
		Cursor c = contentResolver.query(uri, null, null, null, null);
		List<String> allTread_id = new ArrayList<String>();
		c.moveToFirst();
		if(c.getCount()>0)
		{			
			do{	
				if(allTread_id.contains(c.getString(1))==false)
					allTread_id.add(c.getString(1));
			//Log.i("gj", c.getString(1));
			}while(c.moveToNext());
		}
		else Log.i("gj", "没有短信");
		c.close();
		//Log.i("gj", allTread_id.toString());
		return allTread_id;
	}
	//获取整个大的list
		public  static List<HashMap <String,Object>> getTheList(Context context){
			List<HashMap <String,Object>> list = new ArrayList<HashMap <String,Object>>();			
			List<String> allTread_id = new ArrayList<String>();			
			ContentResolver contentResolver = context.getContentResolver();
			allTread_id=getAllTread_id(context);//allTread_id.addAll(this.getAllTread_idlist(context));
			String tempThread_id = new String();
			HashMap <String,Object> map = null;
			for(int i = 0; i<allTread_id.size();i++){
				map = new HashMap <String,Object>();
				List<HashMap<String,Object>> listTemp = new ArrayList<HashMap<String,Object>>();
				tempThread_id = allTread_id.get(i);
				Cursor c = contentResolver.query(uri, null, "thread_id"+ "=?" , new String[]{tempThread_id}, null);
				c.moveToFirst();		
				HashMap<String,Object> mapTemp = null;
				//tread_id 是1；address是2；person是3；body是12
				do{
					mapTemp = new HashMap<String,Object>();
					mapTemp.put("_id", c.getString(0));				
					mapTemp.put("Receive", c.getString(9));
					mapTemp.put("body", c.getString(12));
					listTemp.add(mapTemp);
				}while(c.moveToNext());
				c.moveToLast();
				if(readNameByPhoneNumber(context,c.getString(2))==null)
					map.put("addressOrPerson", c.getString(2));
				else 
					map.put("addressOrPerson", readNameByPhoneNumber(context,c.getString(2)));//Person没有，则显示address		
				c.moveToFirst();
				map.put("lastConversation",  c.getString(12));
				map.put("thread_id", tempThread_id);
				map.put("conversation", listTemp);	
				list.add(map);
				c.close();
			}			
			return list;		
		}
		//获取与某联系人的对 话
		public static Cursor getTheSpecificMessageContext(Context context,String thread_id){
			ContentResolver contentResolver = context.getContentResolver();
			Cursor c = contentResolver.query(uri, null, "thread_id"+ "=?" , new String[]{thread_id}, null);
			return c;
		}
//		private static Cursor getTheSpecificMessageContextByNumber(Context context,String address){
//			ContentResolver contentResolver = context.getContentResolver();
//			Cursor c = contentResolver.query(uri, null, "address"+ "=?" , new String[]{address}, null);
//			return c;
//		}
		public static List<Map<String,String>> getTheSpecificMessageContextList(Context context,String thread_id){
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			Cursor c = getTheSpecificMessageContext(context,thread_id);
			c.moveToLast();
			Map<String,String> map = null;
			for(int i=0;i<c.getCount();i++){
				map = new HashMap<String,String>();
				map.put("Receive", c.getString(9));
				map.put("body", c.getString(12));
				map.put("_id", c.getString(0));
				list.add(map);
				c.moveToPrevious();				
			}
			c.close();
			return list;
		}

		 public static String readNameByPhoneNumber(Context context,String phoneNumber){  			 
			 Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+phoneNumber);      
		        ContentResolver resolver = context.getContentResolver();  
		        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null); //从raw_contact表中返回display_name  		        		        
		        String result;
		        if(cursor.moveToFirst())
		        { 
		        	result = cursor.getString(0);
		        }else
		        	result = null;
		        cursor.close();
		        return result;
		    } 
		 
		 public static String readphoneNumberByFuzzName(Context context,String key){  			 
			StringBuilder sb = new StringBuilder();
			ContentResolver cr = context.getContentResolver();
			String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
					    ContactsContract.CommonDataKinds.Phone.NUMBER};
			Cursor cursor = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					projection, 
					ContactsContract.Contacts.DISPLAY_NAME + " like " + "'%" + key + "%'", 
					null, null);
			while(cursor.moveToNext()){
//				String name = cursor.getString(
//						cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String number = cursor.getString(
						cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				sb.append(number);//sb.append(name + " (").append(number + ")").append("\r\n");
			}
			cursor.close();
				return sb.toString();		        
		    } 
		 //根据特定的内容获取对话
			public  static List<HashMap <String,Object>> getTheListBySpecificContent(Context context, String query){
				List<HashMap <String,Object>> list = new ArrayList<HashMap <String,Object>>();			
				List<String> allTread_id = new ArrayList<String>();			
				ContentResolver contentResolver = context.getContentResolver();
				allTread_id=getAllTread_id(context);
				String tempThread_id = new String();
				HashMap <String,Object> map = null;
				for(int i = 0; i<allTread_id.size();i++){
					map = new HashMap <String,Object>();
					List<HashMap<String,String>> listTemp = new ArrayList<HashMap<String,String>>();
					tempThread_id = allTread_id.get(i);
					Cursor c = contentResolver.query(uri, null, "thread_id"+ "=?" , new String[]{tempThread_id}, null);
					c.moveToFirst();		
					HashMap<String,String> mapTemp = null;
					//tread_id 是1；address是2；person是3；body是12
					do{
						if(c.getString(12).toString().contains(query))
						{
						mapTemp = new HashMap<String,String>();
						mapTemp.put("_id", c.getString(0));				
						mapTemp.put("Receive", c.getString(9));
						mapTemp.put("body", c.getString(12));
						listTemp.add(mapTemp);
						Log.i("gj", "ListTemp+1");
						}
					}while(c.moveToNext());
					c.moveToLast();
					if(readNameByPhoneNumber(context,c.getString(2))==null)
						map.put("addressOrPerson", c.getString(2));
					else 
						map.put("addressOrPerson", readNameByPhoneNumber(context,c.getString(2)));//Person没有，则显示address							
					c.moveToFirst(); 
					if(listTemp.size()!=0){
					map.put("lastConversation",  listTemp.get(0).get("body"));
					map.put("thread_id", tempThread_id);
					map.put("conversation", listTemp);	
					list.add(map);
					}
					c.close();
				}			
				return list;		
			}
			private  static String getDate(long date){
				SimpleDateFormat dateFormat = new SimpleDateFormat(    
		                "yyyyMMdd");//注意，一定要把大小写搞对，HH和hh是不同的，    
		        Date d = new Date(date);    
		        return dateFormat.format(d);  //得到日期
			}		
			public  static List<HashMap <String,Object>> getTheListBySpecificTime(Context context, String query){
				List<HashMap <String,Object>> list = new ArrayList<HashMap <String,Object>>();			
				List<String> allTread_id = new ArrayList<String>();			
				ContentResolver contentResolver = context.getContentResolver();
				allTread_id=getAllTread_id(context);
				String tempThread_id = new String();
				HashMap <String,Object> map = null;
				for(int i = 0; i<allTread_id.size();i++){
					map = new HashMap <String,Object>();
					List<HashMap<String,Object>> listTemp = new ArrayList<HashMap<String,Object>>();
					tempThread_id = allTread_id.get(i);
					Cursor c = contentResolver.query(uri, null, "thread_id"+ "=?" , new String[]{tempThread_id}, null);
					c.moveToFirst();		
					HashMap<String,Object> mapTemp = null;
					//tread_id 是1；address是2；person是3；date是4；body是12
					do{
						if(getDate(c.getLong(4)).equals(query))
						{
						mapTemp = new HashMap<String,Object>();
						mapTemp.put("_id", c.getString(0));				
						mapTemp.put("Receive", c.getString(9));
						mapTemp.put("body", c.getString(12));
						listTemp.add(mapTemp);
						}
					}while(c.moveToNext());
					c.moveToLast();
					if(readNameByPhoneNumber(context,c.getString(2))==null)
						map.put("addressOrPerson", c.getString(2));
					else 
						map.put("addressOrPerson", readNameByPhoneNumber(context,c.getString(2)));//Person没有，则显示address							
					c.moveToFirst();
					if(listTemp.size()!=0){
					map.put("lastConversation",  listTemp.get(0).get("body"));
					map.put("thread_id", tempThread_id);
					map.put("conversation", listTemp);	
					list.add(map);
					}
					c.close();
				}			
				return list;		
			}
			public static void deleteMessageDialogByTread_Id(Context context,String thread_id){
				context.getContentResolver().delete(Uri.parse("content://sms"), "thread_id=?", new String[]{thread_id});				
			}
			public static void deleteMessageDialogBy_Id(Context context,String _id){
				context.getContentResolver().delete(Uri.parse("content://sms"), "_id=?", new String[]{_id});				
			}
			public static boolean isUnreadMessageExist(Context context) {  
		        // Sets the sms inbox's URI   
		        Cursor c = context.getContentResolver().query(uri, null,  
		                        "read = 0", null, null);  
		        // Checks the number of unread messages in the inbox  
		        if (c.getCount() == 0) {  
		        	c.close();
		                return false;  
		        } else  {
		        	c.close();
		        	return true; 		        	
		        }		                 
			} 
			public static HashSet<String> unReadMessageThread_id(Context context){
				 Cursor c = context.getContentResolver().query(uri, null,  
	                        "read = 0", null, null);
				 HashSet<String> set = new HashSet<String>();
				 if(c.getCount()==0){
					 Log.i("gj", "no unrad data service");
					 return null;
				 }else{
				 c.moveToFirst();
				do{				
					set.add(c.getString(1));//获取未读短信的thread_id
				}while(c.moveToNext());
				 c.close();
				 return set;
				 }
			}
			public static HashSet<String> unReadMessage_id(Context context,String thread_id){
				Log.i("gj", "2@"+thread_id);
				Cursor c = context.getContentResolver().query(uri, null,  
	                        "read=? and thread_id=?", new String[]{"0",thread_id}, null);
				 HashSet<String> set = new HashSet<String>();
				 if(c.getCount()==0){
					 return null;
				 }else{
				 c.moveToFirst();
				do{	
					Log.i("gj", "set:"+c.getString(0));
					set.add(c.getString(0));//获取未读短信的_id
				}while(c.moveToNext());
				 c.close();
				 return set;
				 }
			}
			public static int unReadMessage_idNum(Context context,String thread_id){				
				Cursor c = context.getContentResolver().query(uri, null,  
	                        "read=? and thread_id=?", new String[]{"0",thread_id}, null);
				 
				 int num = c.getCount();
				 c.close();
				 Log.i("gj", "num: "+num);
				 return num;
				 
			}
			private static String transPhoneNumberToThread_id(Context context,String phoneNumber){
				Cursor c = context.getContentResolver().query(uri, null,  
                        "address=?", new String[]{phoneNumber}, null);
				c.moveToFirst();
				String thread_id = c.getString(1);
				c.close();
				return thread_id;				
			}
			public static List<Map<String,String>> getTheSpecificMessageContextListByNumber(Context context,String number){
				List<Map<String,String>> list = new ArrayList<Map<String,String>>();
				String thread_id = transPhoneNumberToThread_id(context,number);
				list = getTheSpecificMessageContextList(context,thread_id);
				return list;
			}
			
}
