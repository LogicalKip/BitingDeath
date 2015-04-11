package com.logicalkip.bitingdeath.exceptions;

public class AlreadyThereException extends Exception {
	private static final long serialVersionUID = 2664634144531713385L;

	private String message;
	
	public AlreadyThereException(String msg) {
		this.message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
