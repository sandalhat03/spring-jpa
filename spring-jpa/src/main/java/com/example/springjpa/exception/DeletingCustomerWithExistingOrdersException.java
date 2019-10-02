package com.example.springjpa.exception;

public class DeletingCustomerWithExistingOrdersException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeletingCustomerWithExistingOrdersException(Throwable cause) {
		super(cause);
	}

}
