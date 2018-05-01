package com.mydomain.restapi.api.exception;

import org.springframework.validation.BindingResult;

public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final BindingResult errors;

	public InvalidRequestException(final String message, final BindingResult errors) {
		super(message);
		this.errors = errors;
	}

	public BindingResult getErrors() {
		return errors;
	}

}
