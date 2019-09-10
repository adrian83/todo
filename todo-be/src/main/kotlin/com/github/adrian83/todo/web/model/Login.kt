package com.github.adrian83.todo.web.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Email

class Login(
	
	@get:NotEmpty(message="{validation.login.email.empty}")
	@get:Email(message="{validation.login.email.invalid}")
	val email: String,
	
	@get:NotEmpty(message="{validation.login.password.empty}")
	val password: String) {}
