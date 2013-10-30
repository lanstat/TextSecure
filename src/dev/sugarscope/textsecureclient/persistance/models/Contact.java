package dev.sugarscope.textsecureclient.persistance.models;

public class Contact {
	public String name;
	public String phone;
	
	public Contact() {
	}

	public Contact(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return name+"\n"+phone;
	}
}
