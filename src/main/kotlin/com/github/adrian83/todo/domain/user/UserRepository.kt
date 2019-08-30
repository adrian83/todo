package com.github.adrian83.todo.domain.user

import com.github.adrian83.todo.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository <User, Long> {
	
	fun findByEmail(email: String): User?
}