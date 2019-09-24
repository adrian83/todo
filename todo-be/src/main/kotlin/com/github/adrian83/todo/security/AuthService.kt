package com.github.adrian83.todo.security

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.user.UserService
import org.springframework.security.crypto.bcrypt.BCrypt
import com.github.adrian83.todo.domain.user.model.User
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException
import com.github.adrian83.todo.security.exception.InvalidEmailOrPasswordException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import com.github.adrian83.todo.security.exception.EmailAlreadyUserException

@Service
@Transactional
class AuthService(val userService: UserService,
				  val passwordEncoder: PasswordEncoder,
				  val jwtTokenEncoder: JwtTokenEncoder) {
	
	companion object {
		private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }
	
	fun register(email: String, password: String): User {
		
		var hash = passwordEncoder.hash(password)
		var user = User(0, email, hash)
		try {
		return userService.persist(user)
		} catch(ex: DataIntegrityViolationException) {
			throw EmailAlreadyUserException("user with this email is already registered")
		}
	}
	
	fun login(email: String, password: String): String {
		
		var user = userService.findByEmail(email)
		if(user == null){
			logger.warn("Cannot find user with email: $email")
			throw InvalidEmailOrPasswordException("Invalid email or password")
		}
		
		if(!passwordEncoder.compare(password, user.passwordHash?:"")){
			logger.warn("Password verification failed for user with email: $email")
			throw InvalidEmailOrPasswordException("Invalid email or password")
		}
	
		var authToken = AuthToken(user.id, user.email)

		return jwtTokenEncoder.tokenToString(authToken)
	}
	
}

