package com.github.adrian83.todo.web.validation

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy
import kotlin.annotation.AnnotationRetention
import javax.validation.Constraint
import kotlin.reflect.KClass
import javax.validation.Payload

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = arrayOf(RegistrationValidator::class))
@MustBeDocumented
annotation class ValidRegistration(val message: String = "",
								   val groups: Array<KClass<*>> = [],
								   val payload: Array<KClass<in Payload>> = []) {}