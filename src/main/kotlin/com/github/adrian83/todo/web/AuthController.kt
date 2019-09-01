package com.github.adrian83.todo.web

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import com.github.adrian83.todo.security.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.github.adrian83.todo.web.model.Registration
import com.github.adrian83.todo.web.model.Login
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.HttpHeaders
import com.github.adrian83.todo.domain.user.model.User

@RestController
@RequestMapping(value=arrayOf(TodoController.API_PREFIX))
class AuthController(val authService: AuthService) {

	companion object {
        const val API_PREFIX = "api/v1/"
		const val RES_PREFIX = "auth"
    }
	
	@PostMapping(RES_PREFIX + "/register")
	fun register(@RequestBody registration: Registration): User {
		
		System.out.println("Registration")
	
		return authService.register(registration.email, registration.password)
	}
	
	
		@PostMapping(RES_PREFIX + "/login")
	fun login(@RequestBody login: Login, response: ServerHttpResponse) {
		
		System.out.println("Login")
	
		authService.login(login.email, login.password).map{
			response.getHeaders().add(HttpHeaders.AUTHORIZATION, it);
		}
	}
}