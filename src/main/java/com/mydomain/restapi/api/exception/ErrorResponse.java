package com.mydomain.restapi.api.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.mydomain.restapi.util.I18nUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {

	public enum Type {
		UNKNOWN, INVALID_REQUEST, RESOURCE_NOT_FOUND, DATA_INTEGRITY_VIOLAITON
	}

	private Type type;

	private List<Error> errors = new ArrayList<>();

	public ErrorResponse(final Type type) {

		this.type = type;
	}

	public ErrorResponse(final Type type, final Error error) {

		this(type);
		this.addError(error);
	}

	public ErrorResponse(final Type type, final List<Error> errors) {

		this(type);
		this.addErrors(errors);
	}

	public void addError(final Error error) {

		if (error != null) {
			this.errors.add(error);
		}
	}

	public void addErrors(final List<Error> errors) {

		if (errors != null && !errors.isEmpty()) {
			this.errors.addAll(errors);
		}
	}

	public static List<Error> extractErrors(final BindingResult result, final MessageSource messageSource) {

		if (result != null) {
			return extractErrors(result.getFieldErrors(), messageSource);
		}

		return new ArrayList<>();
	}

	public static List<Error> extractErrors(final List<FieldError> fieldErrors, final MessageSource messageSource) {

		final List<Error> errors = new ArrayList<>();

		if (fieldErrors != null && !fieldErrors.isEmpty()) {
			fieldErrors.stream().forEach((e) -> {
				final Locale currentLocale = LocaleContextHolder.getLocale();
				final String msg = messageSource.getMessage(e.getDefaultMessage(), null, currentLocale);
				errors.add(new Error(e.getCode(), msg, e.getField()));
			});
		}

		return errors;
	}

	public static Error extractError(final DataIntegrityViolationException ex, final MessageSource messageSource) {

		final String errorMsg = ex.getMessage();

		if (errorMsg != null) {
			final Optional<Map.Entry<String, ObjectError>> entry = I18nUtil.getObjectErrorForErrorMessage(errorMsg);
			if (entry.isPresent()) {
				final Locale currentLocale = LocaleContextHolder.getLocale();
				final String msg = messageSource.getMessage(entry.get().getValue().getDefaultMessage(), null,
						currentLocale);
				return new Error(entry.get().getValue().getCode(), msg, entry.get().getValue().getObjectName());
			}
		}

		return null;
	}
}
