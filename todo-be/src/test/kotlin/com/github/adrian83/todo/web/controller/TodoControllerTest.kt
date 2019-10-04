package com.github.adrian83.todo.web.controller

import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.context.SpringBootTest
import org.junit.runner.RunWith
import org.mockito.Mock
import com.github.adrian83.todo.domain.user.UserService
import com.github.adrian83.todo.security.AuthService
import org.mockito.InjectMocks
import com.github.adrian83.todo.web.TodoController
import org.junit.Assert
import org.junit.Test
import org.mockito.junit.MockitoJUnitRunner
import com.github.adrian83.todo.domain.todo.TodoService

@RunWith(MockitoJUnitRunner::class)
@SpringBootTest
class TodoControllerTest {

	@InjectMocks
    lateinit var todoController: TodoController
	
    @Mock
	lateinit var todoService: TodoService
	
	@Mock
	lateinit var userService: UserService

	@Test
	fun canary() {
		Assert.assertTrue(true)
	}
	
	
}