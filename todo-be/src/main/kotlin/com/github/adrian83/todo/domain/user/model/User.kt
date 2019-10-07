package com.github.adrian83.todo.domain.user.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    @Column(unique = true) val email: String,
    @Column(name = "password_hash") val passwordHash: String?
) {

    constructor(email: String) : this(0, email, null) {}
}
