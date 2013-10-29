package persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private static final int VERSION = 2;
	private static final String NAME = "secure";

	public Database(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Settings.OnCreate(db);
		Message.OnCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Settings.OnUpdate(db);
		Message.OnUpdate(db);
	}

}
