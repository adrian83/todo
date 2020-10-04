package com.github.adrian83.todo.web

import com.github.adrian83.todo.domain.exception.NotFoundException
import com.github.adrian83.todo.domain.todo.TodoService
import com.github.adrian83.todo.domain.todo.model.Todo
import com.github.adrian83.todo.domain.user.UserService
import com.github.adrian83.todo.security.model.ConstraintViolation
import com.github.adrian83.todo.web.model.NewTodo
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.support.WebExchangeBindException
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping(value = arrayOf(TodoController.API_PREFIX))
class TodoController(
    val todoService: TodoService,
    val userService: UserService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TodoController::class.java)

        const val API_PREFIX = "api/v1/"
        const val RES_PREFIX = "todos"
    }

    @PostMapping(RES_PREFIX)
    fun persist(principal: Principal, @Valid @RequestBody newTodo: NewTodo): Todo {

        logger.info("creating new todo: $newTodo by ${principal.getName()}")

        val user = userService.findByEmail(principal.getName())
        return todoService.persist(Todo(0L, newTodo.text, user.id))
    }

    @GetMapping(
        value = [RES_PREFIX],
        produces = ["application/json; charset=utf-8"]
    )
    fun findAll(principal: Principal): List<Todo> {

        logger.info("listing all todos of ${principal.getName()}")

        val user = userService.findByEmail(principal.getName())
        return todoService.listByUser(user.id)
    }

    @GetMapping(RES_PREFIX + "/{id}")
    fun findById(principal: Principal, @PathVariable id: Long): Todo? {

        logger.info("getting todo with id $id by ${principal.getName()}")

        val user = userService.findByEmail(principal.getName())
        return todoService.findByIdAndUser(id, user.id)
    }

    @PutMapping(RES_PREFIX + "/{id}")
    fun update(principal: Principal, @PathVariable id: Long, @Valid @RequestBody newTodo: NewTodo): Todo? {

        logger.info("getting todo with id $id by ${principal.getName()}")

        val user = userService.findByEmail(principal.getName())
        var todo = Todo(id, newTodo.text, user.id)
        return todoService.update(todo)
    }

    @DeleteMapping(RES_PREFIX + "/{id}")
    fun delete(principal: Principal, @PathVariable id: Long) {

        logger.info("removing todo with id $id by ${principal.getName()}")

        val user = userService.findByEmail(principal.getName())
        todoService.deleteByIdAndUser(id, user.id)
    }

    @ExceptionHandler(value = arrayOf(RuntimeException::class))
    fun handleRunTimeException(ex: RuntimeException): ResponseEntity<out Any> {

        logger.info("exception [${ex::class}] with message: ${ex.message}")
        ex.printStackTrace()

        if (ex is NotFoundException) {
            return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
        } else if (ex is MethodArgumentNotValidException) {

            var violations = ex.getBindingResult().getAllErrors().map { ConstraintViolation(it) }
            return ResponseEntity<List<ConstraintViolation>>(violations, HttpStatus.BAD_REQUEST)
        } else if (ex is WebExchangeBindException) {

            var violations = ex.getAllErrors().map { ConstraintViolation(it) }
            return ResponseEntity<List<ConstraintViolation>>(violations, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity<String>(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
