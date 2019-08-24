package com.github.adrian83.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
class TodoApplication

fun main(args: Array<String>) {
	SpringApplication.run(TodoApplication::class.java, *args)
}