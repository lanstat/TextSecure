package dev.sugarscope.textsecureclient.messages.adapters;

import java.util.ArrayList;

import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.persistance.models.Message;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter{
	private ArrayList<Message> mConversations;
	private LayoutInflater inflater;

	public ChatAdapter(Context context, ArrayList<Message> conversations){
		mConversations = conversations;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mConversations.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mConversations.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convert, ViewGroup parent) {
		final Message item = mConversations.get(pos);
		View v = null;
		if(item != null){
			if(item.phone.equals("Yo"))
				v = inflater.inflate(R.layout.chat_adapter_me, null);
			else
				v = inflater.inflate(R.layout.chat_adapter_outer, null);
			((TextView)v.findViewById(R.id.phone)).setText(item.phone);
			((TextView)v.findViewById(R.id.message)).setText(item.content);
			if(item.image != null)
				((ImageView)v.findViewById(R.id.image)).setImageBitmap(BitmapFactory.decodeByteArray( item.image, 0, item.image.length));
		}
		return v;
	}

}
