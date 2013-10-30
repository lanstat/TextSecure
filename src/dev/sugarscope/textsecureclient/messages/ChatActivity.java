package dev.sugarscope.textsecureclient.messages;

import java.util.ArrayList;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.BaseActivity;
import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.controllers.ControlMensaje;
import dev.sugarscope.textsecureclient.persistance.models.Message;
import dev.sugarscope.transport.Packet;
import dev.sugarscope.transport.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class ChatActivity extends BaseActivity {
	
	ArrayList<Message> mMessages;
	ListView mList;
	EditText mContent;
	ControlMensaje mController;
	String mPhone;
	ArrayAdapter<Message> mAdapter;

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
		final Message item = new Message(phone, content);
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
			mAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, mMessages);
			mList.setAdapter(mAdapter);
		}
	}
	
	public void clickMe(View v){
		final String content = mContent.getText().toString();
		mController.nuevo(mPhone, content);
		mMessages.add(new Message("Yo", content));
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

}
