package com.example.springjpa.common.exception;

public class CustomerNotExistingForOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotExistingForOrderException(String msg) {
		super(msg);
	}
	
	public CustomerNotExistingForOrderException(Throwable cause) {
		super(cause);
	}

}
