package dev.sugarscope.textsecureclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import persistance.DatabaseHandler;
import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.contacts.ContactsActivity;
import dev.sugarscope.textsecureclient.controls.ControlMensaje;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	ControlMensaje mController;
	ArrayAdapter<MessageItem> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		mList = (ListView)findViewById(R.id.listView1);
		mController = new ControlMensaje();
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
				case Tag.OBTAIN_SAVED_MESSAGE:
					convertPacketToMessages(packet.getData());
					break;
				case Tag.SEND_MESSAGE:
					addMessage(packet.getData());
					break;
				}
			}
			
		};
	}
	
	private void addMessage(Object[] data){
		final String phone= data[0].toString();
		final String content = data[1].toString();
		final MessageItem item = new MessageItem(phone, content);
		mController.guardar(item);
		final DatabaseHandler handler = DatabaseHandler.getInstance();
		mContacts = handler.getConversations();
		mAdapter.notifyDataSetChanged();
		showMessage("Nuevo mensaje recibido");
	}
	
	private void convertPacketToMessages(Object[] data){
		final DatabaseHandler handler = DatabaseHandler.getInstance();
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
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		final DatabaseHandler handler = DatabaseHandler.getInstance();
		mContacts = handler.getConversations();
		mAdapter = new ArrayAdapter<MessageItem>(this, android.R.layout.simple_list_item_1, mContacts);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
		try {
			Client.getInstance().connect(Settings.SERVER_HOST, Settings.SERVER_PORT);
			Client.getInstance().getReader().setHandler(mHandler);
			
			Packet packet = new Packet(Tag.LOGIN);
			packet.setData(Settings.phone);
			Client.getInstance().sendPackage(packet);
		} catch (Exception e) {
			showMessage("No se logro realizar un conexion con el servidor.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.contacts:
			Intent intent = new Intent(this, ContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.closeSession:
			try {
				DatabaseHandler.getInstance().close();
				Client.getInstance().close();
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("phone", mContacts.get(id).phone);
		startActivity(intent);
	}

}
