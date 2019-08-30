package com.github.adrian83.todo.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class TodoAuthenticationManager(var userDetailsService: TodoUserDetailsService): ReactiveAuthenticationManager {
	
	override fun authenticate(authentication: Authentication): Mono<Authentication>? {
		var authToken = authentication.getCredentials().toString()
		var userDetailsMono = userDetailsService.findByUsername(authToken)
		return userDetailsMono.map{
			UsernamePasswordAuthenticationToken(it, null, it.getAuthorities())
		}
	}
}