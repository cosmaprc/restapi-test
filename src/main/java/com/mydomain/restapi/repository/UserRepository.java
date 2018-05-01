package com.mydomain.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydomain.restapi.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}