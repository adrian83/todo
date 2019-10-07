package com.github.adrian83.todo.web.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = arrayOf(RegistrationValidator::class))
@MustBeDocumented
annotation class ValidRegistration(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<in Payload>> = []
)
