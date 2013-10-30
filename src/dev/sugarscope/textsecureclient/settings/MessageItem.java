package dev.sugarscope.textsecureclient.settings;

public class MessageItem {
	public String phone;
	public String content;
	public int id;
	
	public MessageItem(){}
	
	public MessageItem(String phone, String content){
		this(0, phone, content);
	}
	
	public MessageItem(int id, String phone, String content){
		this.id = id;
		this.phone = phone;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return phone+" - "+content;
	}
	
	
}
