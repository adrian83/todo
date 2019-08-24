package com.github.adrian83.todo.web

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import com.github.adrian83.todo.domain.todo.model.Todo
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.github.adrian83.todo.web.model.NewTodo
import com.github.adrian83.todo.domain.todo.TodoService


@RestController
class TodoController(val todoService: TodoService) {

	@PostMapping("/")
	fun persist(@RequestBody newTodo: NewTodo): Todo 
		= todoService.persist(Todo(0L, newTodo.text))
	
	
	@GetMapping("/")
	fun findAll() = listOf<Todo>(
		Todo(1L, "Read good book about Kotlin"),
		Todo(1L, "Read good book about Kotlin"))

	@GetMapping("/{id}")
	fun findByLastName(@PathVariable id:Long)
		= Todo(id, "Read good book about Kotlin")
}