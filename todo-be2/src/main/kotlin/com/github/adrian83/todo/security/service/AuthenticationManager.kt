package com.github.adrian83.todo.security

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    var userDetailsService: TodoUserDetailsService,
    var jwtTokenEncoder: JwtTokenEncoder
) : ReactiveAuthenticationManager {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationManager::class.java)
    }

    override fun authenticate(authentication: Authentication): Mono<Authentication>? {

        var tokenStr = authentication.getCredentials().toString()

        logger.info("authenticating user with token: $tokenStr")

        var authToken = jwtTokenEncoder.tokenFromString(tokenStr)
        var userDetailsMono = userDetailsService.findByUsername(authToken.email)

        return userDetailsMono.map {
            UsernamePasswordAuthenticationToken(it, null, it.getAuthorities())
        }
    }
}
