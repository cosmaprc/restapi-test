package com.mydomain.restapi.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {

	private String code;
	private String message;
	private String field;

}
