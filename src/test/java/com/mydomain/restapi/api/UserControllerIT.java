package com.mydomain.restapi.api;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydomain.restapi.api.exception.ErrorResponse;
import com.mydomain.restapi.domain.User;
import com.mydomain.restapi.util.I18nUtil.I18nKeys;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIT {

	private User user;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MessageSource messageSource;

	@Before
	public void setup() {

		user = new User(1L, "username", "first name", "last name", 29);
	}

	@Test
	public void A_getUserShouldReturn404WhenUserNotFound() throws Exception {

		final ResponseEntity<ErrorResponse> responseEntity = restTemplate.getForEntity(UriComponentsBuilder
				.fromPath(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_GET_BY_ID).buildAndExpand(user.getId()).toUri(),
				ErrorResponse.class);

		final ErrorResponse result = responseEntity.getBody();

		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(ErrorResponse.Type.RESOURCE_NOT_FOUND, result.getType());
		assertEquals(true, result.getErrors().isEmpty());

	}

	@Test
	public void B_addUserShouldCreateUser() throws Exception {

		final String requestJson = mapper.writeValueAsString(user);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

		final ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
				UriComponentsBuilder.fromPath(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_ADD).build().toUri(), entity,
				Void.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	}

	@Test
	public void C_getUserShouldReturnUser() throws Exception {

		final ResponseEntity<User> responseEntity = restTemplate
				.getForEntity(ApiPaths.URI_USER_BASE + "/" + user.getId(), User.class);

		final User result = responseEntity.getBody();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(user.getFirstName(), result.getFirstName());
		assertEquals(user.getLastName(), result.getLastName());
		assertEquals(user.getAge(), result.getAge());
	}

	@Test
	public void D_addUserShouldReturn422OnUserValidationError() throws Exception {

		user.setFirstName("");
		user.setLastName("");
		user.setUserName("");
		user.setAge(-1);

		final String requestJson = mapper.writeValueAsString(user);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

		final ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(
				UriComponentsBuilder.fromPath(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_ADD).build().toUri(), entity,
				ErrorResponse.class);

		final ErrorResponse result = responseEntity.getBody();

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertEquals(ErrorResponse.Type.INVALID_REQUEST, result.getType());
		assertEquals(true, result.getErrors().stream().anyMatch((it) -> {
			return it.getMessage().equals(
					messageSource.getMessage(I18nKeys.ERROR_USER_AGE_POSITIVE, null, LocaleContextHolder.getLocale()));
		}));
		assertEquals(true, result.getErrors().stream().anyMatch((it) -> {
			return it.getMessage().equals(messageSource.getMessage(I18nKeys.ERROR_USER_FIRSTNAME_SIZE, null,
					LocaleContextHolder.getLocale()));
		}));
		assertEquals(true, result.getErrors().stream().anyMatch((it) -> {
			return it.getMessage().equals(
					messageSource.getMessage(I18nKeys.ERROR_USER_LASTNAME_SIZE, null, LocaleContextHolder.getLocale()));
		}));
		assertEquals(true, result.getErrors().stream().anyMatch((it) -> {
			return it.getMessage().equals(
					messageSource.getMessage(I18nKeys.ERROR_USER_USERNAME_SIZE, null, LocaleContextHolder.getLocale()));
		}));
	}

	@Test
	public void E_addUserShouldReturn409OnUsernameUqConstraintViolation() throws Exception {

		final String requestJson = mapper.writeValueAsString(user);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

		final ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(
				UriComponentsBuilder.fromPath(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_ADD).build().toUri(), entity,
				ErrorResponse.class);

		final ErrorResponse result = responseEntity.getBody();

		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		assertEquals(ErrorResponse.Type.DATA_INTEGRITY_VIOLAITON, result.getType());
		assertEquals(messageSource.getMessage(I18nKeys.ERROR_CONSTRAINT_UQ_USER_USERNAME, null,
				LocaleContextHolder.getLocale()), result.getErrors().get(0).getMessage());
	}
}
