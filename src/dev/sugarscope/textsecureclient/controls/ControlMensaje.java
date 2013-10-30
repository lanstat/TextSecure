package dev.sugarscope.textsecureclient.controls;

import java.util.ArrayList;

import persistance.DatabaseHandler;
import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.settings.MessageItem;
import dev.sugarscope.textsecureclient.settings.Tag;
import dev.sugarscope.transport.Packet;

public class ControlMensaje {
	
	public void nuevo(String phone, String content){
		Packet packet = new Packet(Tag.SEND_MESSAGE);
		packet.setData(new String[]{phone}, content);
		Client.getInstance().sendPackage(packet);
		DatabaseHandler.getInstance().saveMessage(new MessageItem(phone, content));
	}
	
	public ArrayList<MessageItem> leer(String phone){
		final DatabaseHandler handler = DatabaseHandler.getInstance();
		return  handler.getLastestConversation(phone);
	}
	
	public void guardar(MessageItem item){
		DatabaseHandler.getInstance().saveMessage(item);
	}
	
	public void eliminar(int id){
		
	}
	
}
