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
import org.hibernate.exception.ConstraintViolationException

@Service
@Transactional
class AuthService(val userService: UserService,
				  val passwordEncoder: PasswordEncoder,
				  val jwtTokenEncoder: JwtTokenEncoder) {
	
	companion object {
		private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }
	
	fun register(email: String, password: String): User {
		
		logger.info("registering user with email: $email")
		
		try {
			var hash = passwordEncoder.hash(password)
			var user = User(0, email, hash)
			return userService.persist(user)
		} catch(ex: DataIntegrityViolationException) {
			logger.warn("cannot register user because of: ${ex.message}")
			throw EmailAlreadyUserException("user with this email is already registered")
		} catch(ex: ConstraintViolationException){
			logger.warn("cannot register user because of: ${ex.message}")
			throw EmailAlreadyUserException("user with this email is already registered")
		} catch(ex: Exception) {
			throw EmailAlreadyUserException("test")
		}
	}
	
	fun login(email: String, password: String): String {
		
		logger.info("signing in user with email: $email")
		
		var user = userService.findByEmail(email)
		if(user == null){
			logger.warn("cannot find user with email: $email")
			throw InvalidEmailOrPasswordException("Invalid email or password")
		}
		
		if(!passwordEncoder.compare(password, user.passwordHash?:"")){
			logger.warn("password verification failed for user with email: $email")
			throw InvalidEmailOrPasswordException("Invalid email or password")
		}
	
		var authToken = AuthToken(user.id, user.email)

		return jwtTokenEncoder.tokenToString(authToken)
	}
	
}

