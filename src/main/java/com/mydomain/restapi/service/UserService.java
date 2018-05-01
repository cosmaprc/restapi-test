package com.mydomain.restapi.service;

import com.mydomain.restapi.domain.User;

public interface UserService {

	User saveUser(User user);

	User findUserById(Long id);

	User saveAndFlush(User user);
}
