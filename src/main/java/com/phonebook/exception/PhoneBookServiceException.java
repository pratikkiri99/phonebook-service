package com.phonebook.exception;

public class PhoneBookServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhoneBookServiceException(String exception) {
		super(exception);
	}
}
