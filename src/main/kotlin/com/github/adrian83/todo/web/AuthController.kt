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
import javax.validation.Valid
import org.springframework.validation.Errors
import com.github.adrian83.todo.web.exception.ValidationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.validation.ObjectError

@RestController
@RequestMapping(value=arrayOf(TodoController.API_PREFIX))
class AuthController(val authService: AuthService) {

	companion object {
        const val API_PREFIX = "api/v1/"
		const val RES_PREFIX = "auth"
    }
	
	@PostMapping(RES_PREFIX + "/register")
	fun register(@Valid @RequestBody registration: Registration): User {
		return authService.register(registration.email!!, registration.password!!)
	}
	
	
	@PostMapping(RES_PREFIX + "/login")
	fun login(@Valid @RequestBody login: Login, response: ServerHttpResponse) {
		authService.login(login.email, login.password)
		.map{
			response.getHeaders().add(HttpHeaders.AUTHORIZATION, it);
		}
		
	}
	
	
    @ExceptionHandler(value=arrayOf(RuntimeException::class))
    fun handleRunTimeException(ex: RuntimeException): ResponseEntity<out Any> {
		
		System.out.println("Class" + ex::class)
		ex.printStackTrace()
		
		if(ex is MethodArgumentNotValidException){
			return ResponseEntity<List<ObjectError>>(ex.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST)
		} else if(ex is WebExchangeBindException){
			return ResponseEntity<List<ObjectError>>(ex.getAllErrors(), HttpStatus.BAD_REQUEST)
		}
		
        return ResponseEntity<String>(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}