package persistance;

import android.database.sqlite.SQLiteDatabase;

public class Settings {

	public static final String KEY = "key";
	public static final String VALUE = "value";
	public static final String NAME = "settings";
	
	private static final String OnCreate = "CREATE TABLE "+NAME+"("+KEY+" text, "+VALUE+" text);";
	
	public static void OnCreate(SQLiteDatabase db){
		db.execSQL(OnCreate);
	}
	
	public static void OnUpdate(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS settings;");
		db.execSQL(OnCreate);
	}
}
