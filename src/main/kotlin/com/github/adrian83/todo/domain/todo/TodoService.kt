package com.github.adrian83.todo.domain.todo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.todo.model.Todo

@Service
@Transactional
class TodoService(val todoRepository: TodoRepository){
	
	fun persist(todo: Todo): Todo {
		return todoRepository.save(todo)
	}
}