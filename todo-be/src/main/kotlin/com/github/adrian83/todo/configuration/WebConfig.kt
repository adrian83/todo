package com.github.adrian83.todo.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.http.HttpMethod
import org.springframework.beans.factory.annotation.Value

//@Configuration
//@EnableWebFlux
class WebConfig: WebFluxConfigurer {
	
	@Value("\${cors.frontend.origin}")
	var frontendOrigin: String = ""
	
	override fun addCorsMappings(corsRegistry: CorsRegistry) {
				
		corsRegistry.addMapping("/**")
			.allowedOrigins(frontendOrigin)
			.allowedMethods(
				HttpMethod.GET.name,
				HttpMethod.POST.name,
				HttpMethod.PUT.name,
				HttpMethod.DELETE.name,
				HttpMethod.OPTIONS.name)
			.maxAge(3600)
	}
}