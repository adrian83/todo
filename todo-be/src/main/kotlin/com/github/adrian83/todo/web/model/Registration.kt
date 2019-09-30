package com.github.adrian83.todo.web.model

import com.github.adrian83.todo.web.validation.ValidRegistration
import javax.validation.Payload
import kotlin.reflect.KClass
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Email
import javax.validation.GroupSequence
import javax.validation.Valid
import javax.validation.constraints.Size



@ValidRegistration(message="{validation.registration.password.notmatching}")
class Registration (
	
	@get:NotEmpty(message="{validation.registration.email.empty}")
	@get:Email(message="{validation.registration.email.invalid}")
	@get:Size.List(
		Size(min=5, message="{validation.registration.email.minlen}"),
		Size(max=250, message="{validation.registration.email.maxlen}")
	)
	val email: String?,
	
	@get:NotEmpty(message="{validation.registration.password.empty}")
		@get:Size.List(
		Size(min=1, message="{validation.registration.password.minlen}"),
		Size(max=100, message="{validation.registration.password.maxlen}")
	)
	val password: String?,
					
	@get:NotEmpty(message="{validation.registration.repeatedPassword.empty}")
	val repeatedPassword: String?) {}