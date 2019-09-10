package com.github.adrian83.todo.web.exception

import org.springframework.validation.ObjectError

class ValidationException(msg: String,
						  val errors: List<ObjectError>): RuntimeException(msg) {

}