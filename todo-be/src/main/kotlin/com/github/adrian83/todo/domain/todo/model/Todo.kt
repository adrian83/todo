package com.github.adrian83.todo.domain.todo.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Todo(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    val text: String,
    @Column(name = "user_id") val userId: Long
)
