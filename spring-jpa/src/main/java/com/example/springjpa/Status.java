package com.example.springjpa;

public class Status {
	
	public final static Status SUCCESS = new Status(true);
	
	public final static Status FAILED_DELETE_CUSTOMER_WITH_ORDERS = new Status(false, "Can not delete customer with existing orders.");
	
	public final static Status FAILED_UNKOWN_REASON = new Status(false, "Unkown error.");
	
	private boolean success;
	
	private String reason;
	
	public Status() {
		this(false);
	}
	
	public Status(boolean success) {
		this.success = success;
	}

	public Status(boolean success, String reason) {
		this.success = success;
		this.reason = reason;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
