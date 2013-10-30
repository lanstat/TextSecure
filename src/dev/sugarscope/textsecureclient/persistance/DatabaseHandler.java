package dev.sugarscope.textsecureclient.persistance;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import dev.sugarscope.textsecureclient.persistance.models.Message;
import dev.sugarscope.textsecureclient.settings.Settings;

public class DatabaseHandler {
	private Database database;
	private SQLiteDatabase db;
	private static DatabaseHandler instance;
	
	private DatabaseHandler(){}
	
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
		Log.i(Settings.TAG, item.toString());
		/*db.execSQL("INSERT INTO "+MessageTable.NAME+"("+MessageTable.PHONE+", "+MessageTable.CONTENT+", "+MessageTable.IMAGE+", "+MessageTable.GROUP+")"
				+ " VALUES ('"+item.phone+"', '"+item.content+"', "+item.image+", '"+item.group+"');");*/
		if(item.image !=null){
		String sql = "INSERT INTO "+MessageTable.NAME+"("+MessageTable.PHONE+", "+MessageTable.CONTENT+", "+MessageTable.IMAGE+", "+MessageTable.GROUP+")"
				+ " VALUES (?,?,?,?);";
	    SQLiteStatement insertStmt = db.compileStatement(sql);
	    insertStmt.clearBindings();
	    insertStmt.bindString(1, item.phone);
	    insertStmt.bindString(2, item.content);
	    insertStmt.bindBlob(3, item.image);
	    insertStmt.bindString(4, item.group);
	    insertStmt.executeInsert();
		}else{
			String sql = "INSERT INTO "+MessageTable.NAME+"("+MessageTable.PHONE+", "+MessageTable.CONTENT+", "+MessageTable.GROUP+")"
					+ " VALUES (?,?,?);";
		    SQLiteStatement insertStmt = db.compileStatement(sql);
		    insertStmt.clearBindings();
		    insertStmt.bindString(1, item.phone);
		    insertStmt.bindString(2, item.content);
		    insertStmt.bindString(3, item.group);
		    insertStmt.executeInsert();
		}
		
	}
	
	public ArrayList<Message> getLastestConversation(String contact){
		ArrayList<Message> items = new ArrayList<Message>();
		Cursor cursor = db.query(MessageTable.NAME, new String[]{MessageTable.PHONE, MessageTable.CONTENT, MessageTable.ID, MessageTable.IMAGE, MessageTable.GROUP}, "group_p ='"+contact+"'", null, "", "", "id");
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
		Cursor cursor = db.rawQuery("SELECT group_p, content FROM "+MessageTable.NAME+" GROUP BY group_p;", null);
		if(cursor.moveToFirst()){
			do{
				items.add(cursor2Message(cursor, false));
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		return items;
	} 
	
	public void deleteMessage(int id){
		db.delete(MessageTable.NAME, "id ="+id, null);
	}
	
	private Message cursor2Message(Cursor cursor, boolean id){
		Message item = new Message();
		item.phone = cursor.getString(0);
		item.content = cursor.getString(1);
		if(id){
			item.id = cursor.getInt(2);
			item.image = cursor.getBlob(3);
			item.group = cursor.getString(4);
		}
		
		return item;
	}

}
