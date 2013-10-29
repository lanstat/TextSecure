package dev.sugarscope.textsecureclient;

import java.util.ArrayList;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.settings.MessageItem;
import dev.sugarscope.transport.Packet;
import persistance.DatabaseHandler;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChatActivity extends BaseActivity {
	
	ArrayList<MessageItem> mMessages;
	ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mList = (ListView) findViewById(R.id.listView1);
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
				default:
					break;
				}
			}
			
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		Client.getInstance().getReader().setHandler(mHandler);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			String phone = bundle.getString("phone");
			DatabaseHandler handler = new DatabaseHandler(this);
			mMessages = handler.getLastestConversation(phone);
			ArrayAdapter<MessageItem> adapter = new ArrayAdapter<MessageItem>(this, android.R.layout.simple_list_item_1, mMessages);
			mList.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

}
