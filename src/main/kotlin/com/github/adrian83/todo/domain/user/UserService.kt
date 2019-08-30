package com.github.adrian83.todo.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.user.model.User
import org.springframework.data.domain.Example

@Service
@Transactional
class UserService (val userRepository: UserRepository){
	
	fun persist(user: User): User = userRepository.save(user)
	
	fun findByEmail(email: String): User? {
		
		var user = User(email)
		var example = Example.of(user)
		
		return userRepository.findOne(example).orElse(null)
	} 
	
}