package com.mydomain.restapi;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mydomain.restapi.domain.User;
import com.mydomain.restapi.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {
	@Autowired
	private UserService userService;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	int counter = 0;

	@Scheduled(fixedRate = 10000)
	public void reportCurrentTime() {
		log.info("IN The time is now {}", dateFormat.format(new Date()));

		userService.saveAndFlush(User.builder().age(1).firstName("first name").lastName("last name")
				.userName("user name" + counter).build());
		counter++;

		log.info("OUT The time is now {}", dateFormat.format(new Date()));
	}
}