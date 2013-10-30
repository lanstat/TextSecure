package dev.sugarscope.textsecureclient;

import java.util.ArrayList;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.controls.ControlMensaje;
import dev.sugarscope.textsecureclient.settings.MessageItem;
import dev.sugarscope.textsecureclient.settings.Tag;
import dev.sugarscope.transport.Packet;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class ChatActivity extends BaseActivity {
	
	ArrayList<MessageItem> mMessages;
	ListView mList;
	EditText mContent;
	ControlMensaje mController;
	String mPhone;
	ArrayAdapter<MessageItem> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mList = (ListView) findViewById(R.id.listView1);
		mContent = (EditText) findViewById(R.id.edtContent);
		mController = new ControlMensaje();
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
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
		final MessageItem item = new MessageItem(phone, content);
		mController.guardar(item);
		mMessages.add(item);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Client.getInstance().getReader().setHandler(mHandler);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mPhone = bundle.getString("phone");
			mMessages = mController.leer(mPhone);
			mAdapter = new ArrayAdapter<MessageItem>(this, android.R.layout.simple_list_item_1, mMessages);
			mList.setAdapter(mAdapter);
		}
	}
	
	public void clickMe(View v){
		final String content = mContent.getText().toString();
		mController.nuevo(mPhone, content);
		mMessages.add(new MessageItem("Yo", content));
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

}
