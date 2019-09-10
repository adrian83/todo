package com.github.adrian83.todo.web.model

import com.github.adrian83.todo.web.validation.ValidRegistration
import javax.validation.Payload
import kotlin.reflect.KClass
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Email
import javax.validation.GroupSequence
import javax.validation.Valid



@ValidRegistration(message="{validation.registration.password.notmatching}")
class Registration (
	
	@get:NotEmpty(message="{validation.registration.email.empty}")
	@get:Email(message="{validation.registration.email.invalid}")
	val email: String?,
	
	@get:NotEmpty(message="{validation.registration.password.empty}")
	val password: String?,
					
	@get:NotEmpty(message="{validation.registration.repeatedPassword.empty}")
	val repeatedPassword: String?) {}