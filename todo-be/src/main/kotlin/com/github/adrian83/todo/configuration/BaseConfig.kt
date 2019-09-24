package com.github.adrian83.todo.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.MessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.http.HttpMethod

@Configuration
class BaseConfig {

	@Value("\${cors.frontend.origin}")
	var frontendOrigin: String = ""
	
	@Bean
	fun validator(msgSource: MessageSource): LocalValidatorFactoryBean {
		var validatorFactory = LocalValidatorFactoryBean()
		validatorFactory.setValidationMessageSource(msgSource)
		return validatorFactory
	}
	
	@Bean
	fun messageSource(): MessageSource {
		var messageSource = ReloadableResourceBundleMessageSource()
		messageSource.setBasename("classpath:messages");
    	messageSource.setDefaultEncoding("UTF-8");
    	return messageSource;
	}
	
}