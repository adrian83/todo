package com.github.adrian83.todo.security

import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCrypt


@Component
class PasswordEncoder {

	fun hash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(5))

	fun compare(password: String, passwordHash: String): Boolean = BCrypt.checkpw(password, passwordHash)

}