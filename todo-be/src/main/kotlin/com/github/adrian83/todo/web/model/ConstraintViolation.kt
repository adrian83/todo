package com.github.adrian83.todo.security.model

import org.springframework.validation.ObjectError

class ConstraintViolation(val field: String,
						  val message: String) {
	
	constructor(objError: ObjectError) : this(objError.getObjectName()!!, objError.getDefaultMessage()!!);
}