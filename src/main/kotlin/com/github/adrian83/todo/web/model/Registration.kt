package com.github.adrian83.todo.web.model

import com.github.adrian83.todo.web.validation.ValidRegistration
import javax.validation.Payload
import kotlin.reflect.KClass

@ValidRegistration(
	payload=arrayOf<KClass<in Payload>>(),
	message="{some.key.in.properties}",
	groups=arrayOf<KClass<*>>())
class Registration (val email: String,
					val password: String,
					val repeatedPassword: String) {}