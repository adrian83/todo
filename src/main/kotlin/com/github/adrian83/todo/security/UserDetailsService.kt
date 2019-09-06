package com.github.adrian83.todo.security

import com.github.adrian83.todo.domain.user.UserService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import reactor.core.publisher.Mono

@Component
class TodoUserDetailsService(val userService: UserService): ReactiveUserDetailsService {
	
	override fun findByUsername(email: String): Mono<UserDetails> {
		var user = userService.findByEmail(email)
		return Mono.justOrEmpty(user)
			.map{TodoUserDetails(it!!)}
	}

}