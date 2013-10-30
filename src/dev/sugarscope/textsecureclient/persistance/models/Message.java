package dev.sugarscope.textsecureclient.persistance.models;

public class Message {
	public String phone;
	public String content;
	public int id;
	
	public Message(){}
	
	public Message(String phone, String content){
		this(0, phone, content);
	}
	
	public Message(int id, String phone, String content){
		this.id = id;
		this.phone = phone;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return phone+" - "+content;
	}
	
	
}
