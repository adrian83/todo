package com.github.adrian83.todo.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import com.github.adrian83.todo.security.TodoSecurityContextRepository
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.authentication.ReactiveAuthenticationManager
import com.github.adrian83.todo.security.TodoAuthenticationManager


@Configuration
@EnableWebFluxSecurity
class SecurityConfig  {
	
	@Bean
	fun securitygWebFilterChain(http: ServerHttpSecurity,
								authenticationManager: TodoAuthenticationManager,
								securityContextRepository: TodoSecurityContextRepository): SecurityWebFilterChain {
    return http
		.httpBasic().disable()
		.formLogin().disable()
		.csrf().disable()
		.authorizeExchange()
		.pathMatchers("/h2-console").permitAll()
		.pathMatchers("/api/v1/auth/**").permitAll()
		.pathMatchers("/api/v1/**").authenticated()
		.anyExchange().authenticated()
		.and()
		.authenticationManager(authenticationManager)
		.securityContextRepository(securityContextRepository)
		.build();
	}
}