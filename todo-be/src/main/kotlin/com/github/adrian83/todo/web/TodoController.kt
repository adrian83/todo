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
import com.github.adrian83.todo.domain.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping


@RestController
@RequestMapping(value=arrayOf(TodoController.API_PREFIX))
class TodoController(val todoService: TodoService,
					 val userService: UserService) {

	companion object {
		private val logger = LoggerFactory.getLogger(TodoController::class.java)
		
        const val API_PREFIX = "api/v1/"
		const val RES_PREFIX = "todos"
    }
	
	
	@PostMapping(RES_PREFIX)
	fun persist(principal: Principal, @RequestBody newTodo: NewTodo): Todo {
		
		logger.info("creating new todo: $newTodo by ${principal.getName()}")
		
		val user = userService.findByEmail(principal.getName())
		return todoService.persist(Todo(0L, newTodo.text, user!!.id))
	}
	
	
	@GetMapping(RES_PREFIX)
	fun findAll(principal: Principal): List<Todo> {
		
		logger.info("listing all todos of ${principal.getName()}")
		
		val user = userService.findByEmail(principal.getName())
		return todoService.listByUser(user!!.id)
	}

	
	@GetMapping(RES_PREFIX+"/{id}")
	fun findById(principal: Principal, @PathVariable id:Long): Todo? {
		
		logger.info("getting todo with id $id by ${principal.getName()}")
		
		val user = userService.findByEmail(principal.getName())
		return todoService.findByIdAndUser(id, user!!.id)
	} 
	
	
	@PutMapping(RES_PREFIX+"/{id}")
	fun update(principal: Principal, @PathVariable id:Long, @RequestBody newTodo: NewTodo): Todo? {
		
		logger.info("getting todo with id $id by ${principal.getName()}")
		
		val user = userService.findByEmail(principal.getName())
		var todo = Todo(id, newTodo.text, user!!.id)
		return if(todoService.update(todo) > 0) todo else null 
	}
	
	@DeleteMapping(RES_PREFIX+"/{id}")
	fun delete(principal: Principal, @PathVariable id:Long) {
		
		logger.info("removing todo with id $id by ${principal.getName()}")
		
		val user = userService.findByEmail(principal.getName())
		todoService.deleteByIdAndUser(id, user!!.id)
	} 
	
}