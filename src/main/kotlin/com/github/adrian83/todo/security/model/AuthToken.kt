package com.github.adrian83.todo.security

data class AuthToken(
	val userId: Long,
	val email: String) {
}