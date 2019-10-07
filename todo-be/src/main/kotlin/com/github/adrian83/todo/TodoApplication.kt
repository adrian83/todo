package com.github.adrian83.todo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

@SpringBootApplication(exclude = arrayOf(SecurityAutoConfiguration::class))
class TodoApplication

fun main(args: Array<String>) {
    SpringApplication.run(TodoApplication::class.java, *args)
}
