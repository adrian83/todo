package com.github.adrian83.todo.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.MessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class BaseConfig {

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