package com.mydomain.restapi.api.controller;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mydomain.restapi.api.ApiPaths;
import com.mydomain.restapi.api.exception.ErrorResponse;
import com.mydomain.restapi.api.exception.InvalidRequestException;
import com.mydomain.restapi.api.exception.ResourceNotFoundException;
import com.mydomain.restapi.domain.User;
import com.mydomain.restapi.service.UserService;
import com.mydomain.restapi.util.Constants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = ApiPaths.URI_USER_BASE)
@Slf4j
public class UserController {

	private final UserService userService;

	@Inject
	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = ApiPaths.URI_USER_GET_BY_ID)
	@ApiOperation(nickname = "get-user-by-id", value = "Get user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return user", response = User.class),
			@ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class) })
	public Callable<ResponseEntity<User>> getUser(@PathVariable("id") final Long id) {

		final Callable<ResponseEntity<User>> callable = () -> {

			if (log.isDebugEnabled()) {
				log.debug("searching for user by id@" + id);
			}

			final User user = userService.findUserById(id);

			if (user == null) {
				throw new ResourceNotFoundException(Constants.USER_ID_NOT_FOUND);
			}

			if (log.isDebugEnabled()) {
				log.debug("found user@" + user);
			}

			return new ResponseEntity<>(user, HttpStatus.OK);
		};

		return callable;

	}

	@PostMapping(path = ApiPaths.URI_USER_ADD)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(nickname = "add-user", value = "Add User")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "User created", response = Void.class),
			@ApiResponse(code = 422, message = "Unprocessable entity", response = ErrorResponse.class),
			@ApiResponse(code = 409, message = "Data integrity violation", response = ErrorResponse.class) })
	public Callable<ResponseEntity<Void>> addUser(@RequestBody @Valid final User user, final BindingResult errors,
			final HttpServletRequest req) {

		final Callable<ResponseEntity<Void>> callable = () -> {

			if (log.isDebugEnabled()) {
				log.debug("saving user@" + user);
			}

			if (errors.hasErrors()) {
				throw new InvalidRequestException(Constants.USER_VALIDATION_ERRORS, errors);
			}

			final User saved = userService.saveUser(user);

			if (log.isDebugEnabled()) {
				log.debug("saved user@" + saved);
			}

			final HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ServletUriComponentsBuilder.fromContextPath(req)
					.path(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_GET_BY_ID).buildAndExpand(saved.getId()).toUri());

			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		};

		return callable;
	}

}
