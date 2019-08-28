package com.github.adrian83.todo.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.user.model.User

@Service
@Transactional
class UserService (val userRepository: UserRepository){
	
	fun persist(user: User): User = userRepository.save(user)
	
}