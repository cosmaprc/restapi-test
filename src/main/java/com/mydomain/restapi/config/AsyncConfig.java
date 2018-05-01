package com.mydomain.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AsyncConfig {

	@Value("${threadpooltaskexecutor.corepoolsize}")
	private int corePoolSize;

	@Bean
	WebMvcConfigurer configurer() {

		return new WebMvcConfigurerAdapter() {

			@Override
			public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
				final ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
				t.setCorePoolSize(corePoolSize);
				t.setAllowCoreThreadTimeOut(true);
				t.setKeepAliveSeconds(60);
				t.initialize();
				configurer.setTaskExecutor(t);
				configurer.setDefaultTimeout(10000000); // in milliseconds
			}
		};
	}

}
