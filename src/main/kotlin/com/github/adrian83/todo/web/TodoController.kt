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
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal


@RestController
@RequestMapping(value=arrayOf(TodoController.API_PREFIX))
class TodoController(val todoService: TodoService) {

	companion object {
        const val API_PREFIX = "api/v1/"
		const val RES_PREFIX = "todos"
    }
	
	
	@PostMapping(RES_PREFIX)
	fun persist(principal: Principal, @RequestBody newTodo: NewTodo): Todo {
		print("PRINCIPAL: " + principal.getName())
		return todoService.persist(Todo(0L, newTodo.text))
	}
	
	
	@GetMapping(RES_PREFIX)
	fun findAll(): List<Todo> = todoService.list()

	
	@GetMapping(RES_PREFIX+"/{id}")
	fun findById(@PathVariable id:Long): Todo? = todoService.findById(id)
	
	
	@PutMapping(RES_PREFIX+"/{id}")
	fun update(@PathVariable id:Long, @RequestBody newTodo: NewTodo): Todo {
		var todo = Todo(id, newTodo.text)
		return todoService.update(todo)
	} 
	
}