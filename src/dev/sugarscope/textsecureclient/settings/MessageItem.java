package dev.sugarscope.textsecureclient.settings;

public class MessageItem {
	public String phone;
	public String content;
	
	public MessageItem(){}
	
	public MessageItem(String phone, String content){
		this.phone = phone;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return phone;
	}
	
	
}
