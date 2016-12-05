package com.cabable.inventory.core;

public enum Gender {
	M("Male"), F("Female"), O("Other");
	
	public final String fullName;
	
	Gender(String name){
		this.fullName=name;
	}
}
