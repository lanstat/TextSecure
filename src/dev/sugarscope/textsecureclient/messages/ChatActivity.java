package dev.sugarscope.textsecureclient.messages;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.BaseActivity;
import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.controllers.ControlMensaje;
import dev.sugarscope.textsecureclient.messages.adapters.ChatAdapter;
import dev.sugarscope.textsecureclient.persistance.models.Message;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.transport.Packet;
import dev.sugarscope.transport.Tag;

@SuppressLint("HandlerLeak")
public class ChatActivity extends BaseActivity {
	
	ArrayList<Message> mMessages;
	ListView mList;
	EditText mContent;
	ControlMensaje mController;
	String mPhone;
	ChatAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mList = (ListView) findViewById(R.id.listView1);
		mContent = (EditText) findViewById(R.id.edtContent);
		mController = new ControlMensaje();
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(android.os.Message msg) {
				final Packet packet = (Packet)msg.obj;
				switch (packet.getTag()) {
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
		final byte[] image = (byte[])data[2];
		final Message item = new Message(phone, content, image, phone);
		mController.guardar(item);
		mMessages.add(item);
		mAdapter.notifyDataSetChanged();
		mList.setSelection(mList.getAdapter().getCount()-1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Client.getInstance().getReader().setHandler(mHandler);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mPhone = bundle.getString("phone");
			mMessages = mController.leer(mPhone);
			mAdapter = new ChatAdapter(this, mMessages);
			registerForContextMenu(mList);
			mList.setAdapter(mAdapter);
		}
	}
	
	public void clickMe(View v){
		final String content = mContent.getText().toString();
		mController.nuevo(mPhone, content);
		mMessages.add(new Message("Yo", content));
		mAdapter.notifyDataSetChanged();
		mList.setSelection(mList.getAdapter().getCount()-1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		final Message item = mMessages.get(info.position);
		menu.add(Menu.NONE, item.id, item.id, "Borrar");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final int menuItemIndex = item.getItemId();
		Log.i(Settings.TAG, ""+menuItemIndex);
		mController.eliminar(menuItemIndex);
		
		return true;
	}

}
