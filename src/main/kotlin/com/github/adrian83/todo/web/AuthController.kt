package com.github.adrian83.todo.web

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import com.github.adrian83.todo.security.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.github.adrian83.todo.web.model.Registration

@RestController
@RequestMapping(value=arrayOf(TodoController.API_PREFIX))
class AuthController(val authService: AuthService) {

	companion object {
        const val API_PREFIX = "api/v1/"
		const val RES_PREFIX = "auth"
    }
	
	@PostMapping(RES_PREFIX+"/register")
	fun persist(@RequestBody registration: Registration): Boolean {
		
		System.out.println("Registration")
	
		authService.register(registration.email, registration.password)
		return true	
	}
	
}