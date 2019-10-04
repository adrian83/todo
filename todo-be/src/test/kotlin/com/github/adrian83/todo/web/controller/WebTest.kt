package com.github.adrian83.todo.web.controller

import org.springframework.http.HttpHeaders

open class WebTest {
	
	
	val headers = HttpHeaders().run {
		this.add(HttpHeaders.CONTENT_TYPE, "application/json")
		this
	}
	
	fun url(port: Int, postfix: String): String = "http://localhost:$port/$postfix"
	
}