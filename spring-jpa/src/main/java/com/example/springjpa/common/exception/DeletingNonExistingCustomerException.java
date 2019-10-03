package com.example.springjpa.common.exception;

public class DeletingNonExistingCustomerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer customerId;
	
	public DeletingNonExistingCustomerException(Integer customerId) {
		super(String.format("Customer [%d] does not exists.", customerId));
		this.customerId = customerId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

}
