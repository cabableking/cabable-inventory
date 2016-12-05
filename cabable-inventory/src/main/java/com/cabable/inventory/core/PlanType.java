package com.cabable.inventory.core;

public enum PlanType {
	
	INTERNORM("Intercity", "Dynamic"),INTRANORM("Intracity", "Dynamic"),INTERRENTAL("Intercity", "Rental"),INTRARENTAL("Intracity", "Rental"),FLATFEES("Flat","Flat");
	private String type;
	private String subType;
	
	private PlanType(String type, String subType) {
		this.type= type;
		this.subType=subType;
	}

	public String getType() {
		return type;
	}

	public String getSubType() {
		return subType;
	}

}
