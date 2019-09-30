package com.github.adrian83.todo.security

import com.github.adrian83.todo.domain.user.UserService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import reactor.core.publisher.Mono
import org.slf4j.LoggerFactory

@Component
class TodoUserDetailsService(val userService: UserService): ReactiveUserDetailsService {
	
	companion object {
		private val logger = LoggerFactory.getLogger(TodoUserDetailsService::class.java)
    }
	
	override fun findByUsername(email: String): Mono<UserDetails> {
		
		logger.info("looking for a user with email: $email")
		
		var user = userService.findByEmail(email)
		return Mono.justOrEmpty(user)
			.map{TodoUserDetails(it!!)}
	}

}