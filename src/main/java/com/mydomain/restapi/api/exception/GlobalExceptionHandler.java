package com.mydomain.restapi.api.exception;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	@Inject
	public GlobalExceptionHandler(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex, final WebRequest request) {

		if (log.isErrorEnabled()) {
			log.error("handling exception...", ex);
		}

		final ErrorResponse errors = new ErrorResponse(ErrorResponse.Type.UNKNOWN);

		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(final ResourceNotFoundException ex,
			final WebRequest request) {

		if (log.isErrorEnabled()) {
			log.error("handling ResourceNotFoundException...", ex);
		}

		final ErrorResponse errors = new ErrorResponse(ErrorResponse.Type.RESOURCE_NOT_FOUND);

		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(final DataIntegrityViolationException ex,
			final WebRequest request) {

		if (log.isErrorEnabled()) {
			log.error("handling DataIntegrityViolationException...", ex);
		}

		final ErrorResponse errors = new ErrorResponse(ErrorResponse.Type.DATA_INTEGRITY_VIOLAITON,
				ErrorResponse.extractError(ex, messageSource));

		return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = { InvalidRequestException.class })
	public ResponseEntity<ErrorResponse> handleInvalidRequestException(final InvalidRequestException ex,
			final WebRequest req) {

		if (log.isErrorEnabled()) {
			log.error("handling InvalidRequestException...", ex);
		}

		final ErrorResponse msg = new ErrorResponse(ErrorResponse.Type.INVALID_REQUEST,
				ErrorResponse.extractErrors(ex.getErrors(), messageSource));

		return new ResponseEntity<>(msg, HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
