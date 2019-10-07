package com.github.adrian83.todo.domain.user

import com.github.adrian83.todo.domain.exception.NotFoundException
import com.github.adrian83.todo.domain.user.model.User
import com.github.adrian83.todo.security.exception.EmailAlreadyUserException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(val userRepository: UserRepository) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }

    fun persist(user: User): User {

        try {
            return userRepository.save(user)
        } catch (t: org.springframework.dao.DataIntegrityViolationException) {
            logger.warn("Exception: [${t::class.java}]")
            throw EmailAlreadyUserException("user with this email is already registered")
        }
    }

    fun findByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        if (user == null) {
            throw NotFoundException("User with email $email cannot be found")
        }
        return user
    }

    fun listAll(): List<User> = userRepository.findAll()
}
