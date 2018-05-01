package com.mydomain.restapi.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mydomain.restapi.domain.User;
import com.mydomain.restapi.repository.UserRepository;
import com.mydomain.restapi.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Inject
	private UserRepository userRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public User saveUser(final User user) {

		return userRepository.save(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public User saveAndFlush(final User user) {

		return userRepository.saveAndFlush(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserById(final Long id) {

		return userRepository.findOne(id);
	}

}
