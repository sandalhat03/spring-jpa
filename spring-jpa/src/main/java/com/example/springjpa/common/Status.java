package com.example.springjpa.common;

import java.util.List;

public class Status {
	
	public final static Status SUCCESS = new Status(true);
	
	private boolean success;
	
	private String reason;
	
	private List<FieldError> fieldErrors;
	
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

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public static class FieldError {
		
		private String name;
		
		private Object value;
		
		private String message;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		
	}
}
