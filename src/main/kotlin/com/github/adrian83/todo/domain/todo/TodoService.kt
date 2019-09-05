package com.github.adrian83.todo.domain.todo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.todo.model.Todo

@Service
@Transactional
class TodoService(val todoRepository: TodoRepository){
	
	fun persist(todo: Todo): Todo = todoRepository.save(todo)
	
	fun list(): List<Todo> = todoRepository.findAll()
	
	fun listByUser(userId: Long): List<Todo> = todoRepository.findAllByUserId(userId)
	
	fun findById(id: Long): Todo? = todoRepository.findById(id).orElse(null) // TODO: fix this
	
	fun update(todo: Todo): Int = todoRepository.update(todo.text, todo.id, todo.userId)
}