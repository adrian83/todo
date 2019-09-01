package com.github.adrian83.todo.web.validation

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy
import kotlin.annotation.AnnotationRetention
import javax.validation.Constraint
import kotlin.reflect.KClass
import javax.validation.Payload

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = arrayOf(RegistrationValidator::class))
@MustBeDocumented
annotation class ValidRegistration(val message: String,
								   val groups: Array<KClass<*>>,
								   val payload: Array<KClass<in Payload>>) {
	
	//fun message(): String = "{org.hibernate.validator.referenceguide.chapter06.classlevel.ValidPassengerCount.message}";

    //fun groups(): Class<?>[] = default { };

    //Class<? extends Payload>[] payload() default { };
	
	
}