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
		var maybeUser = userService.findByEmail(email)
		
		if(maybeUser == null) {
			System.out.println("maybeUser is null")
			return Mono.empty()
		}
		
		return Mono.just(TodoUserDetails(maybeUser))
	}

}