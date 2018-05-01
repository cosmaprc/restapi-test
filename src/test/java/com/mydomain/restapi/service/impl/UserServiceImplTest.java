package com.mydomain.restapi.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mydomain.restapi.domain.User;
import com.mydomain.restapi.repository.UserRepository;
import com.mydomain.restapi.service.UserService;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

	@TestConfiguration
	static class UserServiceImplTestContextConfiguration {

		@Bean
		public UserService userService() {
			return new UserServiceImpl();
		}
	}

	@Autowired
	private UserService service;

	@MockBean
	private UserRepository repository;

	private User user;

	@Before
	public void setup() {

		user = new User(1L, "username", "first name", "last name", 29);
	}

	@Test
	public void shouldSaveUser() {

		when(repository.save(user)).thenReturn(user);

		final User saved = service.saveUser(user);

		assertEquals(user, saved);

		verify(repository, times(1)).save(user);
	}

	@Test
	public void shouldFindUserById() {

		when(repository.findOne(user.getId())).thenReturn(user);

		final User found = service.findUserById(user.getId());

		assertEquals(user, found);

		verify(repository, times(1)).findOne(user.getId());
	}
}
