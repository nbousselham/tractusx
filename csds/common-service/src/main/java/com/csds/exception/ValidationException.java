package com.csds.exception;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ValidationException(String exceptionstr) {
		super(exceptionstr);
	}
}
