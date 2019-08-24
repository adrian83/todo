package com.github.adrian83.todo.domain.todo.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

@Entity
data class Todo(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long, val text: String) {
}