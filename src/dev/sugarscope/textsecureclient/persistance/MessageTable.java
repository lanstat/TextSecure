package dev.sugarscope.textsecureclient.persistance;

import android.database.sqlite.SQLiteDatabase;

public class MessageTable {
	
	public static final String CONTENT = "content";
	public static final String PHONE = "phone";
	public static final String NAME = "message";
	public static final String ID = "id";
	
	private static final String OnCreate = "CREATE TABLE "+NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+PHONE+" text, "+CONTENT+" text);";
	
	public static void OnCreate(SQLiteDatabase db){
		db.execSQL(OnCreate);
	}
	
	public static void OnUpdate(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS "+NAME+";");
		db.execSQL(OnCreate);
	}

}
