package com.example.springjpa.common.exception;

public class CustomerNotExistingForOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer customerId;
	
	public CustomerNotExistingForOrderException(Integer customerId) {
		super(String.format("Customer ID [%d] does not exists.", customerId));
		this.customerId = customerId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

}
