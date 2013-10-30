package dev.sugarscope.textsecureclient.persistance;

import java.util.ArrayList;

import dev.sugarscope.textsecureclient.persistance.models.Message;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHandler {
	private Database database;
	private SQLiteDatabase db;
	private static DatabaseHandler instance;
	
	private DatabaseHandler(){
		
	}
	
	public void init(Context context){
		database = new Database(context);
		db = database.getWritableDatabase();
	}
	
	public static DatabaseHandler getInstance(){
		if(instance == null)
			instance = new DatabaseHandler();
		return instance;
	}
	
	public void putSetting(String key, String value){
		db.execSQL("INSERT INTO "+SettingsTable.NAME+" VALUES ('"+key+"', '"+value+"');");
	}
	
	public String getSetting(String key){
		Cursor cursor = db.query(SettingsTable.NAME, new String[]{SettingsTable.VALUE}, "key='"+key+"'", null, "", "", "");
		if(cursor.moveToFirst()){
			return cursor.getString(0);
		}
		return null;
	}
	
	public void close(){
		db.close();
		database.close();
	}
	
	public void saveMessage(Message item){
		db.execSQL("INSERT INTO "+MessageTable.NAME+"("+MessageTable.PHONE+", "+MessageTable.CONTENT+") VALUES ('"+item.phone+"', '"+item.content+"');");
	}
	
	public ArrayList<Message> getLastestConversation(String contact){
		ArrayList<Message> items = new ArrayList<Message>();
		Cursor cursor = db.query(MessageTable.NAME, new String[]{MessageTable.PHONE, MessageTable.CONTENT, MessageTable.ID}, "phone ='"+contact+"'", null, "", "", "id");
		if(cursor.moveToFirst()){
			do{
				items.add(cursor2Message(cursor, true));
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		return items;
	}
	
	public ArrayList<Message> getConversations(){
		ArrayList<Message> items = new ArrayList<Message>();
		Cursor cursor = db.rawQuery("SELECT phone, content FROM "+MessageTable.NAME+" GROUP BY phone;", null);
		if(cursor.moveToFirst()){
			do{
				items.add(cursor2Message(cursor, false));
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		return items;
	} 
	
	private Message cursor2Message(Cursor cursor, boolean id){
		Message item = new Message();
		item.phone = cursor.getString(0);
		item.content = cursor.getString(1);
		if(id)
			item.id = cursor.getInt(2);
		
		return item;
	}

}
