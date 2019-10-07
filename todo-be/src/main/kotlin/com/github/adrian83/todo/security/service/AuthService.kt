package com.github.adrian83.todo.security

import com.github.adrian83.todo.domain.exception.NotFoundException
import com.github.adrian83.todo.domain.user.UserService
import com.github.adrian83.todo.domain.user.model.User
import com.github.adrian83.todo.security.exception.EmailAlreadyUserException
import com.github.adrian83.todo.security.exception.InvalidEmailOrPasswordException
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    val userService: UserService,
    val passwordEncoder: PasswordEncoder,
    val jwtTokenEncoder: JwtTokenEncoder
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }

    fun register(email: String, password: String): User {

        logger.info("registering user with email: $email")

        try {
            var hash = passwordEncoder.hash(password)
            var user = User(0, email, hash)
            return userService.persist(user)
        } catch (ex: DataIntegrityViolationException) {
            logger.warn("cannot register user because of: ${ex.message}")
            throw EmailAlreadyUserException("user with this email is already registered")
        } catch (ex: ConstraintViolationException) {
            logger.warn("cannot register user because of: ${ex.message}")
            throw EmailAlreadyUserException("user with this email is already registered")
        }
    }

    fun login(email: String, password: String): String {

        logger.info("signing in user with email: $email")

        try {
            var user = userService.findByEmail(email)

            if (!passwordEncoder.compare(password, user.passwordHash?:"")) {
                logger.warn("password verification failed for user with email: $email")
                throw InvalidEmailOrPasswordException("Invalid email or password")
            }

            var authToken = AuthToken(user.id, user.email)
            return jwtTokenEncoder.tokenToString(authToken)
        } catch (ex: NotFoundException) {
            logger.warn("cannot find user with email: $email")
            throw InvalidEmailOrPasswordException("Invalid email or password")
        }
    }
}
