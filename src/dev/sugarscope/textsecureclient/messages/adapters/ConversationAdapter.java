package dev.sugarscope.textsecureclient.messages.adapters;

import java.util.ArrayList;

import dev.sugarscope.textsecureclient.R;
import dev.sugarscope.textsecureclient.persistance.models.Message;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConversationAdapter extends BaseAdapter{
	private ArrayList<Message> mConversations;
	private LayoutInflater inflater;

	public ConversationAdapter(Context context, ArrayList<Message> conversations){
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
		View v = inflater.inflate(R.layout.conversation_adapter, null);
		final Message item = mConversations.get(pos);
		if(item != null){
			((TextView)v.findViewById(R.id.phone)).setText(item.phone);
			((TextView)v.findViewById(R.id.message)).setText(item.content);
		}
		return v;
	}

}
