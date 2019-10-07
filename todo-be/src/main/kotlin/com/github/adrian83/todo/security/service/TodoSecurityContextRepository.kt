package com.github.adrian83.todo.security

import com.github.adrian83.todo.domain.user.UserService
import java.lang.IllegalStateException
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class TodoSecurityContextRepository(
    var authenticationManager: AuthenticationManager,
    var userService: UserService
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void>? {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext>? {

        return Mono.justOrEmpty(exchange)
            .map { it!!.getRequest() }
            .flatMap { authTokenFromRequest(it) }
            .map { UsernamePasswordAuthenticationToken(it, it) }
            .flatMap { authenticationManager.authenticate(it) }
            .map { readPrincipal(it.getPrincipal()) }
            .map { it.getUsername() }
            .map { userService.findByEmail(it) }
            .flatMap { Mono.justOrEmpty(it) }
            .map { TodoUserDetails(it!!) }
            .map { UsernamePasswordAuthenticationToken(it, null, it.getAuthorities()) }
            .map { SecurityContextImpl(it) }
    }

    fun readPrincipal(principalObj: Any): UserDetails {
        if (principalObj is UserDetails) {
            return principalObj
        }
        throw IllegalStateException("Invalid Principal type. Expected UserDetails, got " + principalObj::class)
    }

    fun authTokenFromRequest(request: ServerHttpRequest): Mono<String> {
        var authToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)
        return Mono.justOrEmpty(authToken)
    }
}
