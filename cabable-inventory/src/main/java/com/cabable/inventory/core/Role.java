package com.cabable.inventory.core;

public enum Role {

	SUPERADMIN("Cabable Admin, Allows everything", 1800, 900, 3600),
	ADMIN("Operator Admin, Allows everything within the operator", 1800, 900, 3600), 
	READONLY("Operator Read only, Allows only read", 1800, 900, 3600), 
	BOOKONLY("Operator Book only, Allows only booking a cab", 1800, 900, 3600);

	public static final String DESCRIPTION_FIELD = "description";
	public static final String CC_EXPIRES_IN_FIELD = "ccExpiresIn";
	public static final String PASS_EXPIRES_IN_FIELD = "passExpiresIn";
	public static final String REFRESH_EXPIRES_IN_FIELD = "refreshExpiresIn";

	private Role(String desc, int cc, int pass, int refresh) {
		this.description=desc;
		this.cc_expires_in=cc;
		this.pass_expires_in=pass;
		this.refresh_expires_in=refresh;
	}

	private String description;
	private int cc_expires_in;
	private int pass_expires_in;
	private int refresh_expires_in;
	
	public String getDescription() {
		return description;
	}
	public int getCc_expires_in() {
		return cc_expires_in;
	}
	public int getPass_expires_in() {
		return pass_expires_in;
	}
	public int getRefresh_expires_in() {
		return refresh_expires_in;
	}


}
