package com.example.springjpa.common.exception;

public class DeletingNotExistingOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer orderId;
	
	public DeletingNotExistingOrderException(Integer orderId) {
		super(String.format("Order [%d] does not exists.", orderId));
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

}
