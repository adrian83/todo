package com.github.adrian83.todo.web.controller

import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.junit.Test
import org.assertj.core.api.Assertions.*
import org.springframework.http.HttpMethod
import org.springframework.security.test.context.support.WithUserDetails
import com.github.adrian83.todo.TodoApplication
import com.github.adrian83.todo.configuration.TodoTestConfiguration
import com.github.adrian83.todo.domain.user.UserService
import com.github.adrian83.todo.domain.user.model.User
import com.github.adrian83.todo.domain.todo.model.Todo
import org.springframework.http.ResponseEntity
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.util.MultiValueMap
import org.springframework.http.HttpHeaders
import org.assertj.core.api.Assert
import org.assertj.core.api.Assertions.*
import org.junit.BeforeClass
import com.github.adrian83.todo.security.exception.EmailAlreadyUserException
import com.github.adrian83.todo.domain.todo.TodoService
import org.junit.Before
import org.springframework.dao.DataIntegrityViolationException
import com.github.adrian83.todo.web.model.NewTodo


@RunWith(SpringRunner::class)
@SpringBootTest(
	classes = [TodoApplication::class, TodoTestConfiguration::class],
	webEnvironment = WebEnvironment.RANDOM_PORT)
class TodoControllerWebTest: WebTest() {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate
	
	@Autowired
	lateinit var userService: UserService
	
	@Autowired
	lateinit var todoService: TodoService

	@Before
	fun setup(){
		
		try {
			val john = User(TodoTestConfiguration.JOHNS_EMAIL)
			userService.persist(john)
		} catch(ex: DataIntegrityViolationException) {
			print("Exception ${ex::class.java}  ${ex.cause!!::class.java}")
			// its ok, no need for creating user
		}

		try {
			val sandra = User(TodoTestConfiguration.SANDRAS_EMAIL)
			userService.persist(sandra)
			
			todoService.persist(Todo(0L, "todo 1", sandra.id))
			todoService.persist(Todo(0L, "todo 2", sandra.id))
			
		} catch(ex: DataIntegrityViolationException) {
			// its ok, no need for creating user
			print("Exception ${ex::class.java}  ${ex.cause!!::class.java}")
		}
		
	}



    @Test
	@WithUserDetails(
		value=TodoTestConfiguration.JOHNS_EMAIL,
		userDetailsServiceBeanName=TodoTestConfiguration.USER_DETAILS_BEAN)
    fun shouldReturnEmptyListOfTodos() {
		
		// given
		val request = HttpEntity<Void>(headers)
		
		// when
		val response = restTemplate.exchange(
			url(port, "api/v1/todos"),
			HttpMethod.GET,
			request,
			object: ParameterizedTypeReference<List<Todo>>(){})
		
		val expectedTodos = response.getBody();
		
		// then
		assertThat(expectedTodos).isEmpty()
    }
	
	@Test
	@WithUserDetails(
		value=TodoTestConfiguration.SANDRAS_EMAIL,
		userDetailsServiceBeanName=TodoTestConfiguration.USER_DETAILS_BEAN)
    fun shouldReturnListOfTodos() {
		
		// given
		val request = HttpEntity<Void>(headers)
		
		// when
		val response = restTemplate.exchange(
			url(port, "api/v1/todos"),
			HttpMethod.GET,
			request,
			object: ParameterizedTypeReference<List<Todo>>(){})
		
		val expectedTodos = response.getBody();
		
		// then
		assertThat(expectedTodos).isNotEmpty()
    }

	@Test
	@WithUserDetails(
		value=TodoTestConfiguration.SANDRAS_EMAIL,
		userDetailsServiceBeanName=TodoTestConfiguration.USER_DETAILS_BEAN)
    fun shouldPersistNewTodo() {
		
		// given
		val newTodo = NewTodo("Test 3")
		val request = HttpEntity<NewTodo>(newTodo, headers)
		
		// when
		val response = restTemplate.exchange(
			url(port, "api/v1/todos"),
			HttpMethod.POST,
			request,
			object: ParameterizedTypeReference<Todo>(){})
		
		val expectedTodo = response.getBody();
		val signedInUser = userService.findByEmail(TodoTestConfiguration.SANDRAS_EMAIL)
		
		// then
		assertThat(expectedTodo!!.text).isEqualTo(newTodo.text)
		assertThat(expectedTodo.userId).isEqualTo(signedInUser.id)
		assertThat(expectedTodo.id).isGreaterThan(0)
    }
	
}