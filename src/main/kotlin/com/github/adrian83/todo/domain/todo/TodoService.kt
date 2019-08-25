package com.github.adrian83.todo.domain.todo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.todo.model.Todo

@Service
@Transactional
class TodoService(val todoRepository: TodoRepository){
	
	fun persist(todo: Todo): Todo = todoRepository.save(todo)
	
	fun list(): List<Todo> = todoRepository.findAll()
	
	fun findById(id: Long) = todoRepository.findById(id)
	
	fun update(todo: Todo) = todoRepository.save(todo)
}