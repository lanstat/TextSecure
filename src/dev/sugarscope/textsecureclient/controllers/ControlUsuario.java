package dev.sugarscope.textsecureclient.controllers;

import dev.sugarscope.client.Client;
import dev.sugarscope.textsecureclient.persistance.DatabaseHandler;
import dev.sugarscope.transport.Packet;
import dev.sugarscope.transport.Tag;

public class ControlUsuario {
	
	public void verificarCodigoActivacion(String seed){
		Packet packet = new Packet(Tag.VERIFY_SEED);
		packet.setData(seed);
		Client.getInstance().sendPackage(packet);
	}
	
	public void guardar(String number){
		DatabaseHandler.getInstance().putSetting("phone", number);
	}
	
}
