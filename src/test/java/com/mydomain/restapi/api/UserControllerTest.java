package com.mydomain.restapi.api;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydomain.restapi.TestConstants;
import com.mydomain.restapi.api.controller.UserController;
import com.mydomain.restapi.api.exception.ErrorResponse;
import com.mydomain.restapi.api.exception.ResourceNotFoundException;
import com.mydomain.restapi.domain.User;
import com.mydomain.restapi.service.UserService;
import com.mydomain.restapi.util.Constants;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@TestConfiguration
	static class UserControllerTestContextConfiguration {

		@Bean
		public ResourceBundleMessageSource messageSource() {
			final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

			messageSource.setBasename("i18n/messages");
			messageSource.setUseCodeAsDefaultMessage(true);

			return messageSource;
		}
	}

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	private User user;

	@Before
	public void setup() {

		user = new User(1L, "username", "first name", "last name", 29);
	}

	@Test
	public void getUserShouldReturnUser() throws Exception {

		when(userService.findUserById(user.getId())).thenReturn(user);

		final MvcResult resultActions = mockMvc
				.perform(get(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_GET_BY_ID, user.getId()))
				.andExpect(request().asyncStarted()).andReturn();

		mockMvc.perform(asyncDispatch(resultActions)).andDo(print())
				.andExpect(jsonPath(TestConstants.JSON_USER_USERNAME).value(user.getUserName()))
				.andExpect(jsonPath(TestConstants.JSON_USER_FIRSTNAME).value(user.getFirstName()))
				.andExpect(jsonPath(TestConstants.JSON_USER_LASTNAME).value(user.getLastName()))
				.andExpect(jsonPath(TestConstants.JSON_USER_AGE).value(user.getAge())).andExpect(status().isOk());
	}

	@Test
	public void getUserShouldReturn404WhenUserNotFound() throws Exception {

		when(userService.findUserById(user.getId()))
				.thenThrow(new ResourceNotFoundException(Constants.USER_ID_NOT_FOUND));

		final MvcResult resultActions = mockMvc
				.perform(get(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_GET_BY_ID, this.user.getId()))
				.andExpect(request().asyncStarted()).andReturn();

		mockMvc.perform(asyncDispatch(resultActions)).andDo(print())
				.andExpect(jsonPath(TestConstants.JSON_API_ERROR_TYPE)
						.value(ErrorResponse.Type.RESOURCE_NOT_FOUND.toString()))
				.andExpect(jsonPath(TestConstants.JSON_API_ERROR_ERRORS).isEmpty()).andExpect(status().isNotFound());
	}

	@Test
	public void addUserShouldCreateUser() throws Exception {

		when(userService.saveUser(any(User.class))).thenReturn(user);

		final String json = mapper.writeValueAsString(user);

		final MvcResult resultActions = mockMvc.perform(post(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_ADD)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(request().asyncStarted()).andReturn();

		mockMvc.perform(asyncDispatch(resultActions)).andDo(print())
				.andExpect(redirectedUrl(TestConstants.CONTEXT_PATH
						+ UriComponentsBuilder.fromPath(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_GET_BY_ID)
								.buildAndExpand(user.getId()).toUri().toString()))
				.andExpect(status().isCreated());
	}

	@Test
	public void addUserShouldReturn422OnUserValidationError() throws Exception {

		user.setAge(-1);

		final String json = mapper.writeValueAsString(user);

		final MvcResult resultActions = mockMvc.perform(post(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_ADD)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(request().asyncStarted()).andReturn();

		mockMvc.perform(asyncDispatch(resultActions)).andDo(print())
				.andExpect(jsonPath(TestConstants.JSON_API_ERROR_TYPE)
						.value(ErrorResponse.Type.INVALID_REQUEST.toString()))
				.andExpect(jsonPath(TestConstants.JSON_API_ERROR_ERRORS).isNotEmpty())
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void addUserShouldReturn409OnUsernameUqConstraintViolation() throws Exception {

		when(userService.saveUser(any(User.class)))
				.thenThrow(new DataIntegrityViolationException(Constants.CONSTRAINT_UQ_USER_USERNAME));

		final String json = mapper.writeValueAsString(user);

		final MvcResult resultActions = mockMvc.perform(post(ApiPaths.URI_USER_BASE + ApiPaths.URI_USER_ADD)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(request().asyncStarted()).andReturn();

		mockMvc.perform(asyncDispatch(resultActions)).andDo(print())
				.andExpect(jsonPath(TestConstants.JSON_API_ERROR_TYPE)
						.value(ErrorResponse.Type.DATA_INTEGRITY_VIOLAITON.toString()))
				.andExpect(jsonPath(TestConstants.JSON_API_ERROR_ERRORS).isNotEmpty()).andExpect(status().isConflict());
	}
}
