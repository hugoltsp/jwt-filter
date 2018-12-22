package com.hugoltsp.spring.boot.starter.jwt.filter.exception;

public class MalformedAuthorizationHeaderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MalformedAuthorizationHeaderException(String message) {
		super(message);
	}

}
