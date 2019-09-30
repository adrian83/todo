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
import com.github.adrian83.todo.security.exception.InvalidEmailOrPasswordException
import com.github.adrian83.todo.domain.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.dao.DataIntegrityViolationException
import org.slf4j.LoggerFactory
import com.github.adrian83.todo.security.model.ConstraintViolation
import com.github.adrian83.todo.security.exception.EmailAlreadyUserException

@RestController
@RequestMapping(value=arrayOf(TodoController.API_PREFIX))
class AuthController(
	val authService: AuthService,
	val userService: UserService) {

	companion object {
		private val logger = LoggerFactory.getLogger(AuthController::class.java)
		
        const val API_PREFIX = "api/v1/"
		const val RES_PREFIX = "auth"
    }
	
	@PostMapping(RES_PREFIX + "/register")
	fun register(@Valid @RequestBody registration: Registration): User {
		
		logger.info("registering new user with email ${registration.email}")
		
		return authService.register(registration.email!!, registration.password!!)
	}
	
	
	@PostMapping(RES_PREFIX + "/login")
	fun login(@Valid @RequestBody login: Login, response: ServerHttpResponse) {
		
		logger.info("user with email ${login.email} is signing in")
		
		var jwtToken = authService.login(login.email, login.password)
		response.getHeaders().add(HttpHeaders.AUTHORIZATION, jwtToken);
	}
	
	@GetMapping(RES_PREFIX + "/users")
	fun listAll(): List<User> {
		
		logger.info("listing all users")
		
		return userService.listAll()
	}
	
	
    @ExceptionHandler(value=arrayOf(RuntimeException::class))
    fun handleRunTimeException(ex: RuntimeException): ResponseEntity<out Any> {
		
		logger.info("exception [${ex::class}] with message: ${ex.message}")
		ex.printStackTrace()
		
		if(ex is MethodArgumentNotValidException){
			
			var violations = ex.getBindingResult().getAllErrors().map { ConstraintViolation(it.getCode()!!, it.getDefaultMessage()!!)}
			return ResponseEntity<List<ConstraintViolation>>(violations, HttpStatus.BAD_REQUEST)
			
		} else if(ex is WebExchangeBindException){
			
			var violations = ex.getAllErrors().map {
				ConstraintViolation(it.getCode()!!, it.getDefaultMessage()!!)
			}
			return ResponseEntity<List<ConstraintViolation>>(violations, HttpStatus.BAD_REQUEST)
			
		} else if(ex is InvalidEmailOrPasswordException){
			
			var violation = ConstraintViolation("login", ex.message!!)
			return ResponseEntity<List<ConstraintViolation>>(listOf(violation), HttpStatus.BAD_REQUEST)
			
		} else if(ex is EmailAlreadyUserException) {
			
			var violation = ConstraintViolation("register", ex.message!!)
			return ResponseEntity<List<ConstraintViolation>>(listOf(violation), HttpStatus.BAD_REQUEST)
		}
		
        return ResponseEntity<String>(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}