package dev.sugarscope.textsecureclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import persistance.DatabaseHandler;
import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.settings.MessageItem;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.textsecureclient.settings.Tag;
import dev.sugarscope.transport.Packet;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("HandlerLeak")
public class InboxActivity extends BaseActivity implements OnItemClickListener{
	ListView mList;
	ArrayList<MessageItem> mContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		mList = (ListView)findViewById(R.id.listView1);
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
				case Tag.OBTAIN_SAVED_MESSAGE:
					convertPacketToMessages(packet.getData());
					break;
				}
			}
			
		};
	}
	
	private void convertPacketToMessages(Object[] data){
		DatabaseHandler handler = new DatabaseHandler(this);
		ArrayList<String> phones = new ArrayList<String>();
		for(Object obj : data){
			String[] message = (String[])obj;
			final MessageItem item = new MessageItem(message[0], message[1]);
			handler.saveMessage(item);
			if(!phones.contains(item.phone)){
				phones.add(item.phone);
				mContacts.add(0, item);
			}
		}
		handler.close();
		ArrayAdapter<MessageItem> adapter = new ArrayAdapter<MessageItem>(this, android.R.layout.simple_list_item_1, mContacts);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Client.getInstance().connect(Settings.SERVER_HOST, Settings.SERVER_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Client.getInstance().getReader().setHandler(mHandler);
		
		DatabaseHandler handler = new DatabaseHandler(this);
		mContacts = handler.getConversations();
		handler.close();
		
		Packet packet = new Packet(Tag.LOGIN);
		packet.setData(Settings.phone);
		Client.getInstance().sendPackage(packet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("phone", mContacts.get(id).phone);
		startActivity(intent);
	}

}
