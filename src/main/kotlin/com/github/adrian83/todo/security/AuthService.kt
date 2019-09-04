package com.github.adrian83.todo.security

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.user.UserService
import org.springframework.security.crypto.bcrypt.BCrypt
import com.github.adrian83.todo.domain.user.model.User
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException
import com.github.adrian83.todo.security.exception.InvalidEmailOrPasswordException

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
	
	fun login(email: String, password: String): String {
		
		var user = userService.findByEmail(email)
		if(user == null){
			throw InvalidEmailOrPasswordException("Invalid email or password")
		}
		
		if(!passwordEncoder.compare(password, user.passwordHash?:"")){
			throw InvalidEmailOrPasswordException("Invalid email or password")
		}
	
		var authToken = AuthToken(user.id, user.email)
		var authTokenStr = jwtTokenEncoder.tokenToString(authToken)

		return authTokenStr
	}
	
	
}

