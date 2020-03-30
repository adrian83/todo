package com.github.adrian83.todo.web.controller

import com.github.adrian83.todo.domain.todo.TodoService
import com.github.adrian83.todo.domain.todo.model.Todo
import com.github.adrian83.todo.domain.user.UserService
import com.github.adrian83.todo.domain.user.model.User
import com.github.adrian83.todo.web.TodoController
import com.github.adrian83.todo.web.model.NewTodo
import javax.security.auth.kerberos.KerberosPrincipal
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TodoControllerTest {

    @InjectMocks
    lateinit var todoController: TodoController

    @Mock
    lateinit var todoServiceMock: TodoService

    @Mock
    lateinit var userServiceMock: UserService

    @Test
    fun canary() {
        Assert.assertNotNull(todoController)
        Assert.assertNotNull(todoServiceMock)
        Assert.assertNotNull(userServiceMock)
    }

    @Test
    fun shouldPersistTodo() {
        // given
        var userId = 1L
        var userEmail = "someemail@domain.com"
        var todoId = 2L
        var todoText = "do something"

        var user = User(userId, userEmail, "passwordHash")
        var todo = Todo(todoId, todoText, userId)

        var principal = KerberosPrincipal(userEmail)

        var newTodo = NewTodo(todoText)

        `when`(userServiceMock.findByEmail(anyString())).thenReturn(user)
        `when`(todoServiceMock.persist(anyObject())).thenReturn(todo)

        // when
        var result = todoController.persist(principal, newTodo)

        // then
        Assert.assertEquals(todo.id, result.id)
        Assert.assertEquals(todo.text, result.text)
        Assert.assertEquals(todo.userId, result.userId)
    }

    private fun <T> anyObject(): T {
        return Mockito.anyObject<T>()
    }
}
