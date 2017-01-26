package com.cabable.exception;

public enum ExceptionEntity {
	
	LOGIN(1000), DATA(2000), PARSING(3000), ;

	String message; 
	int error_code;
	
	private ExceptionEntity(int error_code) {
		this.error_code = error_code;
	}
	public String getMessage() {
		return message;
	}
	public ExceptionEntity setMessage(String message) {
		this.message = message;
		return this;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	
	
}
