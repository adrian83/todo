package com.github.adrian83.todo.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class TodoAuthenticationManager(
	var userDetailsService: TodoUserDetailsService,
	var jwtTokenEncoder: JwtTokenEncoder): ReactiveAuthenticationManager {
	
	override fun authenticate(authentication: Authentication): Mono<Authentication>? {

		var tokenStr = authentication.getCredentials().toString()
		var authToken = jwtTokenEncoder.tokenFromString(tokenStr)
		var userDetailsMono = userDetailsService.findByUsername(authToken.email)
		
		return userDetailsMono.map{
			UsernamePasswordAuthenticationToken(it, null, it.getAuthorities())
		}
	}
}