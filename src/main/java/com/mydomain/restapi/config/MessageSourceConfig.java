package com.mydomain.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {

		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setCacheSeconds(3600);

		return messageSource;
	}

}
