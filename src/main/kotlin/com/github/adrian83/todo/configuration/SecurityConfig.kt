package com.github.adrian83.todo.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService


@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
	
	
	@Bean
fun  securitygWebFilterChain(
  http: ServerHttpSecurity ): SecurityWebFilterChain {
    return http.authorizeExchange()
		.pathMatchers("/api/v1/auth/**")
		.permitAll()
		.pathMatchers("/api/v1/**")
		.authenticated()
		.and()
		.csrf()
		.disable()
		.build();
}
	
@Bean
fun  userDetailsService(): MapReactiveUserDetailsService {
    var user = User
      .withUsername("user")
      .password("password")
      .roles("USER")
      .build();
    return MapReactiveUserDetailsService(user);
}
	

}