package com.github.adrian83.todo.domain.user.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType



@Entity
data class User(
	@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
	val email: String,
	val passwordHash: String?) {
	
	constructor(email: String) : this(0, email, null){}
}