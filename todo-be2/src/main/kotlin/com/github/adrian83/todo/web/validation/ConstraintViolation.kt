package com.github.adrian83.todo.wev.validation

import org.springframework.validation.ObjectError

class ConstraintViolation(
    val field: String,
    val message: String
) {

    constructor(objError: ObjectError) : this(objError.getObjectName(), objError.getDefaultMessage()!!)
}
