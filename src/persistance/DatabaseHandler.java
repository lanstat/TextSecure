package persistance;

import java.util.ArrayList;

import dev.sugarscope.textsecureclient.settings.MessageItem;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHandler {
	private Database database;
	private SQLiteDatabase db;
	
	public DatabaseHandler(Context context){
		database = new Database(context);
		db = database.getWritableDatabase();
	}
	
	public void putSetting(String key, String value){
		db.execSQL("INSERT INTO "+Settings.NAME+" VALUES ('"+key+"', '"+value+"');");
	}
	
	public String getSetting(String key){
		Cursor cursor = db.query(Settings.NAME, new String[]{Settings.VALUE}, "key='"+key+"'", null, "", "", "");
		if(cursor.moveToFirst()){
			return cursor.getString(0);
		}
		return null;
	}
	
	public void close(){
		db.close();
		database.close();
	}
	
	public void saveMessage(MessageItem item){
		db.execSQL("INSERT INTO "+Message.NAME+"("+Message.PHONE+", "+Message.CONTENT+") VALUES ('"+item.phone+"', '"+item.content+"');");
	}
	
	public ArrayList<MessageItem> getLastestConversation(String contact){
		ArrayList<MessageItem> items = new ArrayList<MessageItem>();
		Cursor cursor = db.query(Message.NAME, new String[]{Message.PHONE, Message.CONTENT}, "phone ='"+contact+"'", null, "", "", "id");
		if(cursor.moveToFirst()){
			do{
				items.add(cursor2Message(cursor));
			}while(cursor.moveToNext());
		}
		
		return items;
	}
	
	public ArrayList<MessageItem> getConversations(){
		ArrayList<MessageItem> items = new ArrayList<MessageItem>();
		Cursor cursor = db.rawQuery("SELECT distinct phone, content FROM "+Message.NAME+";", null);
		if(cursor.moveToFirst()){
			do{
				items.add(cursor2Message(cursor));
			}while(cursor.moveToNext());
		}
		
		return items;
	} 
	
	private MessageItem cursor2Message(Cursor cursor){
		MessageItem item = new MessageItem();
		item.phone = cursor.getString(0);
		item.content = cursor.getString(1);
		
		return item;
	}

}