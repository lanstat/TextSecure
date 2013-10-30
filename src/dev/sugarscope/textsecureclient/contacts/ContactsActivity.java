package dev.sugarscope.textsecureclient.contacts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.messages.ChatActivity;
import dev.sugarscope.textsecureclient.persistance.models.Contact;

public class ContactsActivity extends Activity implements OnItemClickListener{
	
	private ArrayList<Contact> mContacts;
	private ArrayAdapter<Contact> mAdapter;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		mList = (ListView)findViewById(R.id.listView1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mContacts = new ArrayList<Contact>();
		getFirstTimeContacts();
		mAdapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, mContacts);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	private void getFirstTimeContacts(){
		ContentResolver cr = getContentResolver();
	    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
	    String name = null, id =null;
	    String Phone1 = "";
	    if (cur.getCount() > 0) {
	    	while (cur.moveToNext()){
	    		name = "";
	    		Phone1 = "";
	    		id = cur.getString(cur
	                    .getColumnIndex(ContactsContract.Contacts._ID));
	    		name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	    		if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
	    			Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", 
	    					new String[] { id },null);
	    			Phone1 = "";
	    			while (pCur.moveToNext()){
	    				final String phonetype = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
	    				final String MainNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	    				if (phonetype.equalsIgnoreCase("1")) {
	    					Phone1 = MainNumber;
	    				}
	    			}
	    			pCur.close();
	    		}
	    		if(!name.equals("") && !Phone1.equals("")){
	    			mContacts.add(new Contact(name,Phone1));
	    		}
	    	}
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("phone", mContacts.get(id).phone);
		startActivity(intent);
	}
		
}
