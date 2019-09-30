package com.github.adrian83.todo.web.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Email
import javax.validation.constraints.Size

class Login(
	
	@get:NotEmpty(message="{validation.login.email.empty}")
	@get:Email(message="{validation.login.email.invalid}")
	@get:Size.List(
		Size(min=5, message="{validation.login.email.minlen}"),
		Size(max=250, message="{validation.login.email.maxlen}")
	)
	val email: String,
	
	@get:NotEmpty(message="{validation.login.password.empty}")
	@get:Size.List(
		Size(min=1, message="{validation.login.password.minlen}"),
		Size(max=100, message="{validation.login.password.maxlen}")
	)
	val password: String) {}
