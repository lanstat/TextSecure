package dev.sugarscope.textsecureclient.persistance.models;

public class Message {
	public String phone;
	public String content;
	public byte[] image;
	public String group;
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
	
	public Message(String phone, String content, byte[] image, String group) {
		super();
		this.phone = phone;
		this.content = content;
		this.image = image;
		this.group = group;
	}

	@Override
	public String toString() {
		if(image!=null){
			return phone+": "+content+" "+group+" imagen";
		}
		return phone+": "+content+" "+group;
	}
	
	
}
