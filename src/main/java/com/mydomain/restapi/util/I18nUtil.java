package com.mydomain.restapi.util;

import java.util.Map;
import java.util.Optional;

import org.springframework.validation.ObjectError;

import com.google.common.collect.ImmutableMap;

public class I18nUtil {

	public static final Map<String, ObjectError> CONSTRAINT_CODE_MAP = ImmutableMap.<String, ObjectError>builder()
			.put(Constants.CONSTRAINT_UQ_USER_USERNAME,
					new ObjectError(Constants.CONSTRAINT_UQ_USER_USERNAME, I18nKeys.ERROR_CONSTRAINT_UQ_USER_USERNAME))
			.build();

	public static Optional<Map.Entry<String, ObjectError>> getObjectErrorForErrorMessage(final String errorMessage) {
		return CONSTRAINT_CODE_MAP.entrySet().stream().filter((it) -> errorMessage.contains(it.getKey())).findFirst();
	}

	public static class I18nKeys {

		// field validation

		public static final String ERROR_USER_USERNAME_SIZE = "error.user.username.size";

		public static final String ERROR_USER_FIRSTNAME_SIZE = "error.user.firstname.size";

		public static final String ERROR_USER_LASTNAME_SIZE = "error.user.lastname.size";

		public static final String ERROR_USER_AGE_POSITIVE = "error.user.age.positive";

		// constraints validation

		public static final String ERROR_CONSTRAINT_UQ_USER_USERNAME = "error.constraint.uq.user.username";

	}

	private I18nUtil() {
	}

}
