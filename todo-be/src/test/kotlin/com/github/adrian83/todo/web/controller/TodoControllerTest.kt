package com.github.adrian83.todo.web.controller

import com.github.adrian83.todo.domain.todo.TodoService
import com.github.adrian83.todo.domain.user.UserService
import com.github.adrian83.todo.web.TodoController
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.boot.test.context.SpringBootTest

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
