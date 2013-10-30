package dev.sugarscope.textsecureclient.persistance;

import android.database.sqlite.SQLiteDatabase;

public class ContactTable {
	
	public static final String TABLE_NAME = "contact";
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PHONE = "phone";
	
	private static final String OnCreate = "CREATE TABLE "+NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+" text, "+PHONE+" text);";
	
	public static void OnCreate(SQLiteDatabase db){
		db.execSQL(OnCreate);
	}
	
	public static void OnUpdate(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");
		db.execSQL(OnCreate);
	}
	
	
}
