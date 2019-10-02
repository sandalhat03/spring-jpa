package com.example.springjpa.exception;

public class CustomerNotExistingForOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotExistingForOrderException(Throwable cause) {
		super(cause);
	}

}