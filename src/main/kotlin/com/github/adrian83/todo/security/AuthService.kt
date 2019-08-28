package com.github.adrian83.todo.security

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.user.UserService
import org.springframework.security.crypto.bcrypt.BCrypt
import com.github.adrian83.todo.domain.user.model.User

@Service
@Transactional
class AuthService(val userService: UserService) {
	
	fun register(email: String, password: String) {
		
		var hash = BCrypt.hashpw(password, BCrypt.gensalt(5));
		
		var user = User(0, email, hash)
		
		userService.persist(user)
	}
	
	
}

