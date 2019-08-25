package com.github.adrian83.todo.web

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import com.github.adrian83.todo.domain.todo.model.Todo
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.github.adrian83.todo.web.model.NewTodo
import com.github.adrian83.todo.domain.todo.TodoService
import java.util.Optional
import org.springframework.web.bind.annotation.PutMapping


@RestController
class TodoController(val todoService: TodoService) {

	@PostMapping("/")
	fun persist(@RequestBody newTodo: NewTodo): Todo = todoService.persist(Todo(0L, newTodo.text))
	
	@GetMapping("/")
	fun findAll(): List<Todo> = todoService.list()

	@GetMapping("/{id}")
	fun findById(@PathVariable id:Long): Optional<Todo> = todoService.findById(id)
	
	@PutMapping("/{id}")
	fun update(@PathVariable id:Long, @RequestBody newTodo: NewTodo): Todo {
		var todo = Todo(id, newTodo.text)
		return todoService.update(todo)
	} 
	
}