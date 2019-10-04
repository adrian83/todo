package com.github.adrian83.todo.domain.todo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.adrian83.todo.domain.todo.model.Todo
import com.github.adrian83.todo.domain.exception.NotFoundException

@Service
@Transactional
class TodoService(val todoRepository: TodoRepository){
	
	fun persist(todo: Todo): Todo = todoRepository.save(todo)
	
	fun listByUser(userId: Long): List<Todo> = todoRepository.findAllByUserId(userId)
	
	fun findByIdAndUser(id: Long, userId: Long): Todo {
		var todo = todoRepository.findByIdAndUser(id, userId)
		if(todo == null){
			throw NotFoundException("Cannot find Todo with id: $id owned by user with id: $userId")
		}
		return todo
	}
	
	fun update(todo: Todo): Todo {
		var updatedNo = todoRepository.update(todo.text, todo.id, todo.userId)
		if(updatedNo == 0) {
			throw NotFoundException("Cannot update Todo with id: ${todo.id} owned by user with id: ${todo.userId}")
		}
		return todo
	}
	
	fun deleteByIdAndUser(id: Long, userId: Long) {
		var deletedNo = todoRepository.deleteByIdAndUser(id, userId)
		if(deletedNo == 0) {
			throw NotFoundException("Cannot delete Todo with id: $id owned by user with id: $userId")
		}
	}
}