package com.github.adrian83.todo.domain.todo

import org.springframework.data.jpa.repository.JpaRepository
import com.github.adrian83.todo.domain.todo.model.Todo
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface TodoRepository : JpaRepository <Todo, Long> {
	
	fun findAllByUserId(userId: Long): List<Todo>
	
	@Modifying
	@Query("UPDATE Todo t set t.text = :text where t.id = :id AND t.userId = :userId")
	fun update(@Param("text") text: String, @Param("id") id: Long, @Param("userId") userId: Long): Int 
	
}