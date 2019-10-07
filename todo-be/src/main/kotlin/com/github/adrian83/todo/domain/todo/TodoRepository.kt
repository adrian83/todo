package com.github.adrian83.todo.domain.todo

import com.github.adrian83.todo.domain.todo.model.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TodoRepository : JpaRepository <Todo, Long> {

    fun findAllByUserId(userId: Long): List<Todo>

    @Query("SELECT t FROM Todo t WHERE t.id = :id AND t.userId = :userId")
    fun findByIdAndUser(@Param("id") id: Long, @Param("userId") userId: Long): Todo?

    @Modifying
    @Query("UPDATE Todo t set t.text = :text WHERE t.id = :id AND t.userId = :userId")
    fun update(@Param("text") text: String, @Param("id") id: Long, @Param("userId") userId: Long): Int

    @Modifying
    @Query("DELETE Todo t WHERE t.id = :id AND t.userId = :userId")
    fun deleteByIdAndUser(@Param("id") id: Long, @Param("userId") userId: Long): Int
}
