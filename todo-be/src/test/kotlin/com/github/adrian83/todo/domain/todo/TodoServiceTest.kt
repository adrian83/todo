package com.github.adrian83.todo.domain.todo

import com.github.adrian83.todo.domain.exception.NotFoundException
import com.github.adrian83.todo.domain.todo.model.Todo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TodoServiceTest {

    @InjectMocks
    lateinit var todoService: TodoService

    @Mock
    lateinit var todoRepositoryMock: TodoRepository

    @Test
    fun shouldPersistTodo() {

        // given
        val todo = Todo(1L, "this is test", 44)

        `when`(todoRepositoryMock.save(any(Todo::class.java))).thenReturn(todo)

        // when
        val result = todoService.persist(todo)

        // then
        verify(todoRepositoryMock).save(any(Todo::class.java))

        assertEquals(todo, result)
    }

    @Test
    fun shouldFindTodoByIdAndUser() {

        // given
        val todo = Todo(1L, "this is test", 44)

        `when`(todoRepositoryMock.findByIdAndUser(anyLong(), anyLong())).thenReturn(todo)

        // when
        val result = todoService.findByIdAndUser(12L, 55L)

        // then
        verify(todoRepositoryMock).findByIdAndUser(anyLong(), anyLong())

        assertEquals(todo, result)
    }

    @Test(expected = NotFoundException::class)
    fun shouldThrowAnExceptionIfNoneTodoCanBeFoundByIdAndUser() {

        // given
        `when`(todoRepositoryMock.findByIdAndUser(anyLong(), anyLong())).thenReturn(null)

        // when
        todoService.findByIdAndUser(12L, 55L)

        // then
        verify(todoRepositoryMock).findByIdAndUser(anyLong(), anyLong())
    }

    @Test
    fun shouldListTodos() {

        // given
        val todos = listOf(
            Todo(1L, "this is test", 44),
            Todo(2L, "this is also test", 44)
        )

        `when`(todoRepositoryMock.findAllByUserId(anyLong())).thenReturn(todos)

        // when
        val result = todoService.listByUser(243L)

        // then
        verify(todoRepositoryMock).findAllByUserId(anyLong())

        assertEquals(todos, result)
    }

    @Test
    fun shouldUpdateTodo() {

        // given
        val todo = Todo(1L, "this is test", 44)

        `when`(todoRepositoryMock.update(anyString(), anyLong(), anyLong())).thenReturn(1)

        // when
        val result = todoService.update(todo)

        // then
        verify(todoRepositoryMock).update(anyString(), anyLong(), anyLong())

        assertEquals(todo, result)
    }

    @Test(expected = NotFoundException::class)
    fun shouldThrowExceptionWhenNoneTodoCanBeUpdated() {

        // given
        val todo = Todo(1L, "this is test", 44)

        `when`(todoRepositoryMock.update(anyString(), anyLong(), anyLong())).thenReturn(0)

        // when
        todoService.update(todo)

        // then
        verify(todoRepositoryMock).update(anyString(), anyLong(), anyLong())
    }

    @Test
    fun shouldDeleteUsersTodos() {

        // given
        `when`(todoRepositoryMock.deleteByIdAndUser(anyLong(), anyLong())).thenReturn(5)

        // when
        todoService.deleteByIdAndUser(123L, 345L)

        // then
        verify(todoRepositoryMock).deleteByIdAndUser(anyLong(), anyLong())
    }

    @Test(expected = NotFoundException::class)
    fun shouldThrowExceptionWhenNoneTodoCanBeDeleted() {

        // given
        `when`(todoRepositoryMock.deleteByIdAndUser(anyLong(), anyLong())).thenReturn(0)

        // when
        todoService.deleteByIdAndUser(123L, 345L)

        // then
        verify(todoRepositoryMock).deleteByIdAndUser(anyLong(), anyLong())
    }
}
