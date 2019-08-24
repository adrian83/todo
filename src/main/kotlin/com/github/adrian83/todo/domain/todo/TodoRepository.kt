package com.github.adrian83.todo.domain.todo

import org.springframework.data.jpa.repository.JpaRepository
import com.github.adrian83.todo.domain.todo.model.Todo


interface TodoRepository : JpaRepository <Todo, Long>