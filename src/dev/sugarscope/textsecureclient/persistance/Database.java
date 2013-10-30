package dev.sugarscope.textsecureclient.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private static final int VERSION = 3;
	private static final String NAME = "secure";

	public Database(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		SettingsTable.OnCreate(db);
		MessageTable.OnCreate(db);
		ContactTable.OnCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		SettingsTable.OnUpdate(db);
		MessageTable.OnUpdate(db);
		ContactTable.OnUpdate(db);
	}

}
