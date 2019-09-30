package com.github.adrian83.todo.web.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class NewTodo(
	
	@get:NotEmpty(message="{validation.newtodo.text.empty}")
	@get:Size.List(
		Size(min=5, message="{validation.newtodo.text.minlen}"),
		Size(max=1000, message="{validation.newtodo.text.maxlen}")
	)
	val text: String
);