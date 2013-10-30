package dev.sugarscope.textsecureclient.controllers;

import java.util.ArrayList;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.persistance.DatabaseHandler;
import dev.sugarscope.textsecureclient.persistance.models.Message;
import dev.sugarscope.textsecureclient.settings.Settings;
import dev.sugarscope.transport.Packet;
import dev.sugarscope.transport.Tag;

public class ControlMensaje {
	
	public void nuevo(String phone, String content){
		Packet packet = new Packet(Tag.SEND_MESSAGE);
		packet.setData(new String[]{phone}, content);
		Client.getInstance().sendPackage(packet);
		DatabaseHandler.getInstance().saveMessage(new Message(phone, content));
	}
	
	public ArrayList<Message> leer(String phone){
		return  DatabaseHandler.getInstance().getLastestConversation(phone);
	}
	
	public void guardar(Message item){
		DatabaseHandler.getInstance().saveMessage(item);
	}
	
	public void iniciarSesion(){
		Packet packet = new Packet(Tag.LOGIN);
		packet.setData(Settings.phone);
		Client.getInstance().sendPackage(packet);
	}
	
	public ArrayList<Message> obtenerConversaciones(){
		return DatabaseHandler.getInstance().getConversations();
	}
	
	public void eliminar(int id){
		
	}
	
}
