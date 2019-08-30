package com.github.adrian83.todo.security

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.user.UserService
import org.springframework.security.crypto.bcrypt.BCrypt
import com.github.adrian83.todo.domain.user.model.User
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException

@Service
@Transactional
class AuthService(val userService: UserService,
				  val passwordEncoder: PasswordEncoder,
				  val jwtTokenEncoder: JwtTokenEncoder) {
	
	fun register(email: String, password: String): User {
		var hash = passwordEncoder.hash(password)
		var user = User(0, email, hash)
		return userService.persist(user)
	}
	
	fun login(email: String, password: String): Mono<String> {
		
		System.out.println("in login 1 " + email)
		
		var user = userService.findByEmail(email)
		
		if(user == null){
			System.out.println("in login 2")
			return Mono.empty()
		}
		
		if(!passwordEncoder.compare(password, user.passwordHash?:"")){
			System.out.println("in login 3")
			throw IllegalArgumentException("invalidPassword or login")
		}
		
		System.out.println("in login 4")
		var authToken = AuthToken(user.id)
		var authTokenStr = jwtTokenEncoder.tokenToString(authToken)
		System.out.println("in login 5")
		return Mono.just(authTokenStr)
	}
	
	
}

