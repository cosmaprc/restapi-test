package com.mydomain.restapi.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.mydomain.restapi.api.ApiPaths;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	private ApiInfo apiInfo() {

		return new ApiInfoBuilder().title("testco tech Rest API").description("testco tech Rest API")
				.license("").licenseUrl("").termsOfServiceUrl("").version("1.0.0")
				.contact(new Contact("cosmaprc cosmaprc", "", "cosmaprc.cosmaprc@gmail.com")).build();
	}

	@Bean
	public Docket sutomSwaggerConfig() {

		final Predicate<String> apiPaths = regex(ApiPaths.URI_USER_BASE + ".*");

		return new Docket(DocumentationType.SWAGGER_2).select().paths(apiPaths).build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET,
						Arrays.asList(new ResponseMessageBuilder().code(400).message("Bad Request").build()))
				.apiInfo(apiInfo());
	}

}
