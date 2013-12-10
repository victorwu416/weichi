package com.victorkywu.weichi;

public class ContactItem {
	String displayName;
	String phoneNumber;
	boolean isPicked;
	
	public ContactItem(String displayName, String phoneNumber, boolean isPicked) {
		this.displayName = displayName;
		this.phoneNumber = phoneNumber;
		this.isPicked = isPicked;		
	}
}